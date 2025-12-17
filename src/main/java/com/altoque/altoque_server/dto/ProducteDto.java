package com.altoque.altoque_server.dto;

/**
 * Model DTO de la classe Producte
 * @author marc mestres
 */
public class ProducteDto {
    public Long id;
    public String nom;
    public String descripcio;
    public Double preu;
    public String empresaCif;

    /**
     * Constructor per defecte
     */
    public ProducteDto() {}

    public ProducteDto(Long id, String nom, String descripcio, Double preu) {
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
        this.preu = preu;
    }
    
    

    /**
     * Constructor de ProducteDto
     * @param id identificador del producte
     * @param nom nom del producte
     * @param descripcio descripció del producte
     * @param preu preu del producte
     * @param empresaCif identificador de l'empresa del producte
     */
    public ProducteDto(Long id, String nom, String descripcio, Double preu, String empresaCif) {
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
        this.preu = preu;
        this.empresaCif = empresaCif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Double getPreu() {
        return preu;
    }

    public void setPreu(Double preu) {
        this.preu = preu;
    }

    public String getEmpresaCif() {
        return empresaCif;
    }

    public void setEmpresaCif(String empresaCif) {
        this.empresaCif = empresaCif;
    }
    
    
}