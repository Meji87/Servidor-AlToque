package com.altoque.altoque_server.repositori;

import com.altoque.altoque_server.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author marc mestres
 */
@Repository
public interface EmpresaRepositori extends JpaRepository<Empresa, String>{
    //Optional<Empresa> finById(String cif);
    Empresa findByCif(String cif);
    
}

