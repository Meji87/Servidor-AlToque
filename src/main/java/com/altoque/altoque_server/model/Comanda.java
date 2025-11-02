/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.altoque.altoque_server.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import static java.lang.System.Logger.Level.ALL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mejia
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
    
//    @Enumerated(EnumType.STRING) 
//    EstatComanda estat; // PENDENT, PAGADA, ENVIADA...
    
    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, orphanRemoval = true)
    List<LiniaComanda> linies = new ArrayList<>();
}


