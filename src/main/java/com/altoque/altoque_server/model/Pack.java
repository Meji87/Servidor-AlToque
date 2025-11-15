
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable=false) 
    private String nom;
    
    @Column(nullable=false) 
    private long preu;    
    @ManyToOne(optional=false) 
    @JoinColumn(name="empresa_cif")
    private Empresa empresa;
    
}


