package pe.edu.utp.biblioteca.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.utp.biblioteca.model.Prestamo;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final List<Prestamo> prestamos = new ArrayList<>();
    private final AtomicInteger siguienteId = new AtomicInteger(1);

    // GET /prestamos
    @GetMapping
    public List<Prestamo> listarPrestamos() {
        return prestamos;
    }

    // POST /prestamos
    @PostMapping
    public ResponseEntity<Prestamo> crearPrestamo(@RequestBody Prestamo prestamo) {
        prestamo.setId(siguienteId.getAndIncrement());
        prestamos.add(prestamo);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prestamo);
    }
}