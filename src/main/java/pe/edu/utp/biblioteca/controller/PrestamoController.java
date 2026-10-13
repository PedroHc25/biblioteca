package pe.edu.utp.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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


    // POST /api/prestamos
    @PostMapping
    public ResponseEntity<Prestamo> crearPrestamo(@RequestBody Prestamo prestamo) {
        Prestamo prestamoGuardado = prestamoService.guardar(prestamo);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(prestamoGuardado);
    }


}