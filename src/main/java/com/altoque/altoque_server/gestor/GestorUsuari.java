package com.altoque.altoque_server.gestor;

import com.altoque.altoque_server.dao.UsuariDao;
import com.altoque.altoque_server.model.Usuari;
import com.altoque.altoque_server.servidor.GestorException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marc mestres
 */
@Component
public class GestorUsuari {
    
    @Autowired
    UsuariDao usuariDao;
    
    public Usuari buscarPerNomUsuari(String nomUsuari){
        return usuariDao.buscarPerNomusuari(nomUsuari);
    }
    
    public Usuari buscarPerNom(String nom){
        return usuariDao.buscarPerNom(nom);
    }
    
    /**
     * Insereix un nou usuari a la base de dades
     *
     * @param usuari l'usuari a afegir
     * @throws GestorException si l'usuari ja existeix
     */
    public void inserir(Usuari usuari) throws GestorException {
        usuariDao.inserirUsuari(usuari);
    }
    
    /**
     * Elimina un usuari de la base de dades
     *
     * @param nomUsuari el codi de l'usuari
     * @throws GestorException si l'usuari no existeix
     */
    public void eliminar(String nomUsuari) throws GestorException {
        usuariDao.eliminarUsuari(nomUsuari);
    }
        
    public List<Usuari> llistar(){
        return usuariDao.llistarUsuaris();
    }
}
