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
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_METHOD)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void post_01_crearUsuarioConTodosLosCampos_deberiaRetornar201() throws Exception {
        String jsonPayload = """
            {
              "id": 10,
              "nombre": "Keyla Mialu",
              "apellido": "Jimenez Gallegos",
              "dni": "12345678",
              "correo": "keyla@gmail.com",
              "telefono": "999999999"
            }
            """;

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.nombre").value("Keyla Mialu"))
                .andExpect(jsonPath("$.apellido").value("Jimenez Gallegos"))
                .andExpect(jsonPath("$.dni").value("12345678"))
                .andExpect(jsonPath("$.correo").value("keyla@gmail.com"))
                .andExpect(jsonPath("$.telefono").value("999999999"));
    }

    @Test
    void post_02_crearUsuarioSinId_deberiaGenerarIdAutomaticoYRetornar201() throws Exception {
        String jsonPayload = """
            {
              "nombre": "Carlos",
              "apellido": "Ramirez",
              "dni": "78901234",
              "correo": "carlos.ramirez@utp.edu.pe",
              "telefono": "911223344"
            }
            """;

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.correo").value("carlos.ramirez@utp.edu.pe"));
    }

    @Test
    void post_03_crearMultiplesUsuariosConsecutivos_deberiaRetornar201() throws Exception {
        String usuarioA = """
            {
              "id": 20,
              "nombre": "Ana",
              "apellido": "Torres",
              "dni": "45678901",
              "correo": "ana.torres@utp.edu.pe",
              "telefono": "955667788"
            }
            """;

        String usuarioB = """
            {
              "id": 21,
              "nombre": "Luis",
              "apellido": "Vargas",
              "dni": "56789012",
              "correo": "luis.vargas@utp.edu.pe",
              "telefono": "944332211"
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
    void get_01_obtenerUsuariosIniciales_deberiaRetornarListaConValoresPorDefecto() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2))) 
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$.nombre").value("Maria"));
    }

    @Test
    void get_02_obtenerUsuariosDespuesDePost_deberiaIncrementarCantidadElementos() throws Exception {
        String nuevoUsuario = """
            {
              "id": 3,
              "nombre": "Sonia",
              "apellido": "Castro",
              "dni": "88776655",
              "correo": "sonia.castro@utp.edu.pe",
              "telefono": "933445566"
            }
            """;

        mockMvc.perform(post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(nuevoUsuario))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nombre").value("Sonia"))
                .andExpect(jsonPath("$.dni").value("88776655"));
    }

    @Test
    void get_03_verificarEstructuraCompletaDeCampos_deberiaContenerTodosLosAtributos() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].nombre").exists())
                .andExpect(jsonPath("$[0].apellido").exists())
                .andExpect(jsonPath("$[0].dni").exists())
                .andExpect(jsonPath("$[0].correo").exists())
                .andExpect(jsonPath("$[0].telefono").exists());
    }
}
