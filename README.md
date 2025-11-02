# 📘 Bookmarker API – Quarkus 3 🔐

Aplicación REST segura construida con **Quarkus**, **Panache ORM**, **Jakarta REST** y **JWT Authentication** para administrar marcadores (bookmarks) y categorías con autenticación basada en roles.

---

## 🧩 Estructura del Proyecto
```
quarkus-course/
 ├── src/
 │   ├── main/
 │   │   ├── docker/
 │   │   ├── java/com/geovannycode/bookmarker/
 │   │   │   ├── api/           # Controladores REST
 │   │   │   │   ├── AppInfoController.java
 │   │   │   │   ├── AuthController.java  # 🔐 NEW
 │   │   │   │   └── BookmarkController.java
 │   │   │   ├── config/        # Configuración OpenAPI y excepciones
 │   │   │   ├── entities/      # Entidades JPA
 │   │   │   │   ├── Bookmark.java
 │   │   │   │   ├── Category.java
 │   │   │   │   └── User.java  # 🔐 NEW
 │   │   │   ├── exceptions/    # Excepciones personalizadas
 │   │   │   ├── models/        # DTOs y modelos
 │   │   │   │   ├── AuthRequest.java      # 🔐 NEW
 │   │   │   │   ├── AuthResponse.java     # 🔐 NEW
 │   │   │   │   ├── RegisterRequest.java  # 🔐 NEW
 │   │   │   │   ├── UserResponse.java     # 🔐 NEW
 │   │   │   │   └── ...
 │   │   │   ├── repository/    # Repositorios Panache
 │   │   │   │   └── UserRepository.java   # 🔐 NEW
 │   │   │   ├── services/      # Lógica de negocio
 │   │   │   │   └── AuthService.java      # 🔐 NEW
 │   │   │   └── ApplicationProperties.java
 │   │   └── resources/
 │   │       ├── db/migration/  # Migraciones SQL (Flyway)
 │   │       │   ├── V1__create_categories.sql
 │   │       │   ├── V2__create_bookmarks.sql
 │   │       │   └── V3__create_users.sql  # 🔐 NEW
 │   │       ├── META-INF/resources/
 │   │       │   ├── privateKey.pem        # 🔐 NEW
 │   │       │   └── publicKey.pem         # 🔐 NEW
 │   │       └── application.properties
 │   └── test/                  # Tests de integración
 ├── target/
 ├── compose.yml
 ├── pom.xml
 ├── postman_collection.json    # 🔐 UPDATED
 ├── LICENSE
 └── README.md
```

---

## 🔐 Seguridad y Autenticación

La API está protegida con **JWT (JSON Web Tokens)** y soporta autenticación basada en roles:

### 🎭 Roles Disponibles
- **USER**: Usuario estándar (puede crear y actualizar bookmarks)
- **ADMIN**: Administrador (puede eliminar bookmarks y gestionar usuarios)

### 🔑 Endpoints de Autenticación

| Método | Endpoint | Descripción | Acceso |
|---------|-----------|-------------|---------|
| POST | `/api/auth/register` | Registrar nuevo usuario | Público |
| POST | `/api/auth/login` | Iniciar sesión | Público |
| GET | `/api/auth/me` | Obtener usuario actual | Requiere JWT |

### 🛡️ Matriz de Permisos

| Endpoint | Público | USER | ADMIN |
|----------|---------|------|-------|
| `GET /api/bookmarks/*` | ✅ | ✅ | ✅ |
| `POST /api/bookmarks` | ❌ | ✅ | ✅ |
| `PUT /api/bookmarks/{id}` | ❌ | ✅ | ✅ |
| `DELETE /api/bookmarks/{id}` | ❌ | ❌ | ✅ |

### 👤 Usuario Admin por Defecto
```
Username: admin
Password: admin123
Roles: ADMIN, USER
```

---

## 🚀 Ejecución del Proyecto

### 🔧 Requisitos
- Java 21+
- Maven 3.9+
- Docker (opcional para Dev Services)
- OpenSSL (para generar claves JWT)

### 🔐 Configuración Inicial (Primera vez)

**1. Generar claves RSA para JWT:**
```bash
# Generar clave privada
openssl genrsa -out privateKey.pem 2048

# Generar clave pública
openssl rsa -in privateKey.pem -pubout -out publicKey.pem

# Mover a resources
mkdir -p src/main/resources/META-INF/resources
mv privateKey.pem src/main/resources/META-INF/resources/
mv publicKey.pem src/main/resources/META-INF/resources/
```

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

> 💡 **Tip**: En Swagger UI, haz clic en el botón **"Authorize" 🔒** (esquina superior derecha) para ingresar tu JWT token y probar endpoints protegidos.

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

### 🔐 Autenticación

#### Registrar Usuario
**POST** `/api/auth/register`
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "geovannycode",
    "email": "contact@geovannycode.com",
    "password": "SecurePass123!",
    "fullName": "Geovanny Mendoza"
  }'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "username": "geovannycode",
  "roles": "USER"
}
```

#### Iniciar Sesión
**POST** `/api/auth/login`
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

#### Usuario Actual
**GET** `/api/auth/me` 🔒 *Requiere JWT*
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 🔹 Bookmarks

| Método | Endpoint | Descripción | Autenticación |
|---------|-----------|-------------|---------------|
| GET | `/api/bookmarks/all` | Lista todos los bookmarks | No requerida |
| GET | `/api/bookmarks?page=1` | Paginado | No requerida |
| GET | `/api/bookmarks/{id}` | Busca por id | No requerida |
| POST | `/api/bookmarks` | Crea un bookmark | 🔒 USER/ADMIN |
| PUT | `/api/bookmarks/{id}` | Actualiza | 🔒 USER/ADMIN |
| DELETE | `/api/bookmarks/{id}` | Elimina | 🔒 ADMIN only |

#### Crear Bookmark (Autenticado)
```bash
curl -X POST http://localhost:8080/api/bookmarks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "title": "Quarkus Security",
    "url": "https://quarkus.io/guides/security",
    "description": "Official security guide"
  }'
```

#### Eliminar Bookmark (Solo Admin)
```bash
curl -X DELETE http://localhost:8080/api/bookmarks/1 \
  -H "Authorization: Bearer YOUR_ADMIN_JWT_TOKEN"
```

> 💡 **Tip**: Usa la colección de Postman incluida (`postman_collection.json`) para probar todos los endpoints con autenticación automática.

---

## 📦 Colección de Postman

Importa `postman_collection.json` en Postman para obtener:
- ✅ Todos los endpoints preconfigurados
- ✅ Flujo completo de autenticación
- ✅ Gestión automática de tokens JWT
- ✅ Tests de validación incluidos
- ✅ Variables de entorno configuradas

**Características de la colección:**
1. **Auto-login**: Las requests protegidas ejecutan login automáticamente si no hay token
2. **Token persistente**: El JWT se guarda automáticamente y se reutiliza
3. **Tests integrados**: Validación automática de respuestas
4. **Flujo completo**: Register → Login → CRUD operations

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

# JWT Configuration 🔐
mp.jwt.verify.publickey.location=META-INF/resources/publicKey.pem
mp.jwt.verify.issuer=bookmarker-api
jwt.duration=24h
smallrye.jwt.sign.key.location=META-INF/resources/privateKey.pem

# Security Configuration 🔐
quarkus.http.auth.permission.public.paths=/api/health,/api/auth/*,/api/bookmarks,/api/bookmarks/*,/q/*
quarkus.http.auth.permission.public.policy=permit
quarkus.http.auth.permission.authenticated.paths=/api/bookmarks
quarkus.http.auth.permission.authenticated.policy=authenticated

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
- `AuthServiceTest` 🔐 (Authentication flows)

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
| Seguridad 🔐 | **Quarkus Security JPA + SmallRye JWT** |
| Password Hash 🔐 | **BCrypt** |
| Documentación | OpenAPI 3.0 + Swagger UI |
| Testing | JUnit 5 + REST Assured + Quarkus Test |

---

## 📖 Características

- ✅ **API REST** completa con CRUD para bookmarks y categorías
- ✅ **Autenticación JWT** 🔐 con tokens RSA256
- ✅ **Autorización basada en roles** 🔐 (USER, ADMIN)
- ✅ **Password hashing** 🔐 con BCrypt
- ✅ **Documentación automática** con OpenAPI/Swagger + Security Schemes
- ✅ **Paginación** configurable
- ✅ **Validación** de datos con Bean Validation
- ✅ **Cache** para optimización de queries
- ✅ **Migraciones** de base de datos con Flyway
- ✅ **Tests de integración** completos (incluye auth tests)
- ✅ **Dev Services** con PostgreSQL en Docker
- ✅ **Colección de Postman** con flujo de autenticación automatizado

---

## 🔒 Flujo de Autenticación

```
┌─────────┐                    ┌─────────┐                    ┌──────────┐
│ Client  │                    │   API   │                    │    DB    │
└────┬────┘                    └────┬────┘                    └────┬─────┘
     │                              │                              │
     │  POST /api/auth/register     │                              │
     │─────────────────────────────>│                              │
     │                              │   Guardar usuario            │
     │                              │   (password hasheada)        │
     │                              │─────────────────────────────>│
     │                              │                              │
     │                              │   Usuario creado             │
     │                              │<─────────────────────────────│
     │   JWT Token + User info      │                              │
     │<─────────────────────────────│                              │
     │                              │                              │
     │  POST /api/auth/login        │                              │
     │─────────────────────────────>│                              │
     │                              │   Verificar credenciales     │
     │                              │─────────────────────────────>│
     │                              │                              │
     │                              │   Usuario válido             │
     │                              │<─────────────────────────────│
     │   JWT Token (válido 24h)     │                              │
     │<─────────────────────────────│                              │
     │                              │                              │
     │  POST /api/bookmarks         │                              │
     │  + JWT Header                │                              │
     │─────────────────────────────>│                              │
     │                              │   Validar JWT y roles        │
     │                              │                              │
     │                              │   Crear bookmark             │
     │                              │─────────────────────────────>│
     │                              │                              │
     │                              │   Bookmark creado            │
     │                              │<─────────────────────────────│
     │   201 Created                │                              │
     │<─────────────────────────────│                              │
     │                              │                              │
     │  DELETE /api/bookmarks/1     │                              │
     │  + JWT Header                │                              │
     │─────────────────────────────>│                              │
     │                              │   Verificar rol ADMIN        │
     │                              │                              │
     │                              ├─ Es ADMIN?                   │
     │                              │     │                        │
     │                              │     └─ SÍ ──> Eliminar      │
     │                              │              bookmark        │
     │   204 No Content             │─────────────────────────────>│
     │<─────────────────────────────│                              │
     │                              │                              │
     │                              │     └─ NO ──> 403 Forbidden │
     │   403 Forbidden              │                              │
     │<─────────────────────────────│                              │
```

---

## 🚦 Ejemplos de Uso

### 1️⃣ Registrar y Obtener Token
```bash
# Registrar nuevo usuario
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "developer",
    "email": "dev@example.com",
    "password": "DevPass123!",
    "fullName": "Dev User"
  }'

# Guardar el token de la respuesta
# Token: eyJhbGciOiJSUzI1NiIsInR5cCI...
```

### 2️⃣ Usar Token en Requests
```bash
# Crear bookmark (requiere USER role)
curl -X POST http://localhost:8080/api/bookmarks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI..." \
  -d '{
    "title": "My Bookmark",
    "url": "https://example.com",
    "description": "Example bookmark"
  }'
```

### 3️⃣ Admin Operations
```bash
# Login como admin
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'

# Eliminar bookmark (requiere ADMIN role)
curl -X DELETE http://localhost:8080/api/bookmarks/5 \
  -H "Authorization: Bearer ADMIN_TOKEN_HERE"
```

---

## 🔧 Troubleshooting

### ❌ Error: "Unauthorized" (401)
- Verifica que estés enviando el header `Authorization: Bearer YOUR_TOKEN`
- Confirma que el token no haya expirado (válido por 24h)
- Intenta hacer login nuevamente para obtener un token fresco

### ❌ Error: "Forbidden" (403)
- Tu usuario no tiene el rol necesario para esta operación
- DELETE bookmarks requiere rol ADMIN
- Verifica tus roles con `GET /api/auth/me`

### ❌ Error: "Keys not found"
- Genera las claves RSA siguiendo las instrucciones en "Configuración Inicial"
- Asegúrate de que `privateKey.pem` y `publicKey.pem` estén en `src/main/resources/META-INF/resources/`

---

## 👨‍💻 Autor

**Geovanny Mendoza**  
Backend Developer – Java, Spring, Quarkus, Kotlin  
🌐 [https://geovannycode.com](https://geovannycode.com)  
🐦 [@geovannycode](https://x.com/geovannycode)

---

## 📄 Licencia

Este proyecto está bajo la licencia MIT. Ver archivo [LICENSE](LICENSE) para más detalles.

---

## 🙏 Agradecimientos

- [Quarkus](https://quarkus.io) - Framework supersónico subatómico
- [SmallRye JWT](https://smallrye.io/) - Implementación de MicroProfile JWT
- Comunidad de desarrolladores Java

---

**⭐ Si te gusta este proyecto, dale una estrella en GitHub!**
