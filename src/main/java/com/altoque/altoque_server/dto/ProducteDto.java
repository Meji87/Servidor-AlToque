package com.altoque.altoque_server.dto;

/**
 * Model DTO de la classe Producte
 * @author marc mestres
 */
public class ProducteDto {
    public Long id;
    public String nom;
    public String descripcio;
    public double preu;
    public String empresaCif;

    /**
     * Constructor per defecte
     */
    public ProducteDto() {}

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
}