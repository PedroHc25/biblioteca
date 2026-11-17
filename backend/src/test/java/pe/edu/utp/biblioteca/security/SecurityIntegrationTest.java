package pe.edu.utp.biblioteca.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Pruebas de seguridad con los filtros REALES activos (JWT + Spring Security).
 * Verifican que las rutas protegidas exigen token y que un token válido da acceso.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void seguridad_01_rutaProtegidaSinToken_deberiaRetornar401() throws Exception {
        mockMvc.perform(get("/api/libros"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void seguridad_02_rutaProtegidaConTokenValido_deberiaRetornar200() throws Exception {
        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "nombre": "Ana",
                      "apellido": "Torres",
                      "dni": "45678901",
                      "correo": "ana@gmail.com",
                      "telefono": "955667788",
                      "password": "Admin123"
                    }
                    """))
                .andExpect(status().isCreated());

        String cuerpo = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    { "correo": "ana@gmail.com", "password": "Admin123" }
                    """))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Matcher m = Pattern.compile("\"token\"\\s*:\\s*\"([^\"]+)\"").matcher(cuerpo);
        org.junit.jupiter.api.Assertions.assertTrue(m.find(), "La respuesta debe traer el token");
        String token = m.group(1);

        mockMvc.perform(get("/api/libros")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
