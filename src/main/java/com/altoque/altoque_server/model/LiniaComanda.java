
package com.altoque.altoque_server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 *
 * @author marc mestres
 */
@Entity
public class LiniaComanda {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    Long id;
    
    @ManyToOne(optional=false) 
    Comanda comanda;
    
    @ManyToOne(optional=false) 
    Producte producte;
    
    @Column(nullable=false) 
    Integer quantitat;
    
    @Column(nullable=false) 
    long preuUnitari; 
}
