package pe.edu.utp.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.utp.biblioteca.model.Prestamo;
import pe.edu.utp.biblioteca.service.PrestamoService;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    // GET /api/prestamos
    @GetMapping
    public List<Prestamo> listarPrestamos() {
        return prestamoService.listarTodos();
    }

    // GET /api/prestamos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> obtenerPrestamoPorId(@PathVariable Integer id) {
        return prestamoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/prestamos
    @PostMapping
    public ResponseEntity<Prestamo> crearPrestamo(@RequestBody Prestamo prestamo) {
        Prestamo prestamoGuardado = prestamoService.guardar(prestamo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prestamoGuardado);
    }

    // PUT /api/prestamos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Prestamo> actualizarPrestamo(
            @PathVariable Integer id, @RequestBody Prestamo prestamo) {
        return prestamoService.buscarPorId(id)
                .map(existente ->
                        ResponseEntity.ok(prestamoService.actualizar(id, prestamo)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/prestamos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPrestamo(@PathVariable Integer id) {
        if (prestamoService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}