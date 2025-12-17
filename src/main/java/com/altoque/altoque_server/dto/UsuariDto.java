
package com.altoque.altoque_server.dto;

/**
 *
 * @author marc mestres mejias
 */
public class UsuariDto {
    private String nomusuari;
    private String nom;
    private String cognoms;
    private String contrasenya;

    public UsuariDto() {
    }

    public UsuariDto(String nomusuari, String nom, String cognoms, String contrasenya) {
        this.nomusuari = nomusuari;
        this.nom = nom;
        this.cognoms = cognoms;
        this.contrasenya = contrasenya;
    }

    public String getNomusuari() {
        return nomusuari;
    }

    public void setNomusuari(String nomusuari) {
        this.nomusuari = nomusuari;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognoms() {
        return cognoms;
    }

    public void setCognoms(String cognoms) {
        this.cognoms = cognoms;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }
    
    
}

