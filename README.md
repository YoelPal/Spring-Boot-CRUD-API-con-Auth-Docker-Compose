# springboot-crud-Security

Proyecto de ejemplo: API REST CRUD con Spring Boot y seguridad JWT.

Este repositorio contiene una aplicación Spring Boot (Java 17) que implementa un CRUD básico para productos y gestión de usuarios/roles con autenticación y autorización basada en JSON Web Tokens (JWT). 

## Tecnologías

- Java 17
- Spring Boot 3.5.x
- Spring Security (JWT)
- Spring Data JPA (Hibernate)
- MySQL (conector `mysql-connector-j`)
- jjwt (io.jsonwebtoken) para JWT
- Lombok (opcional, presente en el `pom.xml`)
- Maven (con wrapper `mvnw` / `mvnw.cmd`)

## Características

- CRUD completo para `Product` (/api/products)
  - GET /api/products
  - GET /api/products/{id}
  - POST /api/products (requiere rol ADMIN)
  - PUT /api/products/{id} (requiere rol ADMIN)
  - DELETE /api/products/{id} (requiere rol ADMIN)
- Gestión de `User` y `Role` (entidades JPA)
- Seguridad basada en JWT
  - Login vía POST /login (envía JSON {"username":"...","password":"..."})
  - Si la autenticación es correcta, la respuesta incluye un token y también se agrega en la cabecera `Authorization: Bearer <token>`
- Stateless: la aplicación usa sesiones sin estado (JWT)

## Estructura relevante

- `src/main/java/com/yoel/springboot/app/springboot_crud/` – paquete principal
  - `controllers/` – controladores REST (ej. `ProductControllerImpl`)
  - `entities/` – entidades JPA (`Product`, `User`, `Role`)
  - `security/` – configuración de Spring Security, filtros JWT (`JwtAuthenticationFilter`, `JwtValidationFilter`) y `TokenJwtConfig`
  - `services/`, `repositories/` – lógica de negocio y acceso a datos
  - `config/` – configuraciones (ej. `DataInitializer`, `ValidationConfig`)
- `src/main/resources/application.properties` – configuración (datasource, JPA)
- `pom.xml` – dependencias y plugins
- **`Dockerfile`** – definición para construir la imagen Docker de la aplicación
- **`docker-compose.yml`** – orquestación de contenedores (MySQL + Spring Boot app)
- `.mvn/`, `mvnw`, `mvnw.cmd` – Maven Wrapper (permite ejecutar Maven sin instalarlo globalmente)

## Requisitos

- Java 17 instalado
- MySQL (o cambiar la URL en `application.properties` a tu base de datos preferida)
- Variables de entorno opcionales usadas en `application.properties`:
  - `DB_URL` 
  - `DB_USER` ( por defecto `root`)
  - `DB_PASSWORD` (por defecto `123456`)

## Configuración rápida (local, Windows PowerShell)

### Opción 1: Instalación Manual (sin Docker)

1. Clonar el repositorio.
2. Configurar MySQL: crear la base de datos `springboot_crud` y ajustar puerto/credenciales si es necesario.
3. Ejecutar (usar el wrapper incluido):

```powershell
# Compilar y empaquetar
.\mvnw.cmd clean package

# Ejecutar la aplicación
.\mvnw.cmd spring-boot:run
```

El servidor arrancará típicamente en http://localhost:8080 (por defecto de Spring Boot).

### Opción 2: Docker + Docker Compose 

Con Docker Compose levantarás tanto MySQL como la aplicación Spring Boot automáticamente, sin necesidad de instalar nada más.

**Requisitos previos:**
- Tener Docker y Docker Compose instalados en tu máquina.

**Pasos:**

1. Clona el repositorio y navega a la raíz del proyecto.

2. Ejecuta (Windows PowerShell):

```powershell
# Construir las imágenes y levantar los contenedores
docker-compose up --build

# O en modo background (segundo plano)
docker-compose up -d --build
```

3. Verifica que todo esté corriendo:

```powershell
docker-compose ps
```

Deberías ver dos contenedores: `mysql_db` y `spring_app`.

4. La aplicación estará disponible en: http://localhost:8080
5. La base de datos estará disponible en: `localhost:3306` (usuario: `user`, contraseña: `password`)

**Para detener los contenedores:**

```powershell
docker-compose down

# Si además quieres eliminar volúmenes (borrar datos de MySQL)
docker-compose down -v
```

**Ver logs de la aplicación:**

```powershell
# Logs de la app Spring Boot
docker-compose logs app

# Logs de MySQL
docker-compose logs db

# Logs en tiempo real
docker-compose logs -f app
```

## Autenticación / obtener token

El proyecto utiliza `UsernamePasswordAuthenticationFilter` personalizado (`JwtAuthenticationFilter`). Por defecto, este filtro procesa peticiones de login en la ruta `/login` (POST). Se envía un body JSON con `username` y `password`.

Ejemplo (Linux/macOS con curl):

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' -i
```

Ejemplo (PowerShell):

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/login" -ContentType 'application/json' -Body (@{username='admin'; password='admin'} | ConvertTo-Json)
```

Respuesta esperada: JSON con campos `token` y `username`, y además la cabecera `Authorization: Bearer <token>`.

Una vez tengas el token, úsalo así para acceder a endpoints protegidos (ej.: obtener productos):

curl (Linux/macOS):

```bash
curl -H "Authorization: Bearer <TOKEN>" http://localhost:8080/api/products
```

PowerShell:

```powershell
$headers = @{ Authorization = "Bearer <TOKEN>" }
Invoke-RestMethod -Uri "http://localhost:8080/api/products" -Headers $headers
```

## Nota sobre creación de usuarios/roles (ADMIN)

### Creación automática de datos iniciales con DataInitializer

El proyecto incluye una clase `DataInitializer` (`src/main/java/com/yoel/springboot/app/springboot_crud/config/DataInitializer.java`) que se ejecuta automáticamente al arrancar la aplicación. Esta clase:

1. **Crea los roles** `ROLE_ADMIN` y `ROLE_USER` si no existen.
2. **Crea un usuario ADMIN** de prueba con credenciales:
   - **Username**: `admin`
   - **Password**: `admin123`

De esta forma, **no necesitas insertar datos manualmente en la base de datos**. Al arrancar la app (ya sea localmente o con Docker Compose), los roles y usuario ADMIN se crean automáticamente.

```java
@Configuration
public class DataInitializer {
    // Crea automáticamente:
    // - Rol ROLE_ADMIN
    // - Rol ROLE_USER
    // - Usuario admin (password: admin123)
}
```

**Para usar el usuario de prueba:**

```bash
# Login con curl
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' -i
```

### Alternativa: Crear usuarios/roles manualmente

Si prefieres crear usuarios adicionales de forma manual:

- Insertar registros directamente en las tablas `users`, `roles` y `user_roles`.
- La contraseña debe estar codificada con BCrypt: `new BCryptPasswordEncoder().encode("tuPassword")`.

O utilizar los endpoints de creación (requieren autenticación y rol ADMIN).



## Build y empaquetado

### Con Maven (local)

Empaqueta un JAR ejecutable con:

```powershell
.\mvnw.cmd clean package
```

Ejecuta el JAR:

```powershell
java -jar target\springboot-crud-0.0.1-SNAPSHOT.jar
```

### Con Docker

**Construir la imagen Docker:**

```powershell
# Desde la raíz del proyecto
docker build -t spring-security-app:latest .
```

**Ejecutar el contenedor Docker (sin Docker Compose):**

```powershell
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/springboot_crud \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=123456 \
  spring-security-app:latest
```



## Tests

Ejecuta las pruebas unitarias con:

```powershell
.\mvnw.cmd test
```

## Detalles de Docker Compose

### ¿Qué hace el docker-compose.yml?

El archivo `docker-compose.yml` define dos servicios:

#### 1. **Servicio de Base de Datos (`db`)**
- **Imagen**: MySQL 8.0
- **Container name**: `mysql_db`
- **Puertos**: 3306:3306
- **Variables de entorno**:
  - `MYSQL_ROOT_PASSWORD`: `rootpassword`
  - `MYSQL_DATABASE`: `springboot_crud`
  - `MYSQL_USER`: `user`
  - `MYSQL_PASSWORD`: `password`
- **Volumen**: `mysql_data:/var/lib/mysql` (persiste datos aunque se detenga el contenedor)
- **Health Check**: Verifica que MySQL esté listo antes de que la app intente conectar

#### 2. **Servicio de Aplicación (`app`)**
- **Build**: Construye automáticamente la imagen usando el `Dockerfile`
- **Imagen**: `spring-security-app:latest`
- **Container name**: `spring_app`
- **Puertos**: 8080:8080
- **Dependencia**: Espera a que `db` esté saludable antes de iniciar
- **Variables de entorno**:
  - `SPRING_DATASOURCE_URL`: Usa el nombre del servicio `db` para conectar a MySQL
  - `SPRING_DATASOURCE_PASSWORD`: `password`
  - Otras propiedades de Spring Boot (JPA dialect, etc.)

### Flujo de ejecución con docker-compose

```
1. docker-compose up --build
   ↓
2. Construye la imagen Docker de la app (desde Dockerfile)
   ↓
3. Inicia MySQL (mysql_db)
   ↓
4. Espera a que MySQL sea accesible (health check)
   ↓
5. Inicia la app Spring Boot (spring_app)
   ↓
6. DataInitializer crea roles y usuario ADMIN
   ↓
7. API disponible en http://localhost:8080
```

## Notas de seguridad y mejoras recomendadas para portfolio

- Actualmente la configuración requiere autenticación para todas las rutas (`.anyRequest().authenticated()`), y aplica roles ADMIN en rutas específicas.
- `TokenJwtConfig` usa una llave secreta construida dentro del código; para producción deberías cargar la clave desde variables de entorno/secret manager y rotarla periódicamente.
- Añadir validación de CORS si se va a consumir la API desde un frontend separado.
- Agregar Swagger/OpenAPI para documentar endpoints y facilitar pruebas en el portfolio.
- Añadir tests de integración que validen flujos de login y endpoints protegidos.
- Considerar usar perfiles (`application-dev.properties`, `application-prod.properties`) para separar configuraciones.

## Ejemplo completo: Login y CRUD con Docker Compose

Una vez que ejecutaste `docker-compose up --build` y la app está corriendo:

### 1. Obtener token (login con usuario ADMIN creado automáticamente)

```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' \
  | jq .token
```

Copia el valor del `token`.

### 2. Obtener lista de productos

```bash
curl -H "Authorization: Bearer <TU_TOKEN>" \
  http://localhost:8080/api/products
```

### 3. Crear un nuevo producto (requiere rol ADMIN)

```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer <TU_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop","price":1500,"description":"High performance laptop"}'
```

### 4. Actualizar un producto

```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <TU_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop Pro","price":1800,"description":"Updated laptop"}'
```

### 5. Eliminar un producto

```bash
curl -X DELETE http://localhost:8080/api/products/1 \
  -H "Authorization: Bearer <TU_TOKEN>"
```

## Sugerencias para el README del portfolio

- Añadir capturas de pantalla de Postman mostrando el login y la llamada a `/api/products` con token.
- Incluir un pequeño diagrama (arquitectura) mostrando: cliente -> Spring Boot (filtros JWT) -> MySQL.
- Añadir links a commits o PRs que muestren trabajo iterativo.



