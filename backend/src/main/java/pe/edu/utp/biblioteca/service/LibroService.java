package pe.edu.utp.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.utp.biblioteca.dto.LibroRequest;
import pe.edu.utp.biblioteca.model.Autor;
import pe.edu.utp.biblioteca.model.Libro;
import pe.edu.utp.biblioteca.repository.AutorRepository;
import pe.edu.utp.biblioteca.repository.LibroRepository;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public LibroService(
            LibroRepository libroRepository,
            AutorRepository autorRepository) {

        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    public Optional<Libro> buscarPorId(Integer id) {
        return libroRepository.findById(id);
    }

    public Libro guardar(LibroRequest datos) {
        Libro libro = new Libro();

        copiarDatos(datos, libro);

        return libroRepository.save(libro);
    }

    public Libro actualizar(Integer id, LibroRequest datos) {
        Libro existente = libroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Libro no encontrado."
                ));

        copiarDatos(datos, existente);

        return libroRepository.save(existente);
    }

    private void copiarDatos(
            LibroRequest datos,
            Libro libro) {

        validarDatos(datos);

        Autor autor = autorRepository.findById(datos.autorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El autor seleccionado no existe."
                ));

        libro.setTitulo(datos.titulo().trim());
        libro.setAutor(autor);
        libro.setCategoria(datos.categoria().trim());
        libro.setAnio(datos.anio());
        libro.setEstado(datos.estado());
    }

    private void validarDatos(LibroRequest datos) {
        if (datos.titulo() == null
                || datos.titulo().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El título es obligatorio."
            );
        }

        if (datos.autorId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes seleccionar un autor."
            );
        }

        if (datos.categoria() == null
                || datos.categoria().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La categoría es obligatoria."
            );
        }

        if (datos.anio() == null
                || datos.anio() < 0
                || datos.anio() > 2100) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El año debe estar entre 0 y 2100."
            );
        }

        if (datos.estado() == null
                || datos.estado().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El estado es obligatorio."
            );
        }
    }
}