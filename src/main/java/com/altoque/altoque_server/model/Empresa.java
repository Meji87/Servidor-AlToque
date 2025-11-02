
package com.altoque.altoque_server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Classe que representa una empresa
 * @author marc mestres
 */
@Entity
@Table(name = "empreses")
public class Empresa {
    @Id
    @Column(unique = true)
    String cif;
    String contrasenya;

    public Empresa() {}
    
    /**
     * Contrueix una empresa espesificant el cif i la contrasenya.
     * 
     * @param cif nom d'usuari del usuari
     * @param contrasenya contrasenya de l'usuari
     */
    public Empresa(String cif, String contrasenya) {
        this.cif = cif;
        this.contrasenya = contrasenya;
    }



    
    /**
     * Retorna el valor de cif
     *
     * @return valor de cif
     */
    public String getCif() {
        return cif;
    }

    /**
     * Estableix el valor de cif
     *
     * @param cif nou valor de cif
     */
    public void setCif(String cif) {
        this.cif = cif;
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
