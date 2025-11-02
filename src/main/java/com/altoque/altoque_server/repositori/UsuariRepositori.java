
package com.altoque.altoque_server.repositori;

import com.altoque.altoque_server.model.Usuari;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mejia
 */
@Repository
public interface UsuariRepositori extends JpaRepository<Usuari, String>{
    
    Usuari findByNomusuari(String nomUsuari);
    
    Usuari findByNom(String nom);
    
}
