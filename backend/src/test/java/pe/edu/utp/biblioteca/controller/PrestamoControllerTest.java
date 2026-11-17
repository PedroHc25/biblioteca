
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

    // Contadores para generar datos únicos.
    private static int contadorUsuarios = 0;
    private static int contadorAutores = 0;

    // Extrae el ID del JSON devuelto por el backend.
    private int extraerId(String body) {
        var matcher = java.util.regex.Pattern
                .compile("\"id\"\\s*:\\s*(\\d+)")
                .matcher(body);

        return matcher.find()
                ? Integer.parseInt(matcher.group(1))
                : -1;
    }

    // Registra un usuario con DNI y correo únicos.
    private int crearUsuario() throws Exception {

        contadorUsuarios++;

        String dni = String.format(
                "%08d", 10000000 + contadorUsuarios);

        String correo = "prueba"
                + contadorUsuarios
                + "@biblioteca.com";

        String body = mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "nombre": "Usuario",
                      "apellido": "Prueba",
                      "dni": "%s",
                      "correo": "%s",
                      "telefono": "999999999",
                      "password": "Prueba123"
                    }
                    """.formatted(dni, correo)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return extraerId(body);
    }

    // Registra un autor con nombre único.
    private int crearAutor() throws Exception {

        contadorAutores++;

        String nombre = "Autor de prueba " + contadorAutores;

        String body = mockMvc.perform(post("/api/autores")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "nombreCompleto": "%s",
                      "nacionalidad": "Peruana",
                      "estado": "Activo"
                    }
                    """.formatted(nombre)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return extraerId(body);
    }

    // Registra un libro con un autor existente.
    private int crearLibro() throws Exception {

        int autorId = crearAutor();

        String body = mockMvc.perform(post("/api/libros")
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
                .andReturn()
                .getResponse()
                .getContentAsString();

        return extraerId(body);
    }

    // Registra un préstamo con usuario y libro existentes.
    private int crearPrestamo(
            String fechaPrestamo,
            String fechaDevolucion) throws Exception {

        int usuarioId = crearUsuario();
        int libroId = crearLibro();

        String body = mockMvc.perform(post("/api/prestamos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "fechaPrestamo": "%s",
                      "fechaDevolucion": "%s",
                      "estado": "Pendiente",
                      "usuarioId": %d,
                      "libroId": %d
                    }
                    """.formatted(
                        fechaPrestamo,
                        fechaDevolucion,
                        usuarioId,
                        libroId)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return extraerId(body);
    }

    @Test
    void post_01_crearPrestamoConTodosLosCampos_deberiaRetornar201() throws Exception {

        int usuarioId = crearUsuario();
        int libroId = crearLibro();

        mockMvc.perform(post("/api/prestamos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "fechaPrestamo": "2026-09-06",
                      "fechaDevolucion": "2026-09-13",
                      "estado": "Pendiente",
                      "usuarioId": %d,
                      "libroId": %d
                    }
                    """.formatted(usuarioId, libroId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.fechaPrestamo").value("2026-09-06"))
                .andExpect(jsonPath("$.fechaDevolucion").value("2026-09-13"))
                .andExpect(jsonPath("$.estado").value("Pendiente"))
                .andExpect(jsonPath("$.usuarioId").value(usuarioId))
                .andExpect(jsonPath("$.libroId").value(libroId));
    }

    @Test
    void post_02_crearDosPrestamos_deberiaAsignarIdsDiferentes() throws Exception {

        int id1 = crearPrestamo(
                "2026-09-06",
                "2026-09-13");

        int id2 = crearPrestamo(
                "2026-09-08",
                "2026-09-15");

        org.junit.jupiter.api.Assertions.assertNotEquals(id1, id2);
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

        crearPrestamo(
                "2026-09-06",
                "2026-09-13");

        mockMvc.perform(get("/api/prestamos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].estado").value("Pendiente"));
    }
}
