# MODEL - AVANCE 2

Esta carpeta contiene los modelos principales del Sistema de Gestión
de Biblioteca.

MODELOS ACTUALES:

- Libro.java → 
- Usuario.java →
- Prestamo.java → 
- Categoria.java →


OBJETIVO DEL AVANCE 2:

Los modelos existentes del primer avance deberán adaptarse para
trabajar con Spring Data JPA, Hibernate y MySQL.


TAREAS:

1. Mantener los modelos existentes del primer avance.

2. Convertir cada modelo en una entidad JPA utilizando @Entity.

3. Definir correctamente la clave primaria utilizando @Id.

4. Configurar la generación del ID cuando corresponda.

5. Mantener los atributos necesarios del modelo.

6. Cada objeto debe tener como mínimo 4 atributos.

7. Utilizar las anotaciones JPA necesarias.

8. Configurar las relaciones entre entidades cuando corresponda.

9. Verificar que las entidades puedan ser almacenadas correctamente
   en la base de datos MySQL.



IMPORTANTE:

- No crear otro modelo si ya existe uno.
- No eliminar los atributos existentes sin justificación.
- No colocar lógica de negocio dentro del modelo.
- Cada integrante trabaja únicamente en su propia rama.
- Los cambios deben probarse antes de realizar el Pull Request
  hacia develop.


FLUJO:

Modelo
   ↓
JPA / Hibernate
   ↓
MySQL