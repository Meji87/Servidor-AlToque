
package com.altoque.altoque_server.servidor;

/**
 * Excepció pròpia del servidor AlToque.
 * 
 * S'utilitza per encapsular els errors que es produeixen a les capes de gestor i que s'han de
 * propagar de forma controlada fins al punt on es construeix la resposta al client.
 * 
 * @author marc mestres
 */

public class GestorException extends Exception {

    /**
     * Crea una nova GestorException sense cap missatge ni causa.
     * Útil quan només volem indicar que s'ha produït un error genèric.
     */
    public GestorException(){
        super();
    }

    /**
     * Crea una nova GestorException amb un missatge descriptiu.
     * @param message missatge que descriu l'error produït
     */
    public GestorException(String message) {
        super(message);
    }

    /**
     * Crea una nova GestorException amb un missatge i una causa
     * @param message missatge que descriu l'error produït
     * @param cause excepció original que ha causat l'error
     */
    public GestorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Crea una nova GestorException només amb la causa
     * @param cause excepció original que ha causat l'error
     */
    public GestorException(Throwable cause) {
        super(cause);
    }
    
}