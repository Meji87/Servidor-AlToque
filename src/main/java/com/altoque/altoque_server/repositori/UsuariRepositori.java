package com.altoque.altoque_server.repositori;

import com.altoque.altoque_server.model.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositori JPA per l'entitat Usuari
 * Proporciona operacions bàsiques de persistencia i consultes sobre Usuaris
 * 
 * @author marc mestres
 */
@Repository
public interface UsuariRepositori extends JpaRepository<Usuari, String>{
    /**
     * Busca un usuari pel seu nom d'usuari únic
     * @param nomUsuari nom d'usuari que es vol buscar
     * @return l'usuari trobat o null si no existeix
     */
    Usuari findByNomusuari(String nomUsuari);
    
    /**
     * Busca un usuari pel seu nom real
     * @param nom nom real de l'usuari
     * @return l'usuari trobat o null si no existeix
     */
    Usuari findByNom(String nom);
    
}
