
package com.altoque.altoque_server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 *
 * @author marc mestres
 */
@Entity
@Table(name="pack_items")
public class PackItem {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(optional=false) 
    private Pack pack;
    
    @ManyToOne(optional=false) 
    private Producte producte;
    
    @Column(nullable=false) 
    private int quantitat;
}