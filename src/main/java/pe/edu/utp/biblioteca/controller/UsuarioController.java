package pe.edu.utp.biblioteca.controller;

import java.util.ArrayList;
import java.util.List;
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

    private final List<Usuario> usuarios = new ArrayList<>();

    public UsuarioController() {
        usuarios.add(new Usuario(1, "Pedro", "Perez", "71234567", "juan.perez@utp.edu.pe", "987654321"));
        usuarios.add(new Usuario(2, "Maria", "Lopez", "72345678", "maria.lopez@utp.edu.pe", "912345678"));
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        if (usuario.getId() == 0) {
            usuario.setId(usuarios.size() + 1);
        }
        
        usuarios.add(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }
}
