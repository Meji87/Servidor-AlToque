
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
@Table(name="pack_items")
public class PackItem {
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @ManyToOne(optional=false) 
    @JoinColumn(name = "pack_id", nullable=false)
    private Pack pack;
    
    @ManyToOne(optional=false) 
    @JoinColumn(name = "producte_id", nullable=false)
    private Producte producte;
    
    @Column(nullable=false) 
    private int quantitat;

    public PackItem() {
    }

    public PackItem(Pack pack, Producte producte, int quantitat) {
        this.pack = pack;
        this.producte = producte;
        this.quantitat = quantitat;
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public Pack getPack() {
        return pack;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public Producte getProducte() {
        return producte;
    }

    public void setProducte(Producte producte) {
        this.producte = producte;
    }

    public int getQuantitat() {
        return quantitat;
    }

    public void setQuantitat(int quantitat) {
        this.quantitat = quantitat;
    }
    
    
}