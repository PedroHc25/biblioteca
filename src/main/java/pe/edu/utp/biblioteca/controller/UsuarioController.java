
package pe.edu.utp.biblioteca.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.utp.biblioteca.dto.UsuarioResponse;
import pe.edu.utp.biblioteca.model.Usuario;
import pe.edu.utp.biblioteca.service.UsuarioService;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> obtenerUsuarios() {
        List<UsuarioResponse> usuarios = usuarioService.listarTodos()
                .stream()
                .map(this::convertirAResponse)
                .toList();

        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(
            @RequestBody Usuario usuario) {

        usuario.setId(null);
        usuario.setRol("USER");

        Usuario usuarioGuardado = usuarioService.guardar(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convertirAResponse(usuarioGuardado));
    }

    private UsuarioResponse convertirAResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getDni(),
                usuario.getCorreo(),
                usuario.getTelefono(),
                usuario.getRol()
        );
    }
}