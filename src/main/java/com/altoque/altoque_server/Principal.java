
package com.altoque.altoque_server;

import com.altoque.altoque_server.servidor.ServidorAlToque;

/**
 * Punt d'entrada de l'aplicació.
 * Arrenca el servidor AlToque al port 5050.
 * 
 * * @author marc mestres
 */
public class Principal {
    public static void main(String[] args) {
        
        String ip = "localhost";
        int port = 5050;
        
        for (String a : args) {
            if (a.startsWith("--ip=")) ip = a.substring(5);
            if (a.startsWith("--port=")) port = Integer.parseInt(a.substring(7));
        }
        
        ServidorAlToque s = new ServidorAlToque(ip, port);
        s.iniciar();
    }
    
}
