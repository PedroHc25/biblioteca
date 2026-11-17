package pe.edu.utp.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pe.edu.utp.biblioteca.model.Autor;
import pe.edu.utp.biblioteca.repository.AutorRepository;

@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    public Optional<Autor> buscarPorId(Integer id) {
        return autorRepository.findById(id);
    }

    public Autor guardar(Autor autor) {
        validarAutor(autor);

        autor.setId(null);
        return autorRepository.save(autor);
    }

    public Autor actualizar(Integer id, Autor datos) {
        Autor existente = autorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Autor no encontrado."
                ));

        validarAutor(datos);

        existente.setNombreCompleto(datos.getNombreCompleto());
        existente.setNacionalidad(datos.getNacionalidad());
        existente.setEstado(datos.getEstado());

        return autorRepository.save(existente);
    }

    private void validarAutor(Autor autor) {
        if (autor.getNombreCompleto() == null
                || autor.getNombreCompleto().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre completo es obligatorio."
            );
        }

        if (autor.getEstado() == null
                || autor.getEstado().isBlank()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El estado es obligatorio."
            );
        }
    }
}