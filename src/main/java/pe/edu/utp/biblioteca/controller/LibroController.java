package pe.edu.utp.biblioteca.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.utp.biblioteca.model.Libro;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    @PostMapping
    public Libro crearLibro(@RequestBody Libro libro) {
        return libro;
    }
}