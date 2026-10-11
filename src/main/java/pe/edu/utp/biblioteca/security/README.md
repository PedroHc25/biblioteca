# SECURITY - AVANCE 2

Esta carpeta contiene la configuración de seguridad de la aplicación.

RESPONSABLE:
- Pedro

TAREAS:
1. Configurar Spring Security.
2. Configurar autenticación.
3. Implementar el inicio de sesión.
4. Obtener los usuarios desde la base de datos.
5. Generar tokens JWT.
6. Validar los tokens JWT.
7. Proteger los endpoints de la API.
8. Realizar pruebas de autenticación.
9. Verificar acceso permitido y acceso rechazado.

FLUJO:

Usuario
    ↓
Login
    ↓
Spring Security
    ↓
JWT
    ↓
Token válido
    ↓
Endpoint protegido

IMPORTANTE:
La seguridad se implementará después de tener
funcionando la conexión con la base de datos y
los usuarios almacenados mediante JPA.