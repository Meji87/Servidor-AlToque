
package com.altoque.altoque_server.dto;

/**
 *
 * @author marc mestres
 */
public class EmpresaDto {
    private String cif;
    private String nom;
    
    public EmpresaDto() {
    }

    public EmpresaDto(String cif, String nom) {
        this.cif = cif;
        this.nom = nom;
    }  
    
}
