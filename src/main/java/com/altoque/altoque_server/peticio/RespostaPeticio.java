package com.altoque.altoque_server.peticio;

/**
 * Resposta del servidor a una petició.
 * Afegeix un codi d'estat i un missatge, i pot incloure dades
 * 
 * @author marc mestres
 *
 * Exemple d'èxit:
 * {
 *   "codi": 0,
 *   "missatge": "Usuari connectat",
 *   "data": ["TOKEN"]
 * }
 *
 * Exemple d'error:
 * {
 *   "codi": 1,
 *   "missatge": "Credencials incorrectes",
 *   "data": []
 * }
 */
public class RespostaPeticio extends DadesPeticio{
    private int codi;
    private String missatge;
    
    /**
     * Constructor per defecte. Crea una resposta buida.
     */
    public RespostaPeticio() {
        super();
    }
    
    /**
     * Crea una resposta amb codi i missatge.
     * @param codi codi d'estat (0 Ok, 1 Error)
     * @param missatge descripció breu
     */
    public RespostaPeticio(int codi, String missatge) {
        super();
        this.codi = codi;
        this.missatge = missatge;
    }
    
    /**
     * Retorna el valor de codi
     *
     * @return valor de codi
     */
    public int getCodi() {
        return codi;
    }

    /**
     * Estableix el valor de codi
     *
     * @param codi nou valor de codi
     */
    public void setCodi(int codi) {
        this.codi = codi;
    }

    /**
     * Retorna el valor de missatge
     *
     * @return valor de missatge
     */
    public String getMissatge() {
        return missatge;
    }

    /**
     * Estableix el valor de missatge
     *
     * @param missatge nou valor de missatge
     */
    public void setMissatge(String missatge) {
        this.missatge = missatge;
    }
    
}
