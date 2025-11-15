
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
 * Classe que representa un producte
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
    private double preu;
    
    @ManyToOne(fetch = FetchType.LAZY, optional=false)
    @JoinColumn(name = "empresa_cif", nullable=false) // Usa el @Id de Empresa (cif)
    private Empresa empresa;
    
    /**
     * Constructor per defecte, requerid per JPA
     */
    public Producte() {}
    
    /**
     * Constructor de Producte
     * @param nom nom del producte
     * @param descripcio descripció del producte 
     * @param preu preu del producte
     * @param empresa identificador (cif) de l'empresa del producte
     */
    public Producte(String nom, String descripcio, double preu, Empresa empresa) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.preu = preu;
        this.empresa = empresa;
    }

    /**
     * Obté l'identificador del producte
     * @return identificador del producte
     */
    public Long getId() {
        return id;
    }

    // Privat per no exposar-lo
    private void setId(Long id) {
        this.id = id;
    }

    /**
     * Obté el nom del Producte
     * 
     * @return nom del producte
     */
    public String getNom() {
        return nom;
    }

    /**
     * Estableix el nom del producte
     * 
     * @param nom nou valor nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Obté la descripció del producte
     * 
     * @return descripció del producte
     */
    public String getDescripcio() {
        return descripcio;
    }

    /**
     * Estableix la descripció del producte
     * 
     * @param descripcio nou valor de la descripció del producte
     */
    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    /**
     * Obté el preu del producte
     * 
     * @return preu del producte
     */
    public double getPreu() {
        return preu;
    }

    /**
     * Estableix el preu del producte
     * 
     * @param preu nou valor del preu del producte
     */
    public void setPreu(double preu) {
        this.preu = preu;
    }

    /**
     * Obté l'empresa a la que pertany el producte
     * 
     * @return empresa vinculada al producte
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * Estableix la empresa a la qual pertany el producte
     * 
     * @param empresa nova empresa a la que pertany el producte
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
}
