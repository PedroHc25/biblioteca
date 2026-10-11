# SERVICE - AVANCE 2

Esta carpeta contiene la capa de servicios del Sistema de Gestión
de Biblioteca.

OBJETIVO:

La capa Service contiene la lógica de negocio y comunica los
Controllers con los Repositories.

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


TAREAS DE CADA INTEGRANTE:

1. Crear el Service correspondiente a su modelo.

2. Utilizar inyección de dependencias para acceder al Repository.

3. Implementar las operaciones necesarias para el CRUD.

4. Implementar las reglas de negocio correspondientes al módulo.

5. Manejar correctamente los casos en los que un registro exista
   o no exista.

6. Coordinar con el Controller para retornar las respuestas HTTP
   correspondientes.

7. Realizar pruebas antes de enviar el Pull Request.


OPERACIONES ESPERADAS:

LIBRO
- Listar libros
- Buscar libro por ID
- Registrar libro
- Actualizar libro
- Eliminar libro

USUARIO
- Listar usuarios
- Buscar usuario por ID
- Registrar usuario
- Actualizar usuario
- Eliminar usuario

PRESTAMO
- Listar préstamos
- Buscar préstamo por ID
- Registrar préstamo
- Actualizar préstamo
- Eliminar préstamo

CATEGORIA
- Listar categorías
- Buscar categoría por ID
- Registrar categoría
- Actualizar categoría
- Eliminar categoría


IMPORTANTE:

- No colocar lógica de negocio directamente en Controller.
- No acceder directamente a la base de datos desde Controller.
- El Service debe utilizar el Repository correspondiente.
- No modificar el trabajo de otro integrante.
- Cada integrante trabaja en su propia rama.
- Antes del Pull Request, ejecutar y verificar las pruebas.


ESTRUCTURA:

Controller
    ↓
Service
    ↓
Repository
    ↓
Base de Datos