
package com.altoque.altoque_server.gestor;

import com.altoque.altoque_server.dao.PackDao;
import com.altoque.altoque_server.model.Pack;
import com.altoque.altoque_server.servidor.GestorException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author marc mestres
 */
@Service
public class GestorPack {
    @Autowired
    private PackDao packDao;
    
    /**
     * Busca un Pack pel seu nom
     * 
     * @param nom del Pack a buscar
     * @return Llista de Packs trobats, en cas contrari null
     */
    public List<Pack> buscarPerNom(String nom){
        return packDao.buscarPerNom(nom);
    }
    
    /**
     * Busca un Pack pel seu id
     * 
     * @param id del Pack a buscar
     * @return Llista de Packs trobats, en cas contrari null
     */
    public Pack buscarPerId(Long id){
        return packDao.buscarPerId(id);
    }
    
    /**
     * Afegeix un nou pack a la base de dades
     *
     * @param p Pack a afegir
     * @throws GestorException si el pack ja existeix
     */
    public Pack afegir(Pack p)throws GestorException{
        if (p == null) throw new GestorException("null");
        return packDao.crear(p);
    }
    
    /**
     * Elimina un pack de la base de dades
     *
     * @param idPack id del pack
     * @throws GestorException si el pack no existeix
     */
    public void eliminar(Long idPack) throws GestorException {
        packDao.eliminar(idPack);
    }
    
    public List<Pack> llistarPerEmpresa (String cif){
        return packDao.llistarPerEmpresa(cif);
    }
    
    public void modificarPack(Pack p) throws GestorException {
        packDao.modificarPack(p);
    }

}
