
package com.altoque.altoque_server.xifrat;

import com.altoque.altoque_server.gestor.GestorContrasenyes;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author marc mestres
 */
class GestorContrasenyesTest {

    /**
     * Hashea un text, prova que no es guarden contrasenyes en pla
     * Despres de ser hashejat no por ser null
     * el text no pot ser el mateix ambans i despres de hashejar
     */
    @Test
    void hash_no_es_igual_a_la_contrasenya_en_clar() {
        GestorContrasenyes gc = new GestorContrasenyes();
        String raw = "P@ssw0rd123!";

        String hashed = gc.hash(raw);

        assertNotNull(hashed);
        assertNotEquals(raw, hashed, "El hash no ha de ser igual a la contrasenya en pla");
    }

    /**
     * Prova de validar una contrasenya correcta
     * Després de generar el hash, es comprova que el mètode
     * de comprovació retorna true quan la contrasenya és correcta.
     */
    @Test
    void comprovar_contrasenya_correcta_ok() {
        GestorContrasenyes gc = new GestorContrasenyes();
        String raw = "P@ssw0rd123!";

        String hashed = gc.hash(raw);

        assertTrue(gc.comprovar(raw, hashed), "Ha de validar la contrasenya correcta contra el hash");
    }

    /**
     * Prova de validar una contrasenya incorrecte.
     * el sistema no valida una contrasenya diferent de la que es va utilitzar per generar el hash
     * 
     */
    @Test
    void comprovar_contrasenya_incorrecta_off() {
        GestorContrasenyes gc = new GestorContrasenyes();
        String raw = "P@ssw0rd123!";
        String hashed = gc.hash(raw);

        assertFalse(gc.comprovar("unaAltraContrasenya", hashed), "No ha de validar si la contrasenya és incorrecta");
    }

    
    /**
     * El mateix text genera hashes diferents en cada execució
     * tots han de ser vàlids per a la comprovació
     * 
     */
    @Test
    void bcrypt_genera_hash_diferent_cada_vegada() {
        GestorContrasenyes gc = new GestorContrasenyes();
        String raw = "P@ssw0rd123!";

        String h1 = gc.hash(raw);
        String h2 = gc.hash(raw);

        assertNotEquals(h1, h2, "BCrypt incorpora salt, el hash ha de ser diferent cada vegada");
        assertTrue(gc.comprovar(raw, h1));
        assertTrue(gc.comprovar(raw, h2));
    }
}