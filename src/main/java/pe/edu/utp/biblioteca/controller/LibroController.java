package pe.edu.utp.biblioteca.controller;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.utp.biblioteca.model.Libro;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

  private final Map<Integer, Libro> libros = new ConcurrentHashMap<>();

  @GetMapping
  public Collection<Libro> obtenerLibros() {
    return libros.values();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Libro> obtenerLibroPorId(@PathVariable int id) {
    Libro libro = libros.get(id);
    if (libro == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(libro);
  }

  @PostMapping
  public Libro crearLibro(@RequestBody Libro libro) {
    libros.put(libro.getId(), libro);
    return libro;
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> eliminarLibro(@PathVariable int id) {
    Libro libroEliminado = libros.remove(id);

    if (libroEliminado == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.noContent().build();
  }
}
