
package com.altoque.altoque_server.gestor;

import com.altoque.altoque_server.dao.PackItemDao;
import com.altoque.altoque_server.model.PackItem;
import com.altoque.altoque_server.servidor.GestorException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author marc mestres
 */
@Service
public class GestorPackItem {
    @Autowired
    private PackItemDao itemDao;
    
    /**
     * Afegeix un nou pack a la base de dades
     *
     * @param p Pack a afegir
     * @throws GestorException si el pack ja existeix
     */
    public PackItem afegir(PackItem p)throws GestorException{
        if (p == null) throw new GestorException("null");
        return itemDao.crear(p);
    }
    
    /**
     * Llistar tots els items d'un Pack
     * 
     * @param idPack id del Pack a llistar
     * @return Llista dels items d'un Pack trobat, en cas contrari null
     */
    public List<PackItem> llistarPerPack(Long idPack){
        return itemDao.llistarPerPack(idPack);
    }
    
    /**
     * Elimina tots els items d'un pack
     * 
     * @param idPack id del Pack a llistar
     */
    public void eliminarPerPack(Long idPack)throws GestorException{
        itemDao.eliminarPerPack(idPack);
    }
    
}
