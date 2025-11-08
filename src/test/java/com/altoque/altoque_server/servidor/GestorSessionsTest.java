
package com.altoque.altoque_server.servidor;

import com.altoque.altoque_server.Const;
import com.altoque.altoque_server.gestor.GestorSessions;
import com.altoque.altoque_server.model.Sessio;
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
        Sessio sessio = s.iniciarSessio("Meji87", Const.Rol.USUARI);
        String token = sessio.getToken();
        assertNotNull(sessio, "El token no pot ser null");
        assertTrue(s.esValida(token), "El token hauria de ser vàlid tot just crear-se");
    }
    
    /**
     * Comprova que una sessió és vàlida despres de crearla
     */
    @Test
    void validarSessio(){
        GestorSessions s = new GestorSessions();
        Sessio sessio = s.iniciarSessio("Meji87", Const.Rol.USUARI);
        String token = sessio.getToken();
        assertTrue(s.esValida(token), "El token hauria de ser vàlid tot just crear-se");
    }
    
    /**
     * Comprova que el token estigui assosiat a l'usuari
     */
    @Test
    void buscarTokenUsuari(){
        GestorSessions s = new GestorSessions();
        Sessio sessio = s.iniciarSessio("Meji87", Const.Rol.USUARI);
        String token = sessio.getToken();
        assertEquals(token, s.buscarSessio(sessio.getToken()), "Hauria de trobar el token per l'usuari");
    }
    
    /**
     * Comprova que la sessió no sigui vàlida desdpres de logout
     */
    @Test
    void sortirSessio(){
        GestorSessions s = new GestorSessions();
        Sessio sessio = s.iniciarSessio("Meji87", Const.Rol.USUARI);
        String token = sessio.getToken();
        s.tancarSessio(token);
        assertFalse(s.esValida(token), "Després del logout el token no ha de ser vàlid");
    }
    
    /**
     * Comprova que buscar el token d'un usuari sense sessió retorna null.
     */
    @Test
    void usuariSenseSessio() {
        GestorSessions s = new GestorSessions();
        assertNull(s.buscarSessio("inexistent"), "Un usuari sense sessió no ha de tenir token");
    }
}
