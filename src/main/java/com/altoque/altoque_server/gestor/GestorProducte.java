
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
    
    /**
     * Busca un producte pel seu nom
     * 
     * @param nom del producte a buscar
     * @return Producte trobat, en cas contrari null
     */
    public Producte buscarPerNom(String nom){
        return producteDao.buscarPerNom(nom);
    }
    
     /**
     * Busca un producte pel seu identificador
     * 
     * @param id del producte a buscar
     * @return Producte trobat, en cas contrari null
     */   
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
     
    /**
     * Llista tots els productes de la BBDD
     * @return llista de Productes
     */
    public List<Producte> llistar(){
        return producteDao.llistar();
    }
    
     /**
      * Llista els productes d'una empresa 
      * 
      * @param cif identificador de l'empresa amb els productes a buscar
      * @return llista de Productes d'una empresa
      */ 
    public List<Producte> llistarPerEmpresa(String cif){
        return producteDao.llistarPerEmpresa(cif);
    }
         
}
