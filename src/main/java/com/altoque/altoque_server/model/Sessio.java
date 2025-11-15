
package com.altoque.altoque_server.model;

import com.altoque.altoque_server.Const.Rol;

/**
 * Classe que representa una sessio
 * 
 * @author marc mestres
 */
public final class Sessio {
    public final String token;          
    public final String nomUsuari;      
    public final Rol rol;            
    public final long minutsExpiracio;    

    /**
     * Constructor d'una Sessio
     * 
     * @param token identificador de la sessio
     * @param nomUsuari nomusuari vinculat a la sessio
     * @param rol rol de la sessio 
     * @param tempsExpira temps expiració sessio (60 minuts)
     */
    public Sessio(String token, String nomUsuari, Rol rol, long tempsExpira) {
        this.token = token;
        this.nomUsuari = nomUsuari;
        this.rol = rol;
        this.minutsExpiracio = tempsExpira;
    }

    /**
     * Retorna el token de la sessio
     * 
     * @return token de la sessio
     */
    public String getToken() {
        return token;
    }

    /**
     * Retorna el nomusuari de la sessio
     * 
     * @return nomusuari de la sessio
     */
    public String getNomUsuari() {
        return nomUsuari;
    }

    /**
     * Rol de la sessio (USUARI o EMPRESA)
     * 
     * @return rol de la sessio
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Retorna els minuts de vida assignats a la sessio
     * 
     * @return minuts actius de la sessio
     */
    public long getExpiraMinuts() {
        return minutsExpiracio;
    }

  
    /**
     * Retorna si la sessio és valida o no 
     * 
     * @return true si ha expirat, false en cas contrari
     */
    public boolean expirada() { return System.currentTimeMillis() > minutsExpiracio; }
}

