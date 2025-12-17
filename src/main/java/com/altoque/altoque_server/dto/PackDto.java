
package com.altoque.altoque_server.dto;

import java.util.List;

/**
 *
 * @author marc mestres
 */
public class PackDto {
    private String id;        // null en PACK_ADD, obligatorio en PACK_MOD/PACK_DEL
    private String nom;
    private Long preu;        // o long si siempre viene
    private String empresaCif; // opcional, porque la empresa la sacas de la sesión
    private List<PackItemDto> items;

    public static class PackItemDto {
        private Long producteId;   // ojo: tu Producte usa long id
        private Integer quantitat;

        public PackItemDto() {}
        public Long getProducteId() { return producteId; }
        public void setProducteId(Long producteId) { this.producteId = producteId; }
        public Integer getQuantitat() { return quantitat; }
        public void setQuantitat(Integer quantitat) { this.quantitat = quantitat; }
    }

    public PackDto() {}
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Long getPreu() { return preu; }
    public void setPreu(Long preu) { this.preu = preu; }
    public String getEmpresaCif() { return empresaCif; }
    public void setEmpresaCif(String empresaCif) { this.empresaCif = empresaCif; }
    public List<PackItemDto> getItems() { return items; }
    public void setItems(List<PackItemDto> items) { this.items = items; }
}
