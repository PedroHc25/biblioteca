# REPOSITORY - AVANCE 2

## Objetivo

Esta carpeta contiene los repositorios de acceso a la base de datos mediante Spring Data JPA.

## Responsables

- Repository de Libro
- Repository de Usuario
- Repository de Prestamo
- Repository de Categoria

## ¿Qué debe hacer cada integrante?

Crear el repositorio correspondiente a su modelo utilizando:

JpaRepository<Modelo, Integer>

Ejemplo:

public interface LibroRepository extends JpaRepository<Libro, Integer> {
}

## Orden de trabajo

Controller
    ↓
Service
    ↓
Repository
    ↓
Base de Datos

## Importante

- No colocar lógica de negocio en el Repository.
- Utilizar Spring Data JPA.
- Mantener el nombre de la clase relacionado con el modelo.
- Cada integrante debe trabajar en su propia rama.
- Los cambios terminados deben probarse antes de realizar el Pull Request hacia develop.