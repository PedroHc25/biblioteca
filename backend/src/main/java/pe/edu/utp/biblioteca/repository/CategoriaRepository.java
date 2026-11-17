package pe.edu.utp.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.biblioteca.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}