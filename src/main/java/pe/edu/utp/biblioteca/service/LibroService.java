package pe.edu.utp.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pe.edu.utp.biblioteca.model.Libro;
import pe.edu.utp.biblioteca.repository.LibroRepository;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    public Optional<Libro> buscarPorId(Integer id) {
        return libroRepository.findById(id);
    }

    public Libro guardar(Libro libro) {
        libro.setId(null);
        return libroRepository.save(libro);
    }

    public Libro actualizar(Integer id, Libro libro) {
        libro.setId(id);
        return libroRepository.save(libro);
    }

    public boolean eliminar(Integer id) {
        if (libroRepository.existsById(id)) {
            libroRepository.deleteById(id);
            return true;
        }
        return false;
    }
}