package pe.edu.utp.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.biblioteca.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Integer> {
}