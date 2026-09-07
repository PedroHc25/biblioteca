package pe.edu.utp.biblioteca.controller;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.utp.biblioteca.model.Usuario;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final Map<Integer, Usuario> usuarios = new ConcurrentHashMap<>();

    @GetMapping
    public Collection<Usuario> obtenerUsuarios() {
        return usuarios.values();
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        usuarios.put(usuario.getId(), usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }
}
