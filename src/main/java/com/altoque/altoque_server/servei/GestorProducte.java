
package com.altoque.altoque_server.servei;

import com.altoque.altoque_server.dao.ProducteDao;
import com.altoque.altoque_server.model.Producte;
import com.altoque.altoque_server.servidor.GestorException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mejia
 */
@Service
public class GestorProducte {
    @Autowired
    private ProducteDao producteDao;
    
    public Producte buscarPerNom(String nom){
        return producteDao.buscarPerNom(nom);
    }
    
    /**
     * Insereix un nou usuari a la base de dades
     *
     * @param producte l'usuari a afegir
     * @throws GestorException si l'usuari ja existeix
     */
    public void inserir(Producte producte) throws GestorException {
        producteDao.crear(producte);
    }
    
    /**
     * Elimina un usuari de la base de dades
     *
     * @param idProducte el codi de l'usuari
     * @throws GestorException si l'usuari no existeix
     */
    public void eliminar(String idProducte) throws GestorException {
        producteDao.eliminar(idProducte);
    }
        
    public List<Producte> llistar(){
        return producteDao.llistar();
    }
    
    public List<Producte> llistarPerEmpresa(String cif){
        return producteDao.llistarPerEmpresa(cif);
    }
}
