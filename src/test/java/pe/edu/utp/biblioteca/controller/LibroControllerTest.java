package pe.edu.utp.biblioteca.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private int crearLibro(String json) throws Exception {
        String body = mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return extraerId(body);
    }

    private int extraerId(String body) {
        var m = java.util.regex.Pattern
                .compile("\"id\"\\s*:\\s*(\\d+)")
                .matcher(body);
        return m.find() ? Integer.parseInt(m.group(1)) : -1;
    }

    // ============================
    // POST /api/libros
    // ============================

    @Test
    void post_01_crearLibroConTodosLosCampos_deberiaRetornar201() throws Exception {
        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "titulo": "Cien años de soledad",
                      "autor": "Gabriel García Márquez",
                      "categoria": "Novela",
                      "anio": 1967,
                      "estado": "Disponible"
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titulo").value("Cien años de soledad"))
                .andExpect(jsonPath("$.autor").value("Gabriel García Márquez"))
                .andExpect(jsonPath("$.categoria").value("Novela"))
                .andExpect(jsonPath("$.anio").value(1967))
                .andExpect(jsonPath("$.estado").value("Disponible"));
    }

    @Test
    void post_02_crearLibroConAnioInvalido_deberiaRetornar400() throws Exception {
        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "titulo": "Don Quijote",
                      "autor": "Miguel de Cervantes",
                      "categoria": "Novela",
                      "anio": "ABC",
                      "estado": "Disponible"
                    }
                    """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void post_03_crearVariosLibros_deberiaAsignarIdsAutomaticos() throws Exception {
        String primero = """
            {
              "titulo": "El principito",
              "autor": "Antoine de Saint-Exupéry",
              "categoria": "Literatura",
              "anio": 1943,
              "estado": "Disponible"
            }
            """;
        String segundo = """
            {
              "titulo": "1984",
              "autor": "George Orwell",
              "categoria": "Ciencia Ficción",
              "anio": 1949,
              "estado": "Disponible"
            }
            """;

        int id1 = crearLibro(primero);
        int id2 = crearLibro(segundo);

        org.junit.jupiter.api.Assertions.assertNotEquals(id1, id2);
    }

    // ============================
    // GET /api/libros
    // ============================

    @Test
    void get_01_listarLibrosIniciales_deberiaRetornarListaVacia() throws Exception {
        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void get_02_listarLibrosDespuesDeCrear_deberiaTenerUnElemento() throws Exception {
        crearLibro("""
            {
              "titulo": "Hábitos atómicos",
              "autor": "James Clear",
              "categoria": "Desarrollo personal",
              "anio": 2018,
              "estado": "Disponible"
            }
            """);

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo").value("Hábitos atómicos"));
    }

    @Test
    void get_03_obtenerLibroPorIdExistente_deberiaRetornarLibro() throws Exception {
        int id = crearLibro("""
            {
              "titulo": "El arte de la guerra",
              "autor": "Sun Tzu",
              "categoria": "Estrategia",
              "anio": 500,
              "estado": "Disponible"
            }
            """);

        mockMvc.perform(get("/api/libros/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("El arte de la guerra"))
                .andExpect(jsonPath("$.autor").value("Sun Tzu"));
    }

    @Test
    void get_04_obtenerLibroPorIdInexistente_deberiaRetornar404() throws Exception {
        mockMvc.perform(get("/api/libros/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void get_05_obtenerLibroPorIdFormatoIncorrecto_deberiaRetornar400() throws Exception {
        mockMvc.perform(get("/api/libros/abc"))
                .andExpect(status().isBadRequest());
    }

    // ============================
    // PUT /api/libros/{id}
    // ============================

    @Test
    void put_01_actualizarLibroExistente_deberiaRetornar200() throws Exception {
        int id = crearLibro("""
            {
              "titulo": "Cien años de soledad",
              "autor": "Gabriel García Márquez",
              "categoria": "Novela",
              "anio": 1967,
              "estado": "Disponible"
            }
            """);

        mockMvc.perform(put("/api/libros/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "titulo": "Cien años de soledad (nueva edición)",
                      "autor": "Gabriel García Márquez",
                      "categoria": "Novela",
                      "anio": 1982,
                      "estado": "Prestado"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo")
                        .value("Cien años de soledad (nueva edición)"))
                .andExpect(jsonPath("$.estado").value("Prestado"));
    }

    @Test
    void put_02_actualizarLibroInexistente_deberiaRetornar404() throws Exception {
        mockMvc.perform(put("/api/libros/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "titulo": "Libro fantasma",
                      "autor": "Anónimo",
                      "categoria": "Novela",
                      "anio": 2000,
                      "estado": "Disponible"
                    }
                    """))
                .andExpect(status().isNotFound());
    }

    // ============================
    // DELETE /api/libros/{id}
    // ============================

    @Test
    void delete_01_eliminarLibroExistente_deberiaRetornar204() throws Exception {
        int id = crearLibro("""
            {
              "titulo": "No me puedes lastimar",
              "autor": "David Goggins",
              "categoria": "Desarrollo personal",
              "anio": 2018,
              "estado": "Disponible"
            }
            """);

        mockMvc.perform(delete("/api/libros/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_02_eliminarLibroInexistente_deberiaRetornar404() throws Exception {
        mockMvc.perform(delete("/api/libros/479"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_03_eliminarLibroDosVeces_segundoIntentoDeberiaRetornar404() throws Exception {
        int id = crearLibro("""
            {
              "titulo": "El principito",
              "autor": "Antoine de Saint-Exupéry",
              "categoria": "Literatura",
              "anio": 1943,
              "estado": "Disponible"
            }
            """);

        mockMvc.perform(delete("/api/libros/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/libros/{id}", id))
                .andExpect(status().isNotFound());
    }
}