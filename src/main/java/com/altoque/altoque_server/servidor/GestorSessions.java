
package com.altoque.altoque_server.servidor;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Gestió de sessions en memòria.
 * Desa un token (UUID) aleatori associat al nom d'usuari i permet validar-lo.
 * 
 * @author marc mestres
 */
@Component
public class GestorSessions {
    private final Map<String, String> sessions = new ConcurrentHashMap<>();    
    
    /**
     * Inicia una sessió nova per a l'usuari i retorna el token.
     * @param usuari nom d'usuari
     * @return token únic per identificar la sessió
     */
    public String iniciarSessio(String usuari){
        String token = UUID.randomUUID().toString();
        sessions.put(token, usuari);
        return token;
    }
    
    /**
     * Indica si el token existeix en memoria.
     * @param token token rebut del client
     * @return retorna cert si el token existeix o fals si no existeix.
     */
    public boolean esValida(String token) {
        return sessions.containsKey(token);
    }

    /**
     * Cerca un usuari amb el token enviar per paràmetre
     * @param token token a buscar
     * @return retorna l'usuari del token passat per perametre si existeix.
     */
    public String usuariDe(String token) {
        return sessions.get(token); // pot ser null si no existeix
    }
    
    /**
     * Elimina el token de les sessions en memoria (logout).
     * @param token token a tancar
     */
    public void borrarSessio(String token) {
        sessions.remove(token); // pot ser null si no existeix
    }
    
    /**
     * Cerca el token actiu associat a un usuari.
     * @param usuari nom d'usuari a cercar
     * @return el token si l'usuari té una sessió oberta, en cas contrari null.
     * 
     */
    public String buscarTokenUsuari(String usuari) {
        for (Map.Entry<String, String> e : sessions.entrySet()) {
            String u = e.getValue();         
            if (u != null && u.equals(usuari)) {
                return e.getKey();           
            }
        }
        return null; // no hi ha sessió per a aquest usuari
    }

  
}

