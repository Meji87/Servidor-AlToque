package com.altoque.altoque_server.repositori;

import com.altoque.altoque_server.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositori JPA per l'entitat Empresa
 * Proporciona operacions bàsiques de persistencia i consultes sobre Empreses
 * 
 * @author marc mestres
 */
@Repository
public interface EmpresaRepositori extends JpaRepository<Empresa, String>{
    /**
     * Busca una empresa pel seu CIF
     * @param cif CIF de l'empresa a buscar
     * @return l'empresa trobada o null si no existeix
     */
    Empresa findByCif(String cif);
    
    //Optional<Empresa> finById(String cif);
}

