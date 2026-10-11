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
class PrestamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private int crearPrestamo(String json) throws Exception {
        String body = mockMvc.perform(post("/api/prestamos")
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
    void post_01_crearPrestamoConTodosLosCampos_deberiaRetornar201() throws Exception {
        mockMvc.perform(post("/api/prestamos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "fechaPrestamo": "2026-09-06",
                      "fechaDevolucion": "2026-09-13",
                      "estado": "ACTIVO",
                      "usuarioId": 1,
                      "libroId": 1
                    }
                    """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.fechaPrestamo").value("2026-09-06"))
                .andExpect(jsonPath("$.fechaDevolucion").value("2026-09-13"))
                .andExpect(jsonPath("$.estado").value("ACTIVO"))
                .andExpect(jsonPath("$.usuarioId").value(1))
                .andExpect(jsonPath("$.libroId").value(1));
    }

    @Test
    void post_02_crearDosPrestamos_deberiaAsignarIdsDiferentes() throws Exception {
        String primero = """
            {
              "fechaPrestamo": "2026-09-06",
              "fechaDevolucion": "2026-09-13",
              "estado": "ACTIVO",
              "usuarioId": 1,
              "libroId": 1
            }
            """;
        String segundo = """
            {
              "fechaPrestamo": "2026-09-08",
              "fechaDevolucion": "2026-09-15",
              "estado": "PENDIENTE",
              "usuarioId": 2,
              "libroId": 2
            }
            """;

        int id1 = crearPrestamo(primero);
        int id2 = crearPrestamo(segundo);

        org.junit.jupiter.api.Assertions.assertNotEquals(id1, id2);
        org.junit.jupiter.api.Assertions.assertEquals(1, id1);
        org.junit.jupiter.api.Assertions.assertEquals(2, id2);
    }

    @Test
    void get_01_listarPrestamosIniciales_deberiaRetornarListaVacia() throws Exception {
        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void get_02_listarPrestamosDespuesDeCrear_deberiaTenerUnElemento() throws Exception {
        crearPrestamo("""
            {
              "fechaPrestamo": "2026-09-06",
              "fechaDevolucion": "2026-09-13",
              "estado": "ACTIVO",
              "usuarioId": 1,
              "libroId": 1
            }
            """);

        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].estado").value("ACTIVO"));
    }
}
