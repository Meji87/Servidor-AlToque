
package com.altoque.altoque_server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author marc mestres
 */
@Entity
@Table(name = "productes")
public class Producte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column
    private String descripcio;
    
    @Column(nullable = false)
    private long preu;
    
    @ManyToOne(fetch = FetchType.LAZY, optional=false)
    @JoinColumn(name = "empresa_cif", nullable=false) // Usa el @Id de Empresa (cif)
    private Empresa empresa;
    
    // Requerid per JPA
    public Producte() {}
    
    public Producte(String nom, String descripcio, long preu, Empresa empresa) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.preu = preu;
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    // Privat per no exposar-lo
    private void setId(Long id) {
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

    public long getPreu() {
        return preu;
    }

    public void setPreu(long preu) {
        this.preu = preu;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    
    
}
