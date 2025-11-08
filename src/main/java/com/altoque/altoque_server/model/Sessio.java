
package com.altoque.altoque_server.model;

import com.altoque.altoque_server.Const.Rol;

/**
 *
 * @author marc mestres
 */
public final class Sessio {
  public final String token;          
  public final String nomUsuari;      
  public final Rol rol;            
  public final long minutsExpiracio;    

    public Sessio(String token, String nomUsuari, Rol rol, long tempsExpira) {
        this.token = token;
        this.nomUsuari = nomUsuari;
        this.rol = rol;
        this.minutsExpiracio = tempsExpira;
    }

    public String getToken() {
        return token;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public Rol getRol() {
        return rol;
    }

    public long getExpiraEpochMs() {
        return minutsExpiracio;
    }

  
  public boolean expirada() { return System.currentTimeMillis() > minutsExpiracio; }
}

