package com.altoque.altoque_server.repositori;

import com.altoque.altoque_server.model.Producte;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author marc mestres
 */
@Repository
public interface ProducteRepositori extends JpaRepository<Producte, Long> {
    List<Producte> findByEmpresa_Cif(String cif);
    Producte findByNom(String nom);
    //Optional<Producte> findById(Long id);
}
