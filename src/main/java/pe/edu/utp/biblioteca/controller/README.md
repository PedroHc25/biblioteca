# CONTROLLER - AVANCE 2

Esta carpeta contiene los controladores REST de la aplicación.

CONTROLLERS ACTUALES:

- LibroController.java → 
- UsuarioController.java → 
- PrestamoController.java →
- CategoriaController.java → (crear)

TAREAS:

1. Mantener los Controllers existentes del primer avance.
2. Adaptarlos para utilizar la capa Service.
3. Eliminar progresivamente el acceso directo a listas o mapas en memoria.
4. Utilizar el Service correspondiente.
5. Mantener las rutas REST definidas en el proyecto.
6. Implementar los métodos HTTP necesarios.
7. Retornar códigos HTTP adecuados.
8. Probar cada endpoint.

FLUJO:

Solicitud HTTP
      ↓
Controller
      ↓
Service
      ↓
Repository
      ↓
JPA / Hibernate
      ↓
MySQL

IMPORTANTE:

No eliminar los Controllers existentes sin necesidad.
La adaptación se realizará sobre el código del primer avance.
No acceder directamente al Repository desde el Controller.