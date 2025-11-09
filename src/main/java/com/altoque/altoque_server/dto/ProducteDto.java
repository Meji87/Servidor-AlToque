package com.altoque.altoque_server.dto;

/**
 *
 * @author marc mestres
 */
public class ProducteDto {
    public Long id;
    public String nom;
    public String descripcio;
    public double preu;
    public String empresaCif;

    public ProducteDto() {}

    public ProducteDto(Long id, String nom, String descripcio, Double preu, String empresaCif) {
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
        this.preu = preu;
        this.empresaCif = empresaCif;
    }
}