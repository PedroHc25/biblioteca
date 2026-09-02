package pe.edu.utp.biblioteca.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

}