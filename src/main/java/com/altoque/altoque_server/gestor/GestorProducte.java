
package com.altoque.altoque_server.gestor;

import com.altoque.altoque_server.dao.ProducteDao;
import com.altoque.altoque_server.model.Producte;
import com.altoque.altoque_server.servidor.GestorException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author marc mestres
 */
@Service
public class GestorProducte {
    @Autowired
    private ProducteDao producteDao;
    
    public Producte buscarPerNom(String nom){
        return producteDao.buscarPerNom(nom);
    }
    
    public Producte buscarPerId(long id){
        return producteDao.buscarPerId(id);
    }
    
    /**
     * Insereix un nou usuari a la base de dades
     *
     * @param producte l'usuari a afegir
     * @throws GestorException si l'usuari ja existeix
     */
    public Producte inserir(Producte producte) throws GestorException {
        return producteDao.crear(producte);
    }
    
    /**
     * Elimina un usuari de la base de dades
     *
     * @param idProducte el codi de l'usuari
     * @throws GestorException si l'usuari no existeix
     */
    public void eliminar(Long idProducte) throws GestorException {
        producteDao.eliminar(idProducte);
    }
        
    public List<Producte> llistar(){
        return producteDao.llistar();
    }
    
    public List<Producte> llistarPerEmpresa(String cif){
        return producteDao.llistarPerEmpresa(cif);
    }
}
