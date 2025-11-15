# Payment Customer Service

[![CI/CD](https://github.com/fintech-platform-benas/payment-customer-service/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/fintech-platform-benas/payment-customer-service/actions/workflows/ci-cd.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=fintech-platform-benas_payment-customer-service&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=fintech-platform-benas_payment-customer-service)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=fintech-platform-benas_payment-customer-service&metric=coverage)](https://sonarcloud.io/summary/new_code?id=fintech-platform-benas_payment-customer-service)

Microservicio de gestión de clientes para la plataforma Payment Chain.

## Tecnologías

- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Cloud 2023.0.0**
- **Spring Data JPA**
- **H2 Database** (desarrollo/testing)
- **Eureka Client** (service discovery)
- **Config Client** (configuración centralizada)
- **SpringDoc OpenAPI** (Swagger UI)
- **Lombok**
- **JaCoCo** (code coverage)

## Puertos

- **8081** - Puerto HTTP del servicio

## Endpoints Principales

### API REST

- `GET /api/customer` - Listar todos los clientes
- `GET /api/customer/{id}` - Obtener cliente por ID
- `POST /api/customer` - Crear nuevo cliente
- `PUT /api/customer/{id}` - Actualizar cliente
- `DELETE /api/customer/{id}` - Eliminar cliente

### Documentación API

- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8081/v3/api-docs

### Actuator

- **Health**: http://localhost:8081/actuator/health
- **Info**: http://localhost:8081/actuator/info
- **Metrics**: http://localhost:8081/actuator/metrics

## Requisitos

- **Java 17+**
- **Maven 3.6+**
- **Config Server** corriendo en puerto 8888
- **Eureka Server** corriendo en puerto 8761

## Configuración Local

### 1. Clonar el repositorio

```bash
git clone https://github.com/fintech-platform-benas/payment-customer-service.git
cd payment-customer-service
```

### 2. Compilar

```bash
mvn clean install
```

### 3. Ejecutar

```bash
mvn spring-boot:run
```

O ejecutar el JAR:

```bash
java -jar target/customer-0.0.1-SNAPSHOT.jar
```

## Variables de Entorno

```bash
# Config Server
SPRING_CLOUD_CONFIG_URI=http://localhost:8888

# Eureka Server
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/

# Database (H2)
SPRING_DATASOURCE_URL=jdbc:h2:mem:customerdb
SPRING_DATASOURCE_USERNAME=sa
SPRING_DATASOURCE_PASSWORD=

# Server Port
SERVER_PORT=8081
```

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/paymentchain/customer/
│   │       ├── controller/     # REST Controllers
│   │       ├── entities/       # JPA Entities
│   │       ├── repository/     # Spring Data Repositories
│   │       └── CustomerApplication.java
│   └── resources/
│       ├── application.properties
│       └── data.sql           # Datos iniciales
└── test/
    └── java/                  # Tests unitarios e integración
```

## Modelo de Datos

### Customer Entity

```java
@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private String code;
    private String names;
    private String phone;
    private String iban;
    private String surname;
    private String address;
}
```

## Testing

### Ejecutar tests

```bash
mvn test
```

### Reporte de cobertura

```bash
mvn verify
```

El reporte JaCoCo estará disponible en: `target/site/jacoco/index.html`

## Docker

### Build imagen

```bash
docker build -t payment-customer-service:latest .
```

### Ejecutar contenedor

```bash
docker run -p 8081:8081 \
  -e SPRING_CLOUD_CONFIG_URI=http://host.docker.internal:8888 \
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://host.docker.internal:8761/eureka/ \
  payment-customer-service:latest
```

## CI/CD

El proyecto utiliza **GitHub Actions** para CI/CD:

- ✅ Build automático en push a `main` y `develop`
- ✅ Ejecución de tests
- ✅ Análisis de código con SonarCloud
- ✅ Generación de cobertura con JaCoCo
- ✅ Build de imagen Docker
- ✅ Deploy a Nexus Repository

## Integración con Otros Servicios

Este servicio interactúa con:

- **Config Server** - Configuración centralizada
- **Eureka Server** - Service Discovery
- **API Gateway** - Routing y load balancing
- **Transaction Service** - Validación de transacciones por cliente

## Contribuir

1. Fork el proyecto
2. Crear feature branch (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -m 'Agregar nueva funcionalidad'`)
4. Push al branch (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## Licencia

Este proyecto es parte del portafolio educativo de fintech-platform-benas.

## Contacto

- **Organization**: [fintech-platform-benas](https://github.com/fintech-platform-benas)
- **Repository**: [payment-customer-service](https://github.com/fintech-platform-benas/payment-customer-service)