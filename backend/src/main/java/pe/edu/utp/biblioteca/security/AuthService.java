package pe.edu.utp.biblioteca.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pe.edu.utp.biblioteca.dto.LoginRequest;
import pe.edu.utp.biblioteca.model.Usuario;
import pe.edu.utp.biblioteca.repository.UsuarioRepository;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(LoginRequest request) {
        Usuario usuario = usuarioRepository
                .findByCorreo(request.correo())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Credenciales incorrectas"));

        if (!passwordEncoder.matches(
                request.password(),
                usuario.getPassword())) {
            throw new IllegalArgumentException(
                    "Credenciales incorrectas");
        }

        return jwtService.generarToken(usuario.getCorreo());
    }
}