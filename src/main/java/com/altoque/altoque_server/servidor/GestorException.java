
package com.altoque.altoque_server.servidor;

/**
 *
 * @author marc mestres
 */

public class GestorException extends Exception {
    

    public GestorException(){
        super();
    }

    public GestorException(String message) {
        super(message);
    }

    public GestorException(String message, Throwable cause) {
        super(message, cause);
    }

    public GestorException(Throwable cause) {
        super(cause);
    }

    public GestorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}