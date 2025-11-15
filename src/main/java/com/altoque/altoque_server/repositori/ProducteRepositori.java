package com.altoque.altoque_server.repositori;

import com.altoque.altoque_server.model.Producte;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//import java.util.Optional;

/**
 * Repositori JPA per l'entitat Producte
 * Proporciona operacions bàsiques de persistencia i consultes sobre Productes
 * 
 * @author marc mestres
 */
@Repository
public interface ProducteRepositori extends JpaRepository<Producte, Long> {
    /**
     * Retorna els productes associats a una empresa concreta
     * @param cif CIF de l'empresa propietaria dels productes
     * @return llista de productes de l'empresa (pot ser buida si no hi ha)
     */
    List<Producte> findByEmpresa_Cif(String cif);
    
    /**
     * Busca un producte pel seu nom
     * @param nom nom del producte
     * @return el Producte trobat o null si no existeix
     */
    Producte findByNom(String nom);
    
    //Optional<Producte> findById(Long id);
}
