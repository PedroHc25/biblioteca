package pe.edu.utp.biblioteca.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

// Cada prueba comienza con un contexto nuevo para no depender de otros casos.
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EliminarLibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    void eliminarLibroDosVeces_segundoIntentoDeberiaRetornar404() throws Exception {
        prepararLibro(20, "Hábitos atómicos");

        mockMvc.perform(delete("/api/libros/20"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        mockMvc.perform(delete("/api/libros/20"))
                .andExpect(status().isNotFound());
    }

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
