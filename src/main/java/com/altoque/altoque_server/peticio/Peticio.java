
package com.altoque.altoque_server.peticio;

/**
 * Representa una petició completa enviada/desada com a JSON.
 *
 * @author marc mestres
 * 
 * Exemple de JSON:
 * {
 *   "peticio": "LOGIN_USUARI",
 *   "token": "0087-3345"
 *   "data": ["M13","123"]
 * }
 */
public class Peticio extends DadesPeticio {
    private String peticio; 
    private String token;

    /**
     * Constructor per defecte
     */
    public Peticio() {
        super();
    }
    

    /**
     * Crea una petició indicant el seu nom/tipus.
     * @param peticio identificador de la petició (p. ex. "LOGIN")
     */
    public Peticio(String peticio) {
        super();
        this.peticio = peticio;
    }
    
    /**
     * Crea una petició indicant el seu nom/tipus i un token associat a una Sessió.
     * @param peticio identificador de la petició (p. ex. "LOGOUT")
     * @param token token vinculat a una Sessio
     * 
     */
    public Peticio(String peticio, String token) {
        super();
        this.peticio = peticio;
        this.token = token;
    }

    /**
     * Obté el paràmetre peticio de la petició
     * @return el nom/tipus de la petició (p. ex. "LOGIN_USUARI")
     */
    public String getPeticio() {
        return peticio;
    }

    /**
     * Obté el paràmetre token de la peticio
     * @return 
     */
    public String getToken() {
        return token;
    }

//    public void setToken(String token) {
//        this.token = token;
//    }
    
    
}
