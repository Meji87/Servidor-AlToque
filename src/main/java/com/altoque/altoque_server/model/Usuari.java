
package com.altoque.altoque_server.model;

/**
 * Classe que representa un usuari
 * @author marc mestres
 */
public class Usuari {
    private String id;
    private String nomusuari;
    private String nom;
    private String cognoms;
    private String contrasenya;
    

    /**
     * Contrueix un usuari espesificant el nom d'usuari i la contrasenya.
     * 
     * @param nomusuari nom d'usuari del usuari
     * @param contrasenya contrasenya de l'usuari
     */
    public Usuari(String nomusuari, String contrasenya) {
        this.nomusuari = nomusuari;
        this.contrasenya = contrasenya;
    }

    /**
     * Retorna el valor de nomusuari
     *
     * @return valor de nomusuari
     */
    public String getNomusuari() {
        return nomusuari;
    }

    /**
     * Estableix el valor de nomusuari
     *
     * @param nomusuari  nou valor de nomusuari
     */
    public void setNomusuari(String nomusuari) {
        this.nomusuari = nomusuari;
    }

    /**
     * Retorna el valor de la variable nom de l'usuari
     *
     * @return valor de nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Estableix el valor de nom
     *
     * @param nom  nou valor de nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retorna el valor de cognoms
     *
     * @return valor de cognoms
     */
    public String getCognoms() {
        return cognoms;
    }

    /**
     * Estableix el valor de cognoms
     *
     * @param cognoms  nou valor de cognoms
     */
    public void setCognoms(String cognoms) {
        this.cognoms = cognoms;
    }

    /**
     * Retorna el valor de contrasenya
     *
     * @return valor de contrasenya
     */
    public String getContrasenya() {
        return contrasenya;
    }

    /**
     * Estableix el valor de contrasenya
     *
     * @param contrasenya nou valor de contrasenya
     */
    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }   
}
