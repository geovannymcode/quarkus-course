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
 │   │   │   ├── config/        # Manejador global de excepciones
 │   │   │   ├── entities/      # Entidades JPA
 │   │   │   ├── exceptions/    # Excepciones personalizadas
 │   │   │   ├── models/        # Modelos comunes (PagedResult, etc.)
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
curl -X POST http://localhost:8080/api/bookmarks   -H "Content-Type: application/json"   -d '{"title":"Quarkus Docs","url":"https://quarkus.io","description":"Sitio oficial"}'
```

### 🔹 Categories
| Método | Endpoint | Descripción |
|---------|-----------|-------------|
| GET | `/api/categories` | Lista categorías |
| GET | `/api/categories?page=1` | Paginado |
| GET | `/api/categories/{slug}` | Busca por slug |
| POST | `/api/categories` | Crea categoría |
| PUT | `/api/categories/{id}` | Actualiza |
| DELETE | `/api/categories/{id}` | Elimina |

---

## ⚙️ Configuración Clave (`application.properties`)

```properties
quarkus.http.port=8080
quarkus.flyway.migrate-at-start=true
app.page-size=10

quarkus.datasource.devservices.image-name=postgres:17-alpine
quarkus.hibernate-orm.log.sql=true
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
| Testing | JUnit 5 + Quarkus Test |

---

## 👨‍💻 Autor
**Geovanny Mendoza**  
Backend Developer – Kotlin, Java, Spring, Quarkus  
🌐 [https://geovannycode.com](https://geovannycode.com)  
🐦 [@geovannycode](https://x.com/geovannycode)
