
package com.altoque.altoque_server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Classe que representa un usuari
 * 
 * @author marc mestres
 */
@Entity
@Table(name = "usuaris")
public class Usuari {
    
    //private String id;
    @Id
    @Column(unique = true)
    private String nomusuari;
    private String nom;
    private String cognoms;
    private String contrasenya;
    

    public Usuari() {}
    
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

    public Usuari(String nomusuari, String nom, String cognoms, String contrasenya) {
        this.nomusuari = nomusuari;
        this.nom = nom;
        this.cognoms = cognoms;
        this.contrasenya = contrasenya;
    }
    
    
    /**
     * Retorna el valor de nomUsuari
     *
     * @return valor de nomUsuari
     */
    public String getNomusuari() {
        return nomusuari;
    }

    /**
     * Estableix el valor de nomUsuari
     *
     * @param nomusuari  nou valor de nomUsuari
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
