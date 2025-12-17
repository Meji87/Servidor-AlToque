
package com.altoque.altoque_server.repositori;

import com.altoque.altoque_server.model.Pack;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositori JPA per l'entitat Pack
 * Proporciona operacions bàsiques de persistencia i consultes sobre Packs
 * 
 * @author marc mestres
 */
@Repository
public interface PackRepositori extends JpaRepository<Pack, Long> {
    List<Pack> findByNom(String nom);
    List<Pack> findByEmpresa_Cif(String cif);
}
