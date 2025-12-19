
package com.altoque.altoque_server.xifrat;

import com.altoque.altoque_server.gestor.GestorXifrat;
import com.altoque.altoque_server.servidor.GestorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author marc mestres
 */
class GestorXifratTest {

    /**
     * Xifra i desxifra un text
     * El resultat de xifrar un text no por ser null
     * El text original i xifrat no poden ser iguals
     * El text original i xifrat i desxifrat han de ser iguals
     * 
     * Aquesta prova valida xifrar → desxifrar utilitzant AES en mode CBC.
    */
    @Test
    void xifrar_i_desxifrar_ok() throws Exception {
        String original = "Hola! això és una prova de xifrat AES CBC 123 äéíóú €";
        String xifrat = GestorXifrat.xifrar(original);
        String desxifrat = GestorXifrat.desxifrar(xifrat);

        assertNotNull(xifrat);
        assertNotEquals(original, xifrat, "El text xifrat no hauria de ser igual al pla");
        assertEquals(original, desxifrat, "Desxifrar ha de retornar el text original");
    }

    /**
     * Xifra un mateix text 2 vegades (2 variables)
     * Com que el vector d'inicialització y la key son constants
     * les dues variables xifrades han de ser iguals
    */
    @Test
    void xifrar_dos_cops_amb_mateix_text_ok() throws Exception {
        // KEY i IV constants -> mateix input => mateix output
        String original = "mateix missatge";
        String x1 = GestorXifrat.xifrar(original);
        String x2 = GestorXifrat.xifrar(original);

        assertEquals(x1, x2, "El xifrat ha de ser el mateix per al mateix text");
    }

    /**
     * Prova de xifrar un null
     * xifrar null ha de llençar GestorException
    */
    @Test
    void xifrar_null_llenca_gestorException() {
        assertThrows(GestorException.class, () -> GestorXifrat.xifrar(null));
    }

    /**
     * Prova de desxifrar un null
     * desxifrar null ha de llençar GestorException
    */
    @Test
    void desxifrar_null_llenca_gestorException() {
        assertThrows(GestorException.class, () -> GestorXifrat.desxifrar(null));
    }
}
