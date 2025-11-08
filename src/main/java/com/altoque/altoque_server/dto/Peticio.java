
package com.altoque.altoque_server.dto;

/**
 * Representa una petició completa enviada/desada com a JSON.
 * Inclou el nom/tipus de la petició (camp {@code peticio}) i,
 * a través de la superclasse {@link DadesPeticio}, la llista de dades.
 *
 * @author marc mestres
 * 
 * Exemple de JSON:
 * {
 *   "peticio": "LOGIN_USUARI",
 *   "data": ["M13","123"]
 * }
 */
public class Peticio extends DadesPeticio {
    private String peticio; 
    private String token;

    /**
     * Default constructor
     */
    public Peticio() {
        super();
    }
    

    /**
     * Crea una petició indicant el seu nom/tipus.
     * @param peticio identificador de la petició (p. ex. "LOGIN_USUARI")
     */
    public Peticio(String peticio) {
        super();
        this.peticio = peticio;
    }
    
    /**
     * Crea una petició indicant el seu nom/tipus.
     * @param peticio identificador de la petició (p. ex. "LOGIN_USUARI")
     */
    public Peticio(String peticio, String token) {
        super();
        this.peticio = peticio;
        this.token = token;
    }

    /**
     * @return el nom/tipus de la petició (p. ex. "LOGIN_USUARI")
     */
    public String getPeticio() {
        return peticio;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    
}
