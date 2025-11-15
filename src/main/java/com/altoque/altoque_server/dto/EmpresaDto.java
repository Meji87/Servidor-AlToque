
package com.altoque.altoque_server.dto;

/**
 * Model DTO de la classe Empresa
 * @author marc mestres
 */
public class EmpresaDto {
    private String cif;
    private String nom;
    
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
    
}
