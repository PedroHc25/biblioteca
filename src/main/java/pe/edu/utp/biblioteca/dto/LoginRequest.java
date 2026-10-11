package pe.edu.utp.biblioteca.dto;

public record LoginRequest(
        String correo,
        String password
) {
}