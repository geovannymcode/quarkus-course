# 📘 Bookmarker API – Quarkus 3

Aplicación REST construida con **Quarkus**, **Panache ORM**, y **Jakarta REST** para administrar marcadores (bookmarks) y categorías.

---

## 🧩 Estructura del Proyecto
```
quarkus-course/
 ├── src/
 │   ├── main/
 │   │   ├── docker/
 │   │   ├── java/com/geovannycode/bookmarker/
 │   │   │   ├── api/           # Controladores REST
 │   │   │   ├── config/        # Configuración OpenAPI y excepciones
 │   │   │   ├── entities/      # Entidades JPA
 │   │   │   ├── exceptions/    # Excepciones personalizadas
 │   │   │   ├── models/        # DTOs y modelos (PagedResult, etc.)
 │   │   │   ├── repository/    # Repositorios Panache
 │   │   │   ├── services/      # Lógica de negocio
 │   │   │   └── ApplicationProperties.java
 │   │   └── resources/
 │   │       ├── db/migration/  # Migraciones SQL (Flyway)
 │   │       └── application.properties
 │   └── test/                  # Tests de integración
 ├── target/
 ├── compose.yml
 ├── pom.xml
 ├── LICENSE
 └── README.md
```

---

## 🚀 Ejecución del Proyecto

### 🔧 Requisitos
- Java 21+
- Maven 3.9+
- Docker (opcional para Dev Services)

### ▶️ Modo desarrollo
```bash
./mvnw quarkus:dev
```

Accede en: [http://localhost:8080](http://localhost:8080)

### 📚 Documentación API (Swagger UI)
Una vez iniciada la aplicación, accede a la documentación interactiva:

- **Swagger UI**: [http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui)
- **OpenAPI JSON**: [http://localhost:8080/q/openapi](http://localhost:8080/q/openapi)
- **OpenAPI YAML**: [http://localhost:8080/q/openapi?format=yaml](http://localhost:8080/q/openapi?format=yaml)

---

## 🌐 Endpoints Principales

### 🔹 Health
**GET** `/api/health`
```json
{
  "name": "Bookmarker",
  "version": "1.0.0",
  "status": "UP"
}
```

### 🔹 Bookmarks
| Método | Endpoint | Descripción |
|---------|-----------|-------------|
| GET | `/api/bookmarks/all` | Lista todos los bookmarks |
| GET | `/api/bookmarks?page=1` | Paginado |
| GET | `/api/bookmarks/{id}` | Busca por id |
| POST | `/api/bookmarks` | Crea un bookmark |
| PUT | `/api/bookmarks/{id}` | Actualiza |
| DELETE | `/api/bookmarks/{id}` | Elimina |

**Ejemplo POST**
```bash
curl -X POST http://localhost:8080/api/bookmarks \
  -H "Content-Type: application/json" \
  -d '{"title":"Quarkus Docs","url":"https://quarkus.io","description":"Sitio oficial"}'
```

> 💡 **Tip**: Para ver todos los endpoints con ejemplos y probarlos directamente, visita [Swagger UI](http://localhost:8080/q/swagger-ui)

---

## ⚙️ Configuración Clave (`application.properties`)
```properties
# Server
quarkus.http.port=8080

# Database
quarkus.flyway.migrate-at-start=true
quarkus.datasource.devservices.image-name=postgres:17-alpine
quarkus.hibernate-orm.log.sql=true

# Pagination
app.page-size=10

# OpenAPI / Swagger
quarkus.swagger-ui.always-include=true
quarkus.swagger-ui.path=/q/swagger-ui
quarkus.smallrye-openapi.path=/q/openapi

# Logging
quarkus.log.level=INFO
```

---

## 🧪 Tests

Ejecuta los tests con:
```bash
./mvnw test
```

Incluye pruebas de integración reales (`@QuarkusTest`) para:
- `CategoryServiceTest`
- `BookmarkServiceTest`
- `BookmarkControllerTest` (REST Assured)

---

## 🧠 Tecnologías

| Categoría | Tecnología |
|------------|-------------|
| Framework | Quarkus 3 |
| REST | Jakarta REST |
| ORM | Hibernate + Panache |
| DB | PostgreSQL / H2 |
| Migraciones | Flyway |
| Cache | Quarkus Cache |
| Documentación | OpenAPI 3.0 + Swagger UI |
| Testing | JUnit 5 + REST Assured + Quarkus Test |

---

## 📖 Características

- ✅ **API REST** completa con CRUD para bookmarks y categorías
- ✅ **Documentación automática** con OpenAPI/Swagger
- ✅ **Paginación** configurable
- ✅ **Validación** de datos con Bean Validation
- ✅ **Cache** para optimización de queries
- ✅ **Migraciones** de base de datos con Flyway
- ✅ **Tests de integración** completos
- ✅ **Dev Services** con PostgreSQL en Docker

---

## 👨‍💻 Autor

**Geovanny Mendoza**  
Backend Developer – Java, Spring, Quarkus, Kotlin  
🌐 [https://geovannycode.com](https://geovannycode.com)  
🐦 [@geovannycode](https://x.com/geovannycode)