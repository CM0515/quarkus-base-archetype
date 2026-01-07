# Guía de Arquitectura - Quarkus Base Archetype

## Visión General

Este arquetipo implementa una arquitectura en capas con principios de arquitectura limpia (Clean Architecture), separando la lógica de negocio de los frameworks y librerías.

## Capas de la Aplicación

```
┌─────────────────────────────────────────┐
│        REST Resources (Controllers)     │  Capa de Presentación
├─────────────────────────────────────────┤
│         Services (Business Logic)       │  Capa de Negocio
├─────────────────────────────────────────┤
│      Repositories (Data Access)         │  Capa de Persistencia
├─────────────────────────────────────────┤
│      Entities (Domain Models)           │  Capa de Dominio
└─────────────────────────────────────────┘
```

## Descripción de Componentes

### 1. Common (Componentes Transversales)

#### `exception/`
- **AppException**: Excepción personalizada base
- **ErrorCode**: Enumeración con códigos de error estándar
- Manejo centralizado de errores

#### `dto/`
- **ErrorResponse**: Estructura de respuesta para errores
- **SuccessResponse**: Estructura de respuesta para éxito
- Data Transfer Objects genéricos

#### `entity/`
- **BaseEntity**: Clase base para todas las entidades JPA
- Propiedades comunes: `id`, `createdAt`, `updatedAt`, `isActive`
- Hooks de ciclo de vida (`@PrePersist`, `@PreUpdate`)

#### `repository/`
- **BaseRepository**: Interfaz genérica con operaciones CRUD
- Métodos comunes: `findAllActive()`, `deactivate()`, etc.
- Extiende `PanacheRepository` de Quarkus

#### `service/`
- **BaseService**: Clase base para servicios
- Gestión automática de excepciones
- Métodos CRUD estándar

#### `rest/`
- **BaseResource**: Clase base para recursos REST
- Métodos helper para respuestas HTTP
- Documentación OpenAPI integrada

### 2. Config (Configuración)

- **AppConfiguration**: Inyección de propiedades de configuración
- Acceso a variables de entorno
- Métodos helper como `isProduction()`, `isDevelopment()`

### 3. Health (Monitoreo)

- **ApplicationHealthCheck**: Implementa `HealthCheck`
- Accesible en `/q/health`
- Integración con orquestadores como Kubernetes

### 4. Domain (Módulos de Negocio)

Cada módulo sigue la estructura:

```
domain/
└── module-name/
    ├── entity/          # Entidades JPA
    ├── repository/      # Acceso a datos
    ├── service/         # Lógica de negocio
    ├── rest/            # Endpoints REST
    └── dto/             # DTOs de entrada/salida
```

## Flujo de Datos

### Creación de Recurso

```
HTTP POST /api/resource
    ↓
Resource (REST endpoint)
    ├─ Recibe CreateRequest
    ├─ Valida (validación de bean)
    ↓
Service (Lógica de negocio)
    ├─ Validaciones adicionales
    ├─ Verifica duplicados
    ├─ Prepara datos
    ↓
Repository
    ├─ Persiste en BD
    ↓
Service retorna Entity
    ↓
Resource convierte a Response DTO
    ↓
HTTP 201 Created + ResponseDTO
```

### Manejo de Errores

```
Excepción en cualquier capa
    ↓
GlobalExceptionHandler (exception mapper)
    ├─ Detecta tipo de excepción
    ├─ Crea ErrorResponse
    ├─ Log automático
    ↓
HTTP error response con ErrorResponse JSON
```

## Patrones Implementados

### 1. Repository Pattern
```java
public interface BaseRepository<T> extends PanacheRepository<T> {
    // Acceso consistente a datos
    // Facilita testing con mocks
    // Encapsula lógica de queries
}
```

### 2. Service Layer
```java
public class UserService extends BaseService<User, UserRepository> {
    // Lógica de negocio
    // Validaciones
    // Orquestación
}
```

### 3. DTO Pattern
```java
CreateUserRequest  // Input DTO
UserResponse       // Output DTO
// Desacoplamiento de la API con las entidades
```

### 4. Exception Handling
```java
throw new AppException(
    ErrorCode.VALIDATION_ERROR,
    400,
    "Mensaje de error"
);
// Manejo centralizado y consistente
```

### 5. Health Check
```java
@Health
@ApplicationScoped
public class ApplicationHealthCheck implements HealthCheck {
    // Monitoreo de salud
    // Integración con K8s readiness/liveness probes
}
```

## Implementación de un CRUD Completo

### Paso 1: Crear la Entidad

```java
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    
    @NotBlank
    @Column(unique = true)
    private String name;
    
    @DecimalMin("0.01")
    private BigDecimal price;
    
    // getter/setter automáticos con Lombok
}
```

### Paso 2: Crear el Repositorio

```java
@ApplicationScoped
public class ProductRepository implements BaseRepository<Product> {
    
    public Optional<Product> findByName(String name) {
        return find("name", name).firstResultOptional();
    }
    
    public List<Product> findByPriceRange(BigDecimal min, BigDecimal max) {
        return find("price between ?1 and ?2", min, max).list();
    }
}
```

### Paso 3: Crear el Servicio

```java
@ApplicationScoped
@Transactional
public class ProductService extends BaseService<Product, ProductRepository> {
    
    @Inject
    ProductRepository productRepository;
    
    public Product createProduct(CreateProductRequest request) {
        if (productRepository.findByName(request.getName()).isPresent()) {
            throw new AppException(
                ErrorCode.DUPLICATE_RESOURCE,
                409,
                "Producto ya existe"
            );
        }
        return create(request.toEntity());
    }
}
```

### Paso 4: Crear el Recurso REST

```java
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Products")
public class ProductResource extends BaseResource {
    
    @Inject
    ProductService productService;
    
    @POST
    @Operation(summary = "Crear producto")
    public Response create(@Valid CreateProductRequest request) {
        Product product = productService.createProduct(request);
        return created(ProductResponse.fromEntity(product));
    }
    
    // ... más métodos
}
```

## Testing

### Unit Tests (Servicios)

```java
@QuarkusTest
class ProductServiceTest {
    
    @Inject
    ProductService productService;
    
    @InjectMock
    ProductRepository productRepository;
    
    @Test
    void testCreateProduct() {
        // Arrange
        when(productRepository.find(...)).thenReturn(...);
        
        // Act
        Product result = productService.createProduct(request);
        
        // Assert
        assertNotNull(result);
    }
}
```

### Integration Tests (REST)

```java
@QuarkusTest
class ProductResourceTest {
    
    @Test
    void testCreateProduct() {
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/products")
        .then()
            .statusCode(201)
            .body("success", is(true));
    }
}
```

## Convenciones de Código

### Nombres
- Entidades: Singular (User, Product)
- Tablas: Plural (users, products)
- Endpoints: Plural (GET /api/users, POST /api/products)
- Métodos get: `findByX` en repositorios
- Métodos crear: `create` en servicios

### Anotaciones Obligatorias
- `@Entity` en entidades
- `@ApplicationScoped` en repositorios y servicios
- `@Transactional` en servicios que modifican datos
- `@Inject` para inyección de dependencias
- `@Operation`, `@APIResponse` en endpoints REST

### Logging
```java
@Slf4j // Usa Lombok
public class MyClass {
    log.info("Mensaje informativo");
    log.debug("Información de debug");
    log.error("Error", exception);
}
```

## Seguridad

### Validación de Entrada
```java
@Post
public Response create(@Valid CreateUserRequest request) {
    // request ya fue validado por Hibernate Validator
}
```

### Gestión de Errores Segura
```java
// ✅ BIEN: No expone detalles internos
throw new AppException(
    ErrorCode.DATABASE_ERROR,
    500,
    "Error al procesar la solicitud"
);

// ❌ MAL: Expone stack trace
throw new RuntimeException(ex);
```

## Performance

### Lazy Loading
```java
@Entity
public class Order {
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
```

### Índices en BD
```sql
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_orders_user_id ON orders(user_id);
```

### Paginación
```java
public List<User> findAllActive(int page, int size) {
    return repository.find("isActive", true)
        .page(page, size)
        .list();
}
```

## Despliegue

### Desarrollo
```bash
./mvnw quarkus:dev
```

### Producción (JVM)
```bash
./mvnw clean package
java -jar target/quarkus-app-1.0.0-runner.jar
```

### Producción (Nativa)
```bash
./mvnw clean package -Pnative
./target/quarkus-app-1.0.0-runner
```

### Docker
```bash
docker build -t my-app:1.0.0 .
docker run -p 8080:8080 my-app:1.0.0
```

### Kubernetes
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: quarkus-app
spec:
  replicas: 3
  template:
    spec:
      containers:
      - name: app
        image: my-app:1.0.0
        ports:
        - containerPort: 8080
        readinessProbe:
          httpGet:
            path: /q/health
            port: 8080
        livenessProbe:
          httpGet:
            path: /q/health
            port: 8080
```

## Mejoras Futuras

- [ ] Implementar OAuth2/JWT
- [ ] Agregar caché (Redis)
- [ ] Implementar paginación avanzada
- [ ] Agregar auditoría (CreatedBy, UpdatedBy)
- [ ] Implementar soft deletes
- [ ] Agregar event sourcing
- [ ] Integración con Message brokers (Kafka)
- [ ] Implementar CQRS
