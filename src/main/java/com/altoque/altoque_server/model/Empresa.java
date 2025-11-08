
package com.altoque.altoque_server.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa una empresa
 * @author marc mestres
 */
@Entity
@Table(name = "empreses")
public class Empresa {
    @Id
    @Column(unique = true)
    private String cif;
    
    @Column(nullable = false)
    private String contrasenya;
    
    @Column
    private String nom;
    
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Producte> productes = new ArrayList<>();

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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    
    public List<Producte> getProductes() {
        return productes;
    }

    public void setProductes(List<Producte> productes) {
        this.productes = productes;
    }
    
    public void afegirProducte(Producte p) {
    productes.add(p);
    p.setEmpresa(this);
}

    public void eliminarProducte(Producte p) {
        productes.remove(p);
        p.setEmpresa(null);
    }
}
