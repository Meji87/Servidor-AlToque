
package com.altoque.altoque_server.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marc mestres
 */

@Entity
public class Comanda {
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY) 
    Long id;
    
    @ManyToOne(optional=false) 
    Usuari usuari;
    
    @Column(nullable=false) 
    Instant creadaEl;
    
    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, orphanRemoval = true)
    List<LiniaComanda> linies = new ArrayList<>();
}


