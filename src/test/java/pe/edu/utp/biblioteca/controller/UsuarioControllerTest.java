package pe.edu.utp.biblioteca.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UsuarioControllerTest {

@Autowired
private MockMvc mockMvc;

@Test
void post_01_crearUsuarioConTodosLosCampos_deberiaRetornar201()
        throws Exception {

    String jsonPayload = """
        {
          "id": 10,
          "nombre": "Keyla",
          "apellido": "Jimenez Gallegos",
          "dni": "12345678",
          "correo": "keyla@gmail.com",
          "telefono": "999999999",
          "password": "Admin123"
        }
        """;

    mockMvc.perform(post("/api/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonPayload))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.nombre").value("Keyla"))
            .andExpect(jsonPath("$.apellido").value("Jimenez Gallegos"))
            .andExpect(jsonPath("$.dni").value("12345678"))
            .andExpect(jsonPath("$.correo").value("keyla@gmail.com"))
            .andExpect(jsonPath("$.telefono").value("999999999"))
            .andExpect(jsonPath("$.rol").value("USER"))
            .andExpect(jsonPath("$.password").doesNotExist());
}

@Test
void post_02_crearUsuarioSinId_deberiaGenerarIdAutomaticoYRetornar201()
        throws Exception {

    String jsonPayload = """
        {
          "nombre": "Carlos",
          "apellido": "Ramirez",
          "dni": "78901234",
          "correo": "carlos@gmail.com",
          "telefono": "911223344",
          "password": "Admin123"
        }
        """;

mockMvc.perform(post("/api/usuarios")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonPayload))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.nombre").value("Carlos"))
        .andExpect(jsonPath("$.correo").value("carlos@gmail.com"));
}

@Test
void post_03_crearMultiplesUsuariosConsecutivos_deberiaRetornar201()
        throws Exception {

    String usuarioA = """
        {
          "nombre": "Ana",
          "apellido": "Torres",
          "dni": "45678901",
          "correo": "ana@gmail.com",
          "telefono": "955667788",
          "password": "Admin123"
        }
        """;

    String usuarioB = """
        {
          "nombre": "Luis",
          "apellido": "Vargas",
          "dni": "56789012",
          "correo": "luis@gmail.com",
          "telefono": "944332211",
          "password": "Admin123"
        }
        """;

    mockMvc.perform(post("/api/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(usuarioA))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre").value("Ana"));

    mockMvc.perform(post("/api/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(usuarioB))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre").value("Luis"));
}

@Test
void get_01_obtenerUsuariosIniciales_deberiaRetornarListaVacia()
        throws Exception {

    mockMvc.perform(get("/api/usuarios"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(
                MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(0)));
}

@Test
void get_02_obtenerUsuariosDespuesDePost_deberiaIncrementarCantidadElementos()
        throws Exception {

    String nuevoUsuario = """
        {
          "nombre": "Sonia",
          "apellido": "Castro",
          "dni": "88776655",
          "correo": "sonia@gmail.com",
          "telefono": "933445566",
          "password": "Admin123"
        }
        """;

    mockMvc.perform(post("/api/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(nuevoUsuario))
            .andExpect(status().isCreated());

    mockMvc.perform(get("/api/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id").isNumber())
            .andExpect(jsonPath("$[0].nombre").value("Sonia"))
            .andExpect(jsonPath("$[0].dni").value("88776655"));
}

@Test
void get_03_verificarEstructuraCompletaDeCampos_deberiaContenerTodosLosAtributos()
        throws Exception {

    String nuevoUsuario = """
        {
          "nombre": "Pedro",
          "apellido": "Huayanay",
          "dni": "11223344",
          "correo": "pedro@gmail.com",
          "telefono": "988776655",
          "password": "Admin123"
        }
        """;

    mockMvc.perform(post("/api/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(nuevoUsuario))
            .andExpect(status().isCreated());

    mockMvc.perform(get("/api/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].nombre").exists())
            .andExpect(jsonPath("$[0].apellido").exists())
            .andExpect(jsonPath("$[0].dni").exists())
            .andExpect(jsonPath("$[0].correo").exists())
            .andExpect(jsonPath("$[0].telefono").exists())
            .andExpect(jsonPath("$[0].rol").exists())
            .andExpect(jsonPath("$[0].password").doesNotExist());
}

}
