# Quarkus Base Archetype

Un arquetipo completo y profesional para desarrollar microservicios con Quarkus, siguiendo las mejores prácticas de arquitectura limpia y estructura empresarial.

## Características

✅ **Arquitectura escalable**: Estructura modular con separación de capas  
✅ **Gestión de errores centralizada**: Exception Handler global  
✅ **Configuración flexible**: Soporte para YAML y propiedades  
✅ **Base de datos**: Hibernate ORM con Panache  
✅ **API REST**: RESTEasy con documentación OpenAPI/Swagger  
✅ **Validación**: Hibernate Validator integrado  
✅ **Health Checks**: Monitoreo de salud de la aplicación  
✅ **Métricas**: Prometheus integrado  
✅ **Testing**: Configurado con JUnit 5 y REST-Assured  
✅ **Docker**: Dockerfile y docker-compose para desarrollo  
✅ **Logging**: Configuración de logs en JSON  

## Estructura del Proyecto

```
quarkus-base-archetype/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/microservice/
│   │   │       ├── QuarkusApplication.java          # Clase principal
│   │   │       ├── common/                          # Componentes comunes
│   │   │       │   ├── dto/                         # Data Transfer Objects
│   │   │       │   ├── entity/                      # Entidades base
│   │   │       │   ├── exception/                   # Manejo de excepciones
│   │   │       │   ├── repository/                  # Repositorios base
│   │   │       │   ├── rest/                        # Recursos base
│   │   │       │   └── service/                     # Servicios base
│   │   │       ├── config/                          # Configuración
│   │   │       ├── health/                          # Health Checks
│   │   │       └── domain/                          # Módulos de negocio
│   │   │           └── user/                        # Ejemplo: Módulo de usuarios
│   │   │               ├── dto/
│   │   │               ├── entity/
│   │   │               ├── repository/
│   │   │               ├── rest/
│   │   │               └── service/
│   │   └── resources/
│   │       └── application.yaml                     # Configuración
│   └── test/
│       └── java/com/example/microservice/           # Tests
├── pom.xml                                          # Dependencias Maven
├── Dockerfile                                       # Imagen nativa
├── docker-compose.yml                               # Servicios locales
├── init.sql                                         # Scripts SQL
└── README.md
```

## Requisitos Previos

- **Java 17+**
- **Maven 3.8+**
- **Docker & Docker Compose** (opcional, para desarrollo con contenedores)
- **PostgreSQL 13+** (opcional, si no usas Docker)

## Instalación y Ejecución

### 1. Usando Maven (Desarrollo Local)

```bash
# Clonar o descargar el proyecto
cd quarkus-base-archetype

# Instalar dependencias
./mvnw clean install

# Ejecutar en modo desarrollo (hot-reload)
./mvnw quarkus:dev

# La aplicación estará disponible en http://localhost:8080
# Swagger UI: http://localhost:8080/q/swagger-ui/
# Prometheus: http://localhost:8080/q/metrics/
```

### 2. Usando Docker Compose (Recomendado)

```bash
# Construir y ejecutar todos los servicios
docker-compose up --build

# Para detener
docker-compose down

# Para ver logs
docker-compose logs -f quarkus-app
```

### 3. Compilación a Imagen Nativa

```bash
# Compilar a imagen nativa (requiere GraalVM)
./mvnw clean package -Pnative

# Ejecutar la aplicación nativa
./target/quarkus-base-archetype-1.0.0-runner

# O compilar como imagen Docker nativa
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
```

## Configuración

### Variables de Entorno

```yaml
# Base de datos
DB_USER: postgres
DB_PASSWORD: postgres
DB_URL: jdbc:postgresql://localhost:5432/quarkus_db

# Seguridad
JWT_SECRET: tu-clave-secreta-de-32-caracteres

# Aplicación
app.environment: development | production
app.name: quarkus-base
```

### Archivo application.yaml

El archivo `src/main/resources/application.yaml` contiene toda la configuración:
- Puerto HTTP
- Configuración de base de datos
- Logging
- OpenAPI/Swagger
- Salud y métricas

## Endpoints Disponibles

### API de Usuarios (Ejemplo)

```
GET    /api/users                    # Obtener todos los usuarios
GET    /api/users/{id}               # Obtener usuario por ID
GET    /api/users/email/{email}      # Obtener usuario por email
POST   /api/users                    # Crear nuevo usuario
PUT    /api/users/{id}               # Actualizar usuario
DELETE /api/users/{id}               # Eliminar usuario
```

### Endpoints del Sistema

```
GET  /q/health                       # Health check
GET  /q/metrics                      # Métricas Prometheus
GET  /api/docs                       # OpenAPI schema
GET  /api/swagger-ui/                # Swagger UI
```

## Ejemplo de Uso

### Crear un Usuario

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890",
    "address": "123 Main St",
    "role": "USER"
  }'
```

### Obtener Usuario

```bash
curl http://localhost:8080/api/users/1
```

### Actualizar Usuario

```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "email": "jane@example.com",
    "phone": "+1234567891",
    "address": "456 Oak Ave",
    "role": "ADMIN"
  }'
```

### Eliminar Usuario

```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## Testing

```bash
# Ejecutar todos los tests
./mvnw test

# Ejecutar un test específico
./mvnw test -Dtest=UserServiceTest

# Con cobertura
./mvnw test jacoco:report
```

## Crear un Nuevo Módulo

Para agregar un nuevo módulo de negocio:

1. Crear estructura de directorios en `src/main/java/com/example/microservice/domain/`
2. Crear la entidad extends `BaseEntity`
3. Crear el repositorio implements `BaseRepository<T>`
4. Crear el servicio extends `BaseService<T, R>`
5. Crear el recurso REST extends `BaseResource`
6. Crear DTOs de solicitud y respuesta

Ejemplo:

```java
// Entity
@Entity
@Table(name = "products")
public class Product extends BaseEntity {
    // propiedades...
}

// Repository
@ApplicationScoped
public class ProductRepository implements BaseRepository<Product> {
    // métodos custom...
}

// Service
@ApplicationScoped
public class ProductService extends BaseService<Product, ProductRepository> {
    // lógica de negocio...
}

// Resource
@Path("/products")
public class ProductResource extends BaseResource {
    // endpoints REST...
}
```

## Mejores Prácticas

1. **Separación de responsabilidades**: Usa las capas entity, repository, service y rest
2. **Manejo de errores**: Siempre lanza `AppException` en lugar de excepciones genéricas
3. **Logging**: Usa `@Slf4j` para logging estructurado
4. **Validación**: Usa anotaciones `@NotBlank`, `@Email`, etc. en DTOs
5. **Documentación**: Agrega anotaciones `@Operation`, `@APIResponse` en endpoints
6. **Testing**: Escribe tests para servicios (unit) y recursos (integración)
7. **Transacciones**: Usa `@Transactional` en métodos que modifican datos

## Recursos Útiles

- [Documentación oficial de Quarkus](https://quarkus.io)
- [Guía de Panache](https://quarkus.io/guides/hibernate-orm-panache)
- [OpenAPI con Quarkus](https://quarkus.io/guides/openapi-swaggerui)
- [Testing en Quarkus](https://quarkus.io/guides/getting-started-testing)
- [Compilación nativa con GraalVM](https://quarkus.io/guides/building-native-image)

## Troubleshooting

### Puerto 8080 en uso
```bash
# Cambiar puerto en application.yaml
quarkus:
  http:
    port: 8081
```

### Error de conexión a BD
```bash
# Verificar credenciales en application.yaml
# Verificar que PostgreSQL está corriendo
# En Docker: docker-compose up postgres
```

### Maven no encuentra dependencias
```bash
./mvnw clean install -U
```

## Licencia

Apache License 2.0

## Contribuciones

Las contribuciones son bienvenidas. Por favor:
1. Fork el proyecto
2. Crea una rama para tu feature
3. Commit tus cambios
4. Push a la rama
5. Abre un Pull Request
