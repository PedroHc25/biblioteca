package pe.edu.utp.biblioteca.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class LibroControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void crearLibro_deberiaRetornarLibro() throws Exception {

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "id": 1,
                      "titulo": "Cien años de soledad",
                      "autor": "Gabriel García Márquez",
                      "categoria": "Novela",
                      "anio": 1967,
                      "estado": "Disponible"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Cien años de soledad"))
                .andExpect(jsonPath("$.autor").value("Gabriel García Márquez"));
    }

    @Test
    void crearSegundoLibro_deberiaRetornarlo() throws Exception {

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "id": 2,
                      "titulo": "El principito",
                      "autor": "Antoine de Saint-Exupéry",
                      "categoria": "Literatura",
                      "anio": 1943,
                      "estado": "Disponible"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.titulo").value("El principito"))
                .andExpect(jsonPath("$.autor").value("Antoine de Saint-Exupéry"));
    }

    @Test
    void crearLibro_anioInvalido_deberiaRetornar400() throws Exception {

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "id": 3,
                      "titulo": "Don Quijote",
                      "autor": "Miguel de Cervantes",
                      "categoria": "Novela",
                      "anio": "ABC",
                      "estado": "Disponible"
                    }
                    """))
                .andExpect(status().isBadRequest());
    }

    // Pruebas de Brayan: eliminar libros
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void eliminarLibroExistente_deberiaRetornar204YConservarOtroLibro() throws Exception {
        prepararLibro(10, "No me puedes lastimar");
        prepararLibro(11, "Libro que debe conservarse");

        mockMvc.perform(delete("/api/libros/10"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        // Una segunda eliminación confirma que el libro ya no existe.
        mockMvc.perform(delete("/api/libros/10"))
                .andExpect(status().isNotFound());

        // El libro distinto sigue disponible para eliminarse.
        mockMvc.perform(delete("/api/libros/11"))
                .andExpect(status().isNoContent());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void eliminarLibroDosVeces_segundoIntentoDeberiaRetornar404() throws Exception {
        prepararLibro(20, "Hábitos atómicos");

        mockMvc.perform(delete("/api/libros/20"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        mockMvc.perform(delete("/api/libros/20"))
                .andExpect(status().isNotFound());
    }

    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void eliminarLibroInexistente_deberiaRetornar404YConservarExistente() throws Exception {
        prepararLibro(30, "Libro existente");

        mockMvc.perform(delete("/api/libros/479"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/libros/30"))
                .andExpect(status().isNoContent());
    }

    // Punto a adaptar al integrar MySQL: guardar con LibroRepository y devolver
    // el ID generado. Por ahora utiliza el contrato del primer entregable.
    private void prepararLibro(int id, String titulo) throws Exception {
        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "id": %d,
                      "titulo": "%s",
                      "autor": "Autor de prueba",
                      "categoria": "Desarrollo personal",
                      "anio": 2018,
                      "estado": "Disponible"
                    }
                    """.formatted(id, titulo)))
                .andExpect(status().isOk());
    }
}
