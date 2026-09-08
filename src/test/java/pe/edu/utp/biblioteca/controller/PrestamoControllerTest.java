package pe.edu.utp.biblioteca.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
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

    // ============================
    // GET /prestamos
    // ============================

    @Test
    void listarPrestamosDevuelveListaVaciaInicialmente() throws Exception {

        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void listarPrestamosDespuesDeCrearUnoDevuelveListaConPrestamo() throws Exception {

        mockMvc.perform(post("/api/prestamos")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 0,
                                  "fechaPrestamo": "2026-09-06",
                                  "fechaDevolucion": "2026-09-13",
                                  "estado": "ACTIVO",
                                  "usuarioId": 1,
                                  "libroId": 1
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].estado").value("ACTIVO"));
    }

    @Test
    void listarPrestamosDespuesDeCrearDosDevuelveDosPrestamos() throws Exception {

        mockMvc.perform(post("/api/prestamos")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 0,
                                  "fechaPrestamo": "2026-09-06",
                                  "fechaDevolucion": "2026-09-13",
                                  "estado": "ACTIVO",
                                  "usuarioId": 1,
                                  "libroId": 1
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/prestamos")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 0,
                                  "fechaPrestamo": "2026-09-08",
                                  "fechaDevolucion": "2026-09-15",
                                  "estado": "PENDIENTE",
                                  "usuarioId": 2,
                                  "libroId": 2
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ============================
    // POST /prestamos
    // ============================

    @Test
    void crearPrestamoDevuelveCreatedYAsignaId() throws Exception {

        mockMvc.perform(post("/api/prestamos")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 0,
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
    void crearSegundoPrestamoAsignaIdDiferente() throws Exception {

        mockMvc.perform(post("/api/prestamos")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 0,
                                  "fechaPrestamo": "2026-09-06",
                                  "fechaDevolucion": "2026-09-13",
                                  "estado": "ACTIVO",
                                  "usuarioId": 1,
                                  "libroId": 1
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(post("/api/prestamos")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 0,
                                  "fechaPrestamo": "2026-09-08",
                                  "fechaDevolucion": "2026-09-15",
                                  "estado": "PENDIENTE",
                                  "usuarioId": 2,
                                  "libroId": 2
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void crearPrestamoConDatosCompletosDevuelveInformacionCorrecta() throws Exception {

        mockMvc.perform(post("/api/prestamos")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 0,
                                  "fechaPrestamo": "2026-09-10",
                                  "fechaDevolucion": "2026-09-17",
                                  "estado": "ACTIVO",
                                  "usuarioId": 5,
                                  "libroId": 8
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fechaPrestamo").value("2026-09-10"))
                .andExpect(jsonPath("$.fechaDevolucion").value("2026-09-17"))
                .andExpect(jsonPath("$.usuarioId").value(5))
                .andExpect(jsonPath("$.libroId").value(8));
    }
}