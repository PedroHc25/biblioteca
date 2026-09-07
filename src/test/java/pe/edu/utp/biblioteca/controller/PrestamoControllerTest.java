package pe.edu.utp.biblioteca.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrestamoController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PrestamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void crearPrestamoDevuelveCreatedYAsignaId() throws Exception {
        mockMvc.perform(post("/prestamos")
                        .contentType(APPLICATION_JSON)
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
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test
    void listarPrestamosDevuelveListaVaciaInicialmente() throws Exception {
        mockMvc.perform(get("/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void buscarPrestamoInexistenteDevuelveNotFound() throws Exception {
        mockMvc.perform(get("/prestamos/999"))
                .andExpect(status().isNotFound());
    }
}