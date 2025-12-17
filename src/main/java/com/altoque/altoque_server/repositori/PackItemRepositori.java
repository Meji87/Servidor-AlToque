
package com.altoque.altoque_server.repositori;

import com.altoque.altoque_server.model.PackItem;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositori JPA per l'entitat PackItem
 * Proporciona operacions bàsiques de persistencia i consultes sobre PacksItem
 * 
 * @author marc mestres
 */
@Repository
public interface PackItemRepositori extends JpaRepository<PackItem, Long> {
    List<PackItem> findByPack_Id(Long idPack);
    List<PackItem> findByProducte_Id(Long idProducte);
    
    @Modifying
    @Transactional
    @Query("delete from PackItem pi where pi.pack.id = :packId")
     void deleteByPackId(@Param("packId") Long packId);
}
