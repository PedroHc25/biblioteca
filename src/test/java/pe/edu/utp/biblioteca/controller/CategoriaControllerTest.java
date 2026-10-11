package pe.edu.utp.biblioteca.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private int crearCategoria(String json) throws Exception {
        String body = mockMvc.perform(post("/api/categorias")
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

    @Test
    void post_01_crearCategoriaConTodosLosCampos_deberiaRetornar201() throws Exception {
        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "nombre": "Novela",
                      "descripcion": "Ficción narrativa extensa",
                      "tipo": "Literatura",
                      "estado": "ACTIVO"
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nombre").value("Novela"))
                .andExpect(jsonPath("$.descripcion").value("Ficción narrativa extensa"))
                .andExpect(jsonPath("$.tipo").value("Literatura"))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test
    void post_02_crearDosCategorias_deberiaAsignarIdsDiferentes() throws Exception {
        int id1 = crearCategoria("""
            {
              "nombre": "Novela",
              "descripcion": "Ficción narrativa extensa",
              "tipo": "Literatura",
              "estado": "ACTIVO"
            }
            """);
        int id2 = crearCategoria("""
            {
              "nombre": "Historia",
              "descripcion": "Libros de hechos históricos",
              "tipo": "No ficción",
              "estado": "ACTIVO"
            }
            """);

        org.junit.jupiter.api.Assertions.assertNotEquals(id1, id2);
    }

    @Test
    void get_01_listarCategoriasIniciales_deberiaRetornarListaVacia() throws Exception {
        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void get_02_listarCategoriasDespuesDeCrear_deberiaTenerUnElemento() throws Exception {
        crearCategoria("""
            {
              "nombre": "Ciencia Ficción",
              "descripcion": "Historias futuristas",
              "tipo": "Literatura",
              "estado": "ACTIVO"
            }
            """);

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Ciencia Ficción"));
    }
}
