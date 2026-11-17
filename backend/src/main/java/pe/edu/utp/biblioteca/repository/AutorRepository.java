package pe.edu.utp.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.utp.biblioteca.model.Autor;

public interface AutorRepository
        extends JpaRepository<Autor, Integer> {
}