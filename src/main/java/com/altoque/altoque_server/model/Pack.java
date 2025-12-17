
package com.altoque.altoque_server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="packs")
public class Pack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false) 
    private String nom;
    
    @Column(nullable=false) 
    private long preu;    
    @ManyToOne(optional=false) 
    @JoinColumn(name="empresa_cif")
    private Empresa empresa;

    public Pack() {
    }
    
    public Pack(String nom, long preu, Empresa empresa) {
        this.nom = nom;
        this.preu = preu;
        this.empresa = empresa;
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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


