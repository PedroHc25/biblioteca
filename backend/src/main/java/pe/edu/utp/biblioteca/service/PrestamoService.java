package pe.edu.utp.biblioteca.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.utp.biblioteca.model.Libro;
import pe.edu.utp.biblioteca.model.Prestamo;
import pe.edu.utp.biblioteca.repository.LibroRepository;
import pe.edu.utp.biblioteca.repository.PrestamoRepository;
import pe.edu.utp.biblioteca.repository.UsuarioRepository;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;

    public PrestamoService(
            PrestamoRepository prestamoRepository,
            LibroRepository libroRepository,
            UsuarioRepository usuarioRepository) {

        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Prestamo> listarTodos() {
        return prestamoRepository.findAll();
    }

    public Optional<Prestamo> buscarPorId(Integer id) {
        return prestamoRepository.findById(id);
    }

    @Transactional
    public Prestamo guardar(Prestamo prestamo) {
        validarFechas(prestamo);
        validarUsuario(prestamo.getUsuarioId());

        Libro libro = obtenerLibro(prestamo.getLibroId());

        if (!"Disponible".equalsIgnoreCase(libro.getEstado())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El libro seleccionado no está disponible."
            );
        }

        prestamo.setId(null);
        prestamo.setEstado("Pendiente");

        libro.setEstado("Prestado");
        libroRepository.save(libro);

        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo actualizar(Integer id, Prestamo datos) {
        Prestamo existente = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Préstamo no encontrado."
                ));

        validarFechas(datos);

        // No permitimos cambiar el usuario ni el libro
        // después de crear el préstamo.
        if (!existente.getUsuarioId().equals(datos.getUsuarioId())
                || !existente.getLibroId().equals(datos.getLibroId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede cambiar el usuario o libro de un préstamo existente."
            );
        }

        String estado = datos.getEstado();

        if (!"Pendiente".equalsIgnoreCase(estado)
                && !"Devuelto".equalsIgnoreCase(estado)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El estado debe ser Pendiente o Devuelto."
            );
        }

        existente.setFechaPrestamo(datos.getFechaPrestamo());
        existente.setFechaDevolucion(datos.getFechaDevolucion());

        if ("Devuelto".equalsIgnoreCase(estado)) {
            existente.setEstado("Devuelto");
        } else {
            existente.setEstado("Pendiente");
        }

        Libro libro = obtenerLibro(existente.getLibroId());

        if ("Devuelto".equalsIgnoreCase(existente.getEstado())) {
            libro.setEstado("Disponible");
        } else {
            libro.setEstado("Prestado");
        }

        libroRepository.save(libro);

        return prestamoRepository.save(existente);
    }

    private void validarUsuario(Integer usuarioId) {
        if (usuarioId == null
                || !usuarioRepository.existsById(usuarioId)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El usuario seleccionado no existe."
            );
        }
    }

    private Libro obtenerLibro(Integer libroId) {
        if (libroId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debes seleccionar un libro."
            );
        }

        return libroRepository.findById(libroId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El libro seleccionado no existe."
                ));
    }

    private void validarFechas(Prestamo prestamo) {
        if (prestamo.getFechaPrestamo() == null
                || prestamo.getFechaPrestamo().isBlank()
                || prestamo.getFechaDevolucion() == null
                || prestamo.getFechaDevolucion().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Las fechas son obligatorias."
            );
        }

        try {
            LocalDate fechaPrestamo =
                    LocalDate.parse(prestamo.getFechaPrestamo());

            LocalDate fechaDevolucion =
                    LocalDate.parse(prestamo.getFechaDevolucion());

            if (fechaDevolucion.isBefore(fechaPrestamo)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La fecha de devolución no puede ser anterior a la fecha de préstamo."
                );
            }
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Las fechas deben tener formato YYYY-MM-DD."
            );
        }
    }
}