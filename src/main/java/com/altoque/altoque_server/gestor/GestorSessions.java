
package com.altoque.altoque_server.gestor;

import com.altoque.altoque_server.Const;
import com.altoque.altoque_server.Const.Rol;
import com.altoque.altoque_server.model.Sessio;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * Gestió de sessions en memòria.
 * Desa un token (UUID) aleatori associat a una Sessió.
 * 
 * @author marc mestres
 */
@Component
public class GestorSessions {
    private final Map<String, Sessio> sessions = new ConcurrentHashMap<>();
    private final long minutsExpira = Const.Sessio.MINUTS_EXPIRACIO;
    
    /**
     * Inicia i retorna una nova Sessio crada. Afageix a sessions la parella token-Sessio
     * @param usuari nom d'usuari/cif vinculat a la Sessió
     * @param rol rol assosiat a la sessio a crear
     * @return Sessio creada 
     */
    public Sessio iniciarSessio(String usuari, Rol rol){
        String token = UUID.randomUUID().toString();
        long expira = System.currentTimeMillis() + (minutsExpira * (60 * 1000));
        Sessio s = new Sessio(token, usuari, rol, expira);
        
        sessions.put(token, s);
        return s;
    }
    
    /**
     * Indica si el token passat per paràmetre correspón a una sessió vàlida.
     * @param token token associat a la Sessió a validar
     * @return retorna cert si la Sessió es troba activa en cas contrari false.
     */
    public boolean esValida(String token) {
        Sessio s = sessions.get(token);
        if (s == null || s.expirada()){
            sessions.remove(token);
            return false;
        }
        return true;
    }
    
    /**
     * Busca una sessió vinculada amb el token passat per paràmetre.
     * @param token token associat a la sessió.
     * @return Sessio trobada / null si no s'ha trobat
     */
    public Sessio buscarSessio(String token){
        return sessions.get(token);
    }
    
    /**
     * Elimina la sessió vinculada amb el token passat per paràmetre.
     * @param token token vinculat a la sessió a tancar
     */
    public void tancarSessio(String token) {
        sessions.remove(token);
    }
    
}

