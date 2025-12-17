
package com.altoque.altoque_server.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Model DTO de la classe Empresa
 * @author marc mestres
 */
public class EmpresaDto {
    private String cif;
    private String nom;
    private String contrasenya;
    private List<ProducteDto> productes = new ArrayList<>();
    
    
    /**
     * Constructor per defecte
     */
    public EmpresaDto() {
    }

    /**
     * Constructor de EmpresaDTO
     * @param cif identificador de l'empresa
     * @param nom de l'empresa
     */
    public EmpresaDto(String cif, String nom) {
        this.cif = cif;
        this.nom = nom;
    }  
    
    /**
     * Constructor de EmpresaDTO
     * @param cif identificador de l'empresa
     * @param nom de l'empresa
     * @param contrasenya contrasenya de l'usuari de l'empresa
     */
    public EmpresaDto(String cif, String nom, String contrasenya) {
        this.cif = cif;
        this.nom = nom;
        this.contrasenya = contrasenya;
    }

    public String getCif() {
        return cif;
    }

    private void setCif(String cif) {
        this.cif = cif;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public List<ProducteDto> getProductes() {
        return productes;
    }

    public void setProductes(List<ProducteDto> productes) {
        this.productes = productes;
    }
    
    
    
    
    
}
