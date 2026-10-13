package pe.edu.utp.biblioteca.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void extraerCorreo_deberiaRetornarCorreoDelToken() {
        String correo = "pedro@gmail.com";
        String token = jwtService.generarToken(correo);

        String correoExtraido = jwtService.extraerCorreo(token);

        assertEquals(correo, correoExtraido);
    }

    @Test
    void validarToken_deberiaRetornarFalseConCorreoIncorrecto() {
        String token = jwtService.generarToken("pedro@gmail.com");

        boolean resultado = jwtService.validarToken(
                token, "otro@gmail.com"
        );

        assertFalse(resultado);
    }
}