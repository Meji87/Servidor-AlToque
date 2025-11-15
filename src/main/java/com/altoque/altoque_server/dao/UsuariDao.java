
package com.altoque.altoque_server.dao;

import com.altoque.altoque_server.model.Usuari;
import com.altoque.altoque_server.repositori.UsuariRepositori;
import com.altoque.altoque_server.servidor.GestorException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marc mestres
 */
@Component
public class UsuariDao {
    @Autowired
    private UsuariRepositori usuariRepo;
    
    /**
     * Llista dels usuaris de la BBDD
     * @return llista d'Usuaris
     */
    public List<Usuari> llistarUsuaris(){
        return usuariRepo.findAll();
    }
    
    /**
     * Busca un Usuari a partir del seu nomusuari
     * @param nomUsuari de l'Usuari a buscar
     * @return Usuari trobat, en cas contrari null
     */
    public Usuari buscarPerNomusuari(String nomUsuari){
        return usuariRepo.findByNomusuari(nomUsuari);
    }
  
    /**
     * Busca un Usuari a partir del seu nom
     * @param nom de l'Usuari a buscar
     * @return Usuari trobat, en cas contrari null
     */
    public Usuari buscarPerNom(String nom){
        return usuariRepo.findByNom(nom);
    }
    
    /**
     * Insereix un nou usuari a la base de dades
     *
     * @param usuari l'usuari a afegir
     * @throws GestorException si l'usuari ja existeix
     */
    public void inserirUsuari(Usuari usuari) throws GestorException {
        if (!usuariRepo.existsById(usuari.getNomusuari())) {
            usuariRepo.save(usuari);
        } else {
            throw new GestorException("L'usuari ja existeix");
        }
    }
    
    /**
     * Elimina un usuari de la base de dades
     *
     * @param nomUsuari el codi de l'empresa
     * @throws GestorException si l'nomUsuari no existeix
     */
    public void eliminarUsuari(String nomUsuari) throws GestorException {
        if (usuariRepo.existsById(nomUsuari)) {
            usuariRepo.deleteById(nomUsuari);
        } else {
            throw new GestorException("L'usuari no existeix");
        }
    }
}
