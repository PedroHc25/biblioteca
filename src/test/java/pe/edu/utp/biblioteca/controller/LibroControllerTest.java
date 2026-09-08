package pe.edu.utp.biblioteca.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
public class LibroControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void crearLibro_deberiaRetornarLibro() throws Exception {

    mockMvc.perform(post("/api/libros")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "id": 1,
              "titulo": "Cien años de soledad",
              "autor": "Gabriel García Márquez",
              "categoria": "Novela",
              "anio": 1967,
              "estado": "Disponible"
            }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.titulo").value("Cien años de soledad"))
        .andExpect(jsonPath("$.autor").value("Gabriel García Márquez"));
  }

  @Test
  void crearSegundoLibro_deberiaRetornarlo() throws Exception {

    mockMvc.perform(post("/api/libros")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "id": 2,
              "titulo": "El principito",
              "autor": "Antoine de Saint-Exupéry",
              "categoria": "Literatura",
              "anio": 1943,
              "estado": "Disponible"
            }
            """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.titulo").value("El principito"))
        .andExpect(jsonPath("$.autor").value("Antoine de Saint-Exupéry"));
  }

  @Test
  void crearLibro_anioInvalido_deberiaRetornar400() throws Exception {

    mockMvc.perform(post("/api/libros")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "id": 3,
              "titulo": "Don Quijote",
              "autor": "Miguel de Cervantes",
              "categoria": "Novela",
              "anio": "ABC",
              "estado": "Disponible"
            }
            """))
        .andExpect(status().isBadRequest());
  }

  // Pruebas de Brayan: eliminar libros
  @Test
  void eliminarLibroExistente_deberiaRetornar204() throws Exception {
    mockMvc.perform(post("/api/libros")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
                "id": 10,
                "titulo": "No me puedes lastimar",
                "autor": "David Goggins",
                "categoria": "Desarrollo personal",
                "anio": 2018,
                "estado": "Disponible"
            }
            """))
        .andExpect(status().isOk());

    mockMvc.perform(delete("/api/libros/10"))
        .andExpect(status().isNoContent());
  }

  @Test
  void eliminarLibroDosVeces_segundoIntentoDeberiaRetornar404() throws Exception {
    mockMvc.perform(post("/api/libros")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "id": 20,
              "titulo": "Hábitos atómicos",
              "autor": "James Clear",
              "categoria": "Desarrollo personal",
              "anio": 2018,
              "estado": "Disponible"
            }
            """))
        .andExpect(status().isOk());

    mockMvc.perform(delete("/api/libros/20"))
        .andExpect(status().isNoContent());

    mockMvc.perform(delete("/api/libros/20"))
        .andExpect(status().isNotFound());
  }

  @Test
  void eliminarLibroInexistente_deberiaRetornar404() throws Exception {
    mockMvc.perform(delete("/api/libros/479"))
        .andExpect(status().isNotFound());
  }

  // Pruebas de Brenda: traer libros
  @Test
  void obtenerTodosLosLibros_deberiaRetornarLista() throws Exception {
    mockMvc.perform(post("/api/libros")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "id": 100,
              "titulo": "El arte de la guerra",
              "autor": "Sun Tzu",
              "categoria": "Estrategia",
              "anio": 500,
              "estado": "Disponible"
            }
            """))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/libros"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isNotEmpty());
  }

  @Test
  void obtenerTodosLosLibros_rutaIncorrecta_deberiaRetornar404() throws Exception {
      mockMvc.perform(get("/api/librosss"))
              .andExpect(status().isNotFound());
  }

  @Test
  void usarMetodoNoSoportado_deberiaRetornar405() throws Exception {
      mockMvc.perform(put("/api/libros"))
              .andExpect(status().isMethodNotAllowed());
  }

  @Test
  void obtenerLibroPorIdExistente_deberiaRetornarLibro() throws Exception {
    mockMvc.perform(post("/api/libros")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""
            {
              "id": 101,
              "titulo": "1984",
              "autor": "George Orwell",
              "categoria": "Ciencia Ficción",
              "anio": 1949,
              "estado": "Disponible"
            }
            """))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/libros/101"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(101))
        .andExpect(jsonPath("$.titulo").value("1984"))
        .andExpect(jsonPath("$.autor").value("George Orwell"));
  }

  @Test
  void obtenerLibroInexistente_deberiaRetornar404() throws Exception {
    mockMvc.perform(get("/api/libros/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void obtenerLibroPorId_formatoIncorrecto_deberiaRetornar400() throws Exception {
      mockMvc.perform(get("/api/libros/abc"))
              .andExpect(status().isBadRequest());
  }


}
