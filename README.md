# 📚 BIBLIOTECA - Sistema de Gestión de Biblioteca (API REST)

Sistema de gestión de biblioteca desarrollado en **Java 21 + Spring Boot** que expone
una **API REST** para administrar **usuarios, libros, categorías y préstamos**, con
**autenticación mediante tokens JWT** y persistencia en **MySQL** a través de JPA/Hibernate.

Este README explica el proyecto **paso a paso**: qué tecnologías usa, cómo está
estructurado, cómo se ejecuta, cómo funciona cada capa y cómo se validan los
requisitos del avance evaluado.

---

## 📑 Índice

1. [Tecnologías utilizadas](#-tecnologías-utilizadas)
2. [Estructura del proyecto](#-estructura-del-proyecto)
3. [Requisitos previos](#-requisitos-previos)
4. [Configuración](#-configuración)
5. [Cómo ejecutar la aplicación](#-cómo-ejecutar-la-aplicación)
6. [Cómo ejecutar las pruebas](#-cómo-ejecutar-las-pruebas)
7. [Flujo de una petición paso a paso](#-flujo-de-una-petición-paso-a-paso)
8. [Explicación de cada capa](#-explicación-de-cada-capa)
9. [Endpoints de la API (tabla completa)](#-endpoints-de-la-api-tabla-completa)
10. [Autenticación con JWT](#-autenticación-con-jwt)
11. [Pruebas automatizadas](#-pruebas-automatizadas)
12. [Cumplimiento de la rúbrica del avance](#-cumplimiento-de-la-rúbrica-del-avance)
13. [Notas y puntos a pulir](#-notas-y-puntos-a-pulir)

---

## 🛠 Tecnologías utilizadas

| Tecnología | Versión | Para qué se usa |
|---|---|---|
| Java | 21 (toolchain) | Lenguaje base del proyecto |
| Spring Boot | 4.1.1 | Framework principal (arranque, autoconfiguración) |
| Spring Web MVC | 4.1.1 | Construcción de la API REST (controladores) |
| Spring Data JPA / Hibernate | 7.4.5 | Acceso a base de datos con entidades |
| MySQL | 8.0.x | Base de datos relacional |
| Spring Security | (starter) | Seguridad de la aplicación |
| JWT (jjwt) | 0.12.6 | Generación/validación de tokens |
| JUnit 5 (+ MockMvc) | (starter test) | Pruebas automatizadas |
| Gradle | 9.7.1 | Herramienta de compilación y dependencias |

---

## 🗂 Estructura del proyecto

```
biblioteca/
├── build.gradle.kts                  # Dependencias y configuración de Gradle
├── settings.gradle.kts               # Nombre del proyecto
├── iniciar.bat                       # Script para levantar la app (define variables)
├── gradlew.bat                       # Lanzador de Gradle (Windows)
│
├── src/main/java/pe/edu/utp/biblioteca/
│   ├── BibliotecaApplication.java      # Clase principal (@SpringBootApplication)
│   ├── controller/                     # Capa de presentación (endpoints REST)
│   │   ├── AuthController.java
│   │   ├── CategoriaController.java
│   │   ├── LibroController.java
│   │   ├── PrestamoController.java
│   │   └── UsuarioController.java
│   ├── dto/                            # Objetos de transferencia de datos
│   │   ├── LoginRequest.java
│   │   └── UsuarioResponse.java
│   ├── model/                          # Entidades JPA (tablas)
│   │   ├── Categoria.java
│   │   ├── Libro.java
│   │   ├── Prestamo.java
│   │   └── Usuario.java
│   ├── repository/                     # Interfaces Spring Data JPA
│   │   ├── CategoriaRepository.java
│   │   ├── LibroRepository.java
│   │   ├── PrestamoRepository.java
│   │   └── UsuarioRepository.java
│   ├── security/                       # Autenticación y tokens
│   │   ├── AuthService.java            # Lógica de login
│   │   ├── JwtService.java             # Generar/validar tokens JWT
│   │   ├── JwtAuthenticationFilter.java# Filtro que valida el token en cada petición
│   │   └── SecurityConfig.java         # Reglas de seguridad (Spring Security)
│   └── service/                        # Lógica de negocio (puente Controller→Repository)
│       ├── CategoriaService.java
│       ├── LibroService.java
│       ├── PrestamoService.java
│       └── UsuarioService.java
│
├── src/main/resources/
│   └── application.properties          # Configuración (puerto, BD, JWT)
│
└── src/test/
    ├── java/pe/edu/utp/biblioteca/
    │   ├── BibliotecaApplicationTests.java
    │   ├── controller/                 # Pruebas de los controladores REST
    │   │   ├── CategoriaControllerTest.java
    │   │   ├── LibroControllerTest.java
    │   │   ├── PrestamoControllerTest.java
    │   │   └── UsuarioControllerTest.java
    │   └── security/
    │       └── JwtServiceTest.java     # Pruebas del servicio JWT
    └── resources/
        └── application-test.properties # Configuración para el entorno de pruebas
```

---

## ✅ Requisitos previos

1. **JDK 21** instalado (el proyecto usa el toolchain de Java 21).
2. **MySQL Server 8.x** instalado y corriendo en el puerto `3306`.
3. Gradle (el proyecto incluye el wrapper `gradlew.bat`, no hace falta instalarlo aparte).

---

## ⚙️ Configuración

### 4.1 Bases de datos necesarias

La aplicación necesita **dos** bases de datos en MySQL:

| Base de datos | Para qué se usa |
|---|---|
| `biblioteca` | Base de datos principal de la aplicación |
| `biblioteca_test` | Base de datos que usan las pruebas automatizadas |

Se crean desde la línea de comandos de MySQL (o con MySQL Workbench):

```sql
CREATE DATABASE IF NOT EXISTS biblioteca;
CREATE DATABASE IF NOT EXISTS biblioteca_test;
```

### 4.2 Variables de entorno

La configuración lee secretos desde variables de entorno (ver `application.properties`):

| Variable | Valor de ejemplo | Para qué se usa |
|---|---|---|
| `DB_PASSWORD` | `1234` | Contraseña del usuario `root` de MySQL |
| `JWT_SECRET` | `vLNSyHIUGAJlb6TIcvv+cJ0WW0RiJLhoSJlhX1ITMW7sQ0WOrkRTQHC8OqzDK1wU` | Secreto con el que se firman los tokens JWT |

### 4.3 Archivo `application.properties` (entorno principal)

```properties
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.secret=${JWT_SECRET}
jwt.expiration=3600000
```

Puntos importantes:
- El puerto de la API es **8081**.
- `ddl-auto=update` hace que Hibernate **cree o actualice las tablas automáticamente** según las entidades JPA (al primer arranque se crean las tablas `usuarios`, `libros`, `categorias`, `prestamos`).
- `jwt.expiration=3600000` = el token JWT expira a la 1 hora.

### 4.4 Archivo `application-test.properties` (entorno de pruebas)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/biblioteca_test
spring.jpa.hibernate.ddl-auto=create-drop
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
jwt.secret=VGhpc0lzQU5vdFJlYWxTZWNyZXRLZXlGb3JUZXN0cw==
```

- Apunta a la base **`biblioteca_test`** y usa `create-drop`:
  las tablas se **crean al iniciar** cada prueba y **se eliminan al terminar**. Así cada
  test parte de una base limpia y no ensucia la base `biblioteca` real.

---

## ▶️ Cómo ejecutar la aplicación

### Opción A: con el script `iniciar.bat` (recomendado para la demo)

Doble clic sobre `iniciar.bat`. Este script:

1. Define las variables `DB_PASSWORD` y `JWT_SECRET`.
2. Ejecuta `gradlew.bat bootRun`.
3. Levanta la aplicación en `http://localhost:8081`.

Para detenerla: `Ctrl + C` en la ventana del script.

### Opción B: manual, desde la consola

```powershell
cd c:\biblioteca
$env:DB_PASSWORD="1234"
$env:JWT_SECRET="vLNSyHIUGAJlb6TIcvv+cJ0WW0RiJLhoSJlhX1ITMW7sQ0WOrkRTQHC8OqzDK1wU"
.\gradlew.bat bootRun
```

Ejemplo de salida esperada al arrancar correctamente:

```
Tomcat started on port 8081 (http) with context path '/'
Started BibliotecaApplication in 14.5 seconds
```

---

## ▶️ Cómo ejecutar las pruebas

```powershell
cd c:\biblioteca
$env:DB_PASSWORD="1234"
.\gradlew.bat test
```

Resultado esperado (verificado en el proyecto):

```
TOTAL DE TESTS: 42
TESTS FALLIDOS: 0
TESTS EXITOSOS: 42
BUILD SUCCESSFUL
```

> ⚠️ Las pruebas necesitan: MySQL corriendo, que exista la base `biblioteca_test`
> y que la variable `DB_PASSWORD` esté definida con la contraseña de MySQL.

---

## 🔁 Flujo de una petición paso a paso

Una petición HTTP recorre **exactamente** este camino dentro del proyecto:

```
Solicitud HTTP (cliente: Postman, curl, navegador...)
        ↓
   1. JwtAuthenticationFilter   → ¿Trae "Authorization: Bearer <token>"?
        ↓                        → si el token es válido, autentica; si no, la petición queda sin autenticar
   2. Controller                → recibe la petición y define la ruta/código HTTP
        ↓
   3. Service                   → aplica la lógica de negocio (ej: encriptar contraseña, validar existencia)
        ↓
   4. Repository (JPA)          → interfaz Spring Data JPA (consultas automáticas)
        ↓
   5. Hibernate / JDBC          → traduce la operación a SQL
        ↓
   6. MySQL                     → ejecuta la consulta en la tabla correspondiente
        ↓
   Respuesta JSON               → vuelve por la misma ruta hacia el cliente
```

### Ejemplo concreto: "Registrar un usuario"

1. El cliente hace `POST /api/usuarios` con el JSON del nuevo usuario.
2. El `JwtAuthenticationFilter` deja pasar la petición porque `/api/usuarios` (POST) está en `permitAll`.
3. El `UsuarioController.crearUsuario()` recibe el `Usuario`, fuerza `rol = "USER"` y llama a `UsuarioService.guardar(...)`.
4. El `UsuarioService` **encripta la contraseña con BCrypt** y guarda con `UsuarioRepository.save(...)`.
5. Hibernate genera el `INSERT INTO usuarios (...)` y lo ejecuta en MySQL.
6. El controlador responde `201 Created` con el `UsuarioResponse` (sin el campo `password`).

---

## 📦 Explicación de cada capa

### 8.1 Capa Model (entidades JPA) - `model/`

Define las clases que representan las **tablas de la base de datos** usando anotaciones de
`jakarta.persistence`. Todas tienen **4 o más atributos**, como exige el avance.

**`Usuario`** → tabla `usuarios` (8 atributos)

| Atributo | Tipo | Regla |
|---|---|---|
| `id` | Integer | `@Id`, autogenerado (`IDENTITY`) |
| `nombre` | String | - |
| `apellido` | String | - |
| `dni` | String | `unique`, `not null` |
| `correo` | String | `unique`, `not null` |
| `telefono` | String | - |
| `password` | String | `not null` (se guarda encriptado con BCrypt) |
| `rol` | String | `not null` (valores usados: `ADMIN`, `USER`) |

**`Libro`** → tabla `libros` (6 atributos)

| Atributo | Tipo | Regla |
|---|---|---|
| `id` | Integer | `@Id`, autogenerado |
| `titulo` | String | `not null` |
| `autor` | String | `not null` |
| `categoria` | String | - |
| `anio` | Integer | - |
| `estado` | String | - (ej: "Disponible", "Prestado") |

**`Categoria`** → tabla `categorias` (5 atributos)

| Atributo | Tipo | Regla |
|---|---|---|
| `id` | Integer | `@Id`, autogenerado |
| `nombre` | String | `not null` |
| `descripcion` | String | - |
| `tipo` | String | - |
| `estado` | String | - (ej: "ACTIVO") |

**`Prestamo`** → tabla `prestamos` (6 atributos)

| Atributo | Tipo | Regla |
|---|---|---|
| `id` | Integer | `@Id`, autogenerado |
| `fechaPrestamo` | String | columna `fecha_prestamo` |
| `fechaDevolucion` | String | columna `fecha_devolucion` |
| `estado` | String | - (ej: "ACTIVO", "DEVUELTO") |
| `usuarioId` | Integer | columna `usuario_id` (referencia a usuarios) |
| `libroId` | Integer | columna `libro_id` (referencia a libros) |

Cada entidad tiene: constructor vacío, constructor completo y getters/setters.

### 8.2 Capa Repository - `repository/`

Interfaces de **Spring Data JPA** que entregan los métodos CRUD automáticos sin escribir SQL:

```java
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);   // método derivado del nombre
}
```

- `findAll()`, `findById(id)`, `save(...)`, `deleteById(id)`, `existsById(id)` → ya vienen de `JpaRepository`.
- `findByCorreo(...)` lo implementa Spring Data automáticamente a partir del nombre del método (genera `WHERE correo = ?`). Lo usa el login de seguridad.

### 8.3 Capa Service - `service/`

Contiene la **lógica de negocio** y comunica los Controllers con los Repositories.
Reglas implementadas:

- `UsuarioService.guardar(...)`: **encripta la contraseña con BCrypt** antes de guardar.
- Los métodos `guardar(...)` fuerzan `id = null` para que MySQL genere el id automáticamente.
- Los métodos `eliminar(id)` verifican con `existsById` si el registro existe y devuelven `boolean` (para responder 204 o 404).

### 8.4 Capa Controller (API REST) - `controller/`

Expone los **endpoints HTTP**. Reglas comunes:

- `@RestController` + `@RequestMapping("/api/...")`.
- Responden códigos HTTP adecuados:
  - `200 OK` → listar / obtener / actualizar con éxito.
  - `201 Created` → crear con éxito.
  - `204 No Content` → eliminar con éxito.
  - `404 Not Found` → el recurso no existe.
- `CategoriaController`, `LibroController` y `PrestamoController` implementan **CRUD completo** (GET, GET/{id}, POST, PUT/{id}, DELETE/{id}).
- `UsuarioController` implementa **GET (listar) y POST (registrar)**.
- `AuthController` implementa el **login** que entrega el token JWT.
- `UsuarioController` NO expone la contraseña: convierte a `UsuarioResponse` (DTO) antes de responder.

### 8.5 Capa DTO - `dto/`

Objetos de transferencia para no exponer datos sensibles o innecesarios:

- `LoginRequest` → `record` con `correo` y `password` (entrada del login).
- `UsuarioResponse` → `record` con los datos del usuario **sin la contraseña**.

### 8.6 Capa Security - `security/`

Los 4 actores de la seguridad:

1. **`SecurityConfig`** - Define las reglas:
   - CSRF desactivado (API stateless).
   - Sesiones `STATELESS` (no se guarda sesión del lado del servidor).
   - `permitAll()` → `POST /api/auth/login` (que cualquiera pueda loguearse) y `POST /api/usuarios` (que cualquiera pueda registrarse).
   - `anyRequest().authenticated()` → **todo lo demás exige token válido**.
   - Registra `JwtAuthenticationFilter` **antes** del filtro de autenticación de Spring.
   - Define `BCryptPasswordEncoder` como `PasswordEncoder`.

2. **`JwtService`** - Genera y valida tokens:
   - `generarToken(correo)` → crea el token firmado con el secreto, con `subject = correo`, `issuedAt` y `expiration` (1 hora).
   - `extraerCorreo(token)` → lee el `subject` del token.
   - `validarToken(token, correo)` → verifica firma, vigencia y que el correo coincida.

3. **`JwtAuthenticationFilter`** - Se ejecuta en CADA petición:
   - Lee el header `Authorization`.
   - Si empieza con `Bearer `, extrae el token y lo valida.
   - Si es válido, carga al usuario autenticado en el `SecurityContext` (Spring ya considera la petición autenticada).
   - Si el token es inválido/vencido, limpia el contexto → la petición queda sin autenticar → acceso denegado.

4. **`AuthService`** - Lógica del login:
   - Busca al usuario por correo en la BD (`findByCorreo`).
   - Verifica la contraseña con `passwordEncoder.matches(...)` (BCrypt).
   - Si todo es correcto, genera el token con `JwtService`.
   - Si el correo no existe o la contraseña no coincide → lanza excepción → el controlador responde `401 Unauthorized`.

---

## 🧭 Endpoints de la API (tabla completa)

### Autenticación

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| POST | `/api/auth/login` | Público | Inicia sesión y devuelve el token JWT |

### Categorías

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/categorias` | Token | Lista todas las categorías |
| GET | `/api/categorias/{id}` | Token | Obtiene una categoría por id |
| POST | `/api/categorias` | Token | Crea una categoría (devuelve `201`) |
| PUT | `/api/categorias/{id}` | Token | Actualiza una categoría |
| DELETE | `/api/categorias/{id}` | Token | Elimina una categoría (devuelve `204`) |

### Libros

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/libros` | Token | Lista todos los libros |
| GET | `/api/libros/{id}` | Token | Obtiene un libro por id |
| POST | `/api/libros` | Token | Crea un libro (devuelve `201`) |
| PUT | `/api/libros/{id}` | Token | Actualiza un libro |
| DELETE | `/api/libros/{id}` | Token | Elimina un libro (devuelve `204`) |

### Préstamos

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/prestamos` | Token | Lista todos los préstamos |
| GET | `/api/prestamos/{id}` | Token | Obtiene un préstamo por id |
| POST | `/api/prestamos` | Token | Registra un préstamo (devuelve `201`) |
| PUT | `/api/prestamos/{id}` | Token | Actualiza un préstamo (ej: cambiar estado a `DEVUELTO`) |
| DELETE | `/api/prestamos/{id}` | Token | Elimina un préstamo (devuelve `204`) |

### Usuarios

| Método | Ruta | Acceso | Descripción |
|---|---|---|---|
| GET | `/api/usuarios` | Token | Lista todos los usuarios (sin contraseña) |
| POST | `/api/usuarios` | Público | Registra un usuario (`rol` se fuerza a `USER`, contraseña se encripta) |

**Total: 18 operaciones REST**, distribuidas en **5 controladores**.

---

## 🔐 Autenticación con JWT

### Paso 1 - Registrar un usuario (público)

```http
POST http://localhost:8081/api/usuarios
Content-Type: application/json

{
  "nombre": "Pedro",
  "apellido": "Huayanay",
  "dni": "11223344",
  "correo": "pedro@gmail.com",
  "telefono": "988776655",
  "password": "Admin123"
}
```

Respuesta `201 Created`:

```json
{
  "id": 1,
  "nombre": "Pedro",
  "apellido": "Huayanay",
  "dni": "11223344",
  "correo": "pedro@gmail.com",
  "telefono": "988776655",
  "rol": "USER"
}
```

> Nota: la respuesta **no incluye `password`** (se usa el DTO `UsuarioResponse`).

### Paso 2 - Iniciar sesión (login)

```http
POST http://localhost:8081/api/auth/login
Content-Type: application/json

{
  "correo": "pedro@gmail.com",
  "password": "Admin123"
}
```

Respuesta `200 OK`:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwZWRyb0BnbWFpbC5jb20iLCJpYXQiOjE3Mjg..."
}
```

### Paso 3 - Usar el token en los endpoints protegidos

Todas las rutas que NO son login ni registro exigen el header:

```
Authorization: Bearer <token>
```

Ejemplo en **Postman**:

```
GET /api/libros
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

Comportamiento verificado de la seguridad:

| Situación | Resultado |
|---|---|
| Petición **sin** token | `403 Forbidden` |
| Petición con token **inválido** | `403 Forbidden` |
| Petición con token **válido** | `200 OK` |
| Login con credenciales **incorrectas** | `401 Unauthorized` |

---

## 🧪 Pruebas automatizadas

### ¿Dónde están?

- `src/test/java/.../controller/` → pruebas de los controladores REST con **MockMvc** (simulan peticiones HTTP reales sin levantar el servidor).
- `src/test/java/.../security/JwtServiceTest.java` → pruebas unitarias del servicio JWT.
- `src/test/resources/application-test.properties` → configuración del entorno de pruebas (BD `biblioteca_test`, tablas `create-drop`).

### ¿Cómo funcionan?

Cada clase usa `@SpringBootTest` + `@AutoConfigureMockMvc` + `@ActiveProfiles("test")` + `@DirtiesContext`.
Cada caso de prueba tiene la estructura pedida por el avance:

> **Entrada** (JSON enviado) → **Salida esperada** (código HTTP y campos JSON) → **Verificación** (`jsonPath`)

Ejemplo real (`post_01_crearLibroConTodosLosCampos_deberiaRetornar201`):

1. Envía `POST /api/libros` con el JSON del libro.
2. Espera `201 Created`.
3. Verifica `$.id` sea número, `$.titulo = "Cien años de soledad"`, `$.autor = ...`, etc.

### Inventario total de pruebas (42 en total, todas en verde)

| Clase de prueba | Cantidad | Servicios cubiertos |
|---|---|---|
| `LibroControllerTest` | 13 | POST(3), GET(5), PUT(2), DELETE(3) de `/api/libros` |
| `CategoriaControllerTest` | 8 | POST(1), GET(4), PUT(1), DELETE(2) de `/api/categorias` |
| `PrestamoControllerTest` | 10 | POST(2), GET(4), PUT(2), DELETE(2) de `/api/prestamos` |
| `UsuarioControllerTest` | 6 | POST(3), GET(3) de `/api/usuarios` |
| `JwtServiceTest` | 4 | Generar, extraer y validar tokens JWT |
| `BibliotecaApplicationTests` | 1 | Carga del contexto de Spring |
| **TOTAL** | **42** | **(0 fallos)** |

### Cómo ver el reporte

Después de ejecutar `gradlew.bat test`, el detalle de cada prueba queda en:

```
build/reports/tests/test/index.html   → reporte visual (abrir en el navegador)
build/test-results/test/*.xml         → resultados en formato XML
```

---

## 🎯 Cumplimiento de la rúbrica del avance

### Operaciones REST (8 puntos) — ✅ Cumplido

- ✅ **Más de 8 operaciones REST con acceso a base de datos**: el proyecto tiene **18**
  en **5 controladores** (se piden al menos 8 en al menos 2).
- ✅ **Operaciones en al menos dos controladores**: 5 controladores.
- ✅ **Utilidad para el proyecto**: CRUD de categorías, libros, préstamos y registro de usuarios; todas
  aplicables a un sistema de biblioteca real.
- ✅ **Transmiten al menos 4 objetos definidos en forma completa (≥4 atributos)**:
  `Categoria` (5), `Libro` (6), `Prestamo` (6) y `Usuario` (8).
- ✅ **Acceso a base de datos real**: flujo `Controller → Service → Repository → JPA/Hibernate → MySQL`.

### Seguridad (6 puntos) — ✅ Cumplido

- ✅ **Se usan tokens JWT**: `JwtService` genera, extrae y valida tokens firmados (jjwt 0.12.6).
- ✅ **Manejo de usuarios desde base de datos**: `UserService.guardar()` guarda con BCrypt;
  `AuthService.login()` busca el usuario en MySQL con `findByCorreo()` y valida la contraseña.
- ✅ **La aplicación emplea un mecanismo de seguridad**: Spring Security con cadena de filtros,
  sesiones stateless, contraseñas BCrypt y filtro JWT en cada petición.
- ✅ Verificado en ejecución real.

### Presentación (6 puntos) — ✅ Cumplido

- ✅ **Al menos 2 casos de prueba por cada servicio REST**: cada controlador supera los 2 casos
  (mínimo 6, máximo 13), con entradas, salidas esperadas y verificación.
- ✅ **Sin inconvenientes**: las 42 pruebas pasan (`BUILD SUCCESSFUL`) y la aplicación arranca
  y responde correctamente en `http://localhost:8081`.
- ✅ **Presentación clara**: este README documenta la arquitectura, cada endpoint, el flujo
  de una petición y los pasos exactos para reproducir la demo.

---

## 📝 Notas y puntos a pulir antes de la presentación

1. **Contraseña del usuario administrador**: existe `admin@biblioteca.com` en la BD (rol `ADMIN`),
   pero su contraseña NO está documentada en el proyecto. Para la demo, registrar un usuario nuevo
   con `POST /api/usuarios` (es público) y usar esa cuenta para iniciar sesión.
2. **Códigos de acceso**: al no enviar token, la API responde `403 Forbidden` (comportamiento actual
   de Spring Security). Si el evaluador espera `401 Unauthorized`, se puede configurar un
   `AuthenticationEntryPoint`. Es un detalle opcional, no bloqueante.
3. **Pruebas de login**: los tests actuales prueban los controladores CRUD y el servicio JWT.
   Aún no hay una clase de prueba para `POST /api/auth/login` ni un test que verifique el
   `403/401` de las rutas protegidas (se recomienda agregar `AuthControllerTest` y un
   `SecurityIntegrationTest` para reforzar la defensa de la parte de Seguridad).
4. **Variables de entorno**: para correr los tests se necesita `DB_PASSWORD` definida y la base
   `biblioteca_test` creada. Ver sección [Cómo ejecutar las pruebas](#-cómo-ejecutar-las-pruebas).

---

*Documento generado para el Avance del Sistema de Gestión de Biblioteca.*