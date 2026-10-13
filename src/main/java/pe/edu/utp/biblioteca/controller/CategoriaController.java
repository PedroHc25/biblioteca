package pe.edu.utp.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.utp.biblioteca.model.Categoria;
import pe.edu.utp.biblioteca.service.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // GET /api/categorias
    @GetMapping
    public List<Categoria> obtenerCategorias() {
        return categoriaService.listarTodas();
    }


    // POST /api/categorias
    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria) {
        Categoria categoriaGuardada = categoriaService.guardar(categoria);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoriaGuardada);
    }


}