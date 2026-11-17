package pe.edu.utp.biblioteca.dto;

public record UsuarioResponse(
        int id,
        String nombre,
        String apellido,
        String dni,
        String correo,
        String telefono,
        String rol
) {
}