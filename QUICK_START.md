# 🚀 Inicio Rápido - Quarkus Base Archetype

## ⚡ En 5 Minutos

### 1. **Requisitos**
```bash
java -version          # Java 17+
mvn -version           # Maven 3.8+
docker --version       # Docker (opcional)
```

### 2. **Clonar/Descargar Proyecto**
```bash
cd quarkus-base-archetype
```

### 3. **Ejecutar con Maven**
```bash
# Opción A: Modo desarrollo (hot-reload)
./mvnw quarkus:dev

# La app estará en: http://localhost:8080
```

### 4. **Ejecutar con Docker Compose**
```bash
# Opción B: Con base de datos
docker-compose up --build

# PostgreSQL: localhost:5432
# App REST: localhost:8080
# PgAdmin: localhost:5050
```

## 📖 Primeros Pasos

### Ver Documentación OpenAPI
```
http://localhost:8080/q/swagger-ui/
```

### Crear un usuario (Ejemplo)
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

### Obtener todos los usuarios
```bash
curl http://localhost:8080/api/users
```

### Health Check
```bash
curl http://localhost:8080/q/health
```

### Métricas Prometheus
```bash
curl http://localhost:8080/q/metrics
```

## 📁 Estructura Rápida

```
├── src/main/java/com/example/microservice/
│   ├── common/              ← Base reutilizable
│   ├── config/              ← Configuración
│   ├── domain/user/         ← Tu módulo de negocio
│   └── health/              ← Health checks
├── pom.xml                  ← Dependencias
├── docker-compose.yml       ← BD local
└── README.md                ← Documentación
```

## 🔨 Crear un Nuevo Módulo

1. **Crear estructura** (ejemplo: productos)
```
domain/
└── product/
    ├── entity/
    ├── repository/
    ├── service/
    ├── rest/
    └── dto/
```

2. **Copiar de User y adaptar** (es el ejemplo)

## 🧪 Testing

```bash
# Unit tests
./mvnw test

# Test específico
./mvnw test -Dtest=UserServiceTest

# Con cobertura
./mvnw test jacoco:report
```

## 🐳 Docker & Kubernetes

### Compilar Imagen
```bash
# JVM
./mvnw clean package
docker build -t my-app:1.0 .

# Nativa (más rápido)
./mvnw clean package -Pnative -Dquarkus.native.container-build=true
```

### Ejecutar Localmente
```bash
docker run -p 8080:8080 my-app:1.0
```

## 📊 Comandos Útiles

```bash
# Compilar
./mvnw clean install

# Compilación rápida
./mvnw quarkus:dev

# Tests
./mvnw test

# Build nativo
./mvnw package -Pnative -DskipTests

# Limpiar
./mvnw clean

# Ver dependencias
./mvnw dependency:tree
```

## 🔧 Configuración

### Cambiar Puerto
En `src/main/resources/application.yaml`:
```yaml
quarkus:
  http:
    port: 8081
```

### Cambiar BD
```yaml
quarkus:
  datasource:
    jdbc:
      url: jdbc:postgresql://localhost/otra_bd
    username: usuario
    password: contraseña
```

## 🎯 Checklist de Desarrollo

- [ ] Entender la estructura en `ARCHITECTURE.md`
- [ ] Crear primer módulo copiando `domain/user`
- [ ] Escribir tests (unit + integration)
- [ ] Agregar documentación OpenAPI
- [ ] Configurar variables de entorno
- [ ] Documentar el módulo

## 🐛 Troubleshooting

### Puerto en uso
```bash
# Cambiar en application.yaml
quarkus.http.port: 8081
```

### BD no conecta
```bash
# Verificar credenciales
# Verificar BD está corriendo: docker-compose ps
# Reiniciar: docker-compose restart postgres
```

### Maven lento
```bash
./mvnw clean install -U -o  # Offline
```

## 📚 Recursos

| Recurso | URL |
|---------|-----|
| Docs Quarkus | https://quarkus.io |
| Guía Panache | https://quarkus.io/guides/hibernate-orm-panache |
| Testing | https://quarkus.io/guides/getting-started-testing |
| OpenAPI | https://quarkus.io/guides/openapi-swaggerui |

## 🎓 Siguientes Pasos

1. **Lee** `README.md` - Documentación completa
2. **Lee** `ARCHITECTURE.md` - Explicación detallada
3. **Copia** módulo `user` para crear el tuyo
4. **Ejecuta** los tests
5. **Despliega** a Docker

---

**¿Preguntas?** Revisa los archivos `.md` incluidos. ¡Feliz codificación! 🎉
