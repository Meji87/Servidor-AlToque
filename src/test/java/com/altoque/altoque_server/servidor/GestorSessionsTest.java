
package com.altoque.altoque_server.servidor;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author marc mestres
 */
public class GestorSessionsTest {

    /**
     * Comprova que el token no és null i que la sessió és vàlida, després d'iniciar sessió amb un usuari existent
     */
    @Test
    void iniciarSessio() {
        GestorSessions s = new GestorSessions();
        String token = s.iniciarSessio("M13");
        assertNotNull(token, "El token no pot ser null");
        assertTrue(s.esValida(token), "El token hauria de ser vàlid tot just crear-se");
    }
    
    /**
     * Comprova que una sessió és vàlida despres de crearla
     */
    @Test
    void validarSessio(){
        GestorSessions s = new GestorSessions();
        String token = s.iniciarSessio("M13");
        assertTrue(s.esValida(token), "El token hauria de ser vàlid tot just crear-se");
    }
    
    /**
     * Comprova que el token estigui assosiat a l'usuari
     */
    @Test
    void buscarTokenUsuari(){
        GestorSessions s = new GestorSessions();
        String token = s.iniciarSessio("M13");
        assertEquals(token, s.buscarTokenUsuari("M13"), "Hauria de trobar el token per l'usuari");
    }
    
    /**
     * Comprova que la sessió no sigui vàlida desdpres de logout
     */
    @Test
    void sortirSessio(){
        GestorSessions s = new GestorSessions();
        String token = s.iniciarSessio("M13");
        s.borrarSessio(token);
        assertFalse(s.esValida(token), "Després del logout el token no ha de ser vàlid");
    }
    
    /**
     * Comprova que buscar el token d'un usuari sense sessió retorna null.
     */
    @Test
    void usuariSenseSessio() {
        GestorSessions s = new GestorSessions();
        assertNull(s.buscarTokenUsuari("inexistent"), "Un usuari sense sessió no ha de tenir token");
    }
}
