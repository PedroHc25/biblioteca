
package pe.edu.utp.biblioteca.controller;

import static org.hamcrest.Matchers.hasSize;
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

    // Almacena el ID del último autor creado.
    private int ultimoAutorId;

    // Extrae el ID del JSON devuelto por el backend.
    private int extraerId(String body) {
        var matcher = java.util.regex.Pattern
                .compile("\"id\"\\s*:\\s*(\\d+)")
                .matcher(body);

        return matcher.find()
                ? Integer.parseInt(matcher.group(1))
                : -1;
    }

    // Registra un autor y devuelve su ID.
    private int crearAutor() throws Exception {
        String body = mockMvc.perform(post("/api/autores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "nombreCompleto": "Gabriel García Márquez",
                      "nacionalidad": "Colombiana",
                      "estado": "Activo"
                    }
                    """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ultimoAutorId = extraerId(body);

        return ultimoAutorId;
    }

    // Registra un libro utilizando un autor existente.
    private int crearLibro(String titulo, String categoria,
            int anio, String estado) throws Exception {

        ultimoAutorId = crearAutor();

        String body = mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "titulo": "%s",
                      "autorId": %d,
                      "categoria": "%s",
                      "anio": %d,
                      "estado": "%s"
                    }
                    """.formatted(titulo, ultimoAutorId, categoria, anio, estado)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return extraerId(body);
    }

    @Test
    void post_01_crearLibroConTodosLosCampos_deberiaRetornar201() throws Exception {

        int autorId = crearAutor();

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "titulo": "Cien años de soledad",
                      "autorId": %d,
                      "categoria": "Novela",
                      "anio": 1967,
                      "estado": "Disponible"
                    }
                    """.formatted(autorId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titulo").value("Cien años de soledad"))
                .andExpect(jsonPath("$.autor.nombreCompleto").value("Gabriel García Márquez"))
                .andExpect(jsonPath("$.categoria").value("Novela"))
                .andExpect(jsonPath("$.anio").value(1967))
                .andExpect(jsonPath("$.estado").value("Disponible"));
    }

    @Test
    void post_02_crearLibroConAnioInvalido_deberiaRetornar400() throws Exception {

        int autorId = crearAutor();

        mockMvc.perform(post("/api/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "titulo": "Don Quijote",
                      "autorId": %d,
                      "categoria": "Novela",
                      "anio": "ABC",
                      "estado": "Disponible"
                    }
                    """.formatted(autorId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void get_01_listarLibrosIniciales_deberiaRetornarListaVacia() throws Exception {

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void get_02_listarLibrosDespuesDeCrear_deberiaTenerUnElemento() throws Exception {

        crearLibro("Hábitos atómicos", "Desarrollo personal",
                2018, "Disponible");

        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo").value("Hábitos atómicos"));
    }

    @Test
    void put_01_actualizarLibroExistente_deberiaRetornar200() throws Exception {

        int id = crearLibro("Cien años de soledad", "Novela",
                1967, "Disponible");

        // Reutiliza el autor creado al registrar el libro.
        int autorId = ultimoAutorId;

        mockMvc.perform(put("/api/libros/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "titulo": "Cien años de soledad (nueva edición)",
                      "autorId": %d,
                      "categoria": "Novela",
                      "anio": 1982,
                      "estado": "Prestado"
                    }
                    """.formatted(autorId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo")
                        .value("Cien años de soledad (nueva edición)"))
                .andExpect(jsonPath("$.estado").value("Prestado"));
    }

    @Test
    void put_02_actualizarLibroInexistente_deberiaRetornar404() throws Exception {

        int autorId = crearAutor();

        mockMvc.perform(put("/api/libros/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "titulo": "Libro fantasma",
                      "autorId": %d,
                      "categoria": "Novela",
                      "anio": 2000,
                      "estado": "Disponible"
                    }
                    """.formatted(autorId)))
                .andExpect(status().isNotFound());
    }
}
