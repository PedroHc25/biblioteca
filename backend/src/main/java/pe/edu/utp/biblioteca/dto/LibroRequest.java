package pe.edu.utp.biblioteca.dto;

public record LibroRequest(
        String titulo,
        Integer autorId,
        String categoria,
        Integer anio,
        String estado
) {
}