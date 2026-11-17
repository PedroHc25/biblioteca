package pe.edu.utp.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.utp.biblioteca.model.Prestamo;

public interface PrestamoRepository extends JpaRepository<Prestamo, Integer> {
}