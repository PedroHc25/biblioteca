package pe.edu.utp.biblioteca.controller;

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

/**
 * Pruebas de POST /api/auth/login.
 * Entrada: credenciales JSON. Salida esperada: código HTTP + cuerpo.
 * Verificación: jsonPath sobre el cuerpo de la respuesta.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private void registrarUsuario() throws Exception {
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "nombre": "Keyla",
                      "apellido": "Jimenez",
                      "dni": "12345678",
                      "correo": "keyla@gmail.com",
                      "telefono": "999999999",
                      "password": "Admin123"
                    }
                    """))
                .andExpect(status().isCreated());
    }

    @Test
    void login_01_credencialesCorrectas_deberiaRetornar200YToken() throws Exception {
        registrarUsuario();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    { "correo": "keyla@gmail.com", "password": "Admin123" }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void login_02_passwordIncorrecta_deberiaRetornar401() throws Exception {
        registrarUsuario();

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    { "correo": "keyla@gmail.com", "password": "incorrecta" }
                    """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensaje").value("Credenciales incorrectas"))
                .andExpect(jsonPath("$.token").doesNotExist());
    }
}
