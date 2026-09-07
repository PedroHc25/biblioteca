package pe.edu.utp.biblioteca.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void crearUsuario_deberiaRetornarUsuarioCreado() throws Exception {
        mockMvc.perform(post("/api/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "id": 1,
                  "nombre": "keyla mialu",
                  "apellido": "jimenez gallegos",
                  "dni": "12345678",
                  "correo": "keyla@gmail.com",
                  "telefono": "999999999"
                }
                """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("keyla mialu"))
            .andExpect(jsonPath("$.apellido").value("jimenez gallegos"));
    }

    @Test
    void obtenerUsuarios_deberiaRetornarLista() throws Exception {
        mockMvc.perform(post("/api/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "id": 2,
                  "nombre": "María",
                  "apellido": "Gómez",
                  "dni": "87654321",
                  "correo": "maria@example.com",
                  "telefono": "123456789"
                }
                """))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isNotEmpty());
    }
}
