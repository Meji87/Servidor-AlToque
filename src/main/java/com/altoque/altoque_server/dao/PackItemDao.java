
package com.altoque.altoque_server.dao;

import com.altoque.altoque_server.model.PackItem;
import com.altoque.altoque_server.repositori.PackItemRepositori;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marc mestres
 */
@Component
public class PackItemDao {
    @Autowired
    private PackItemRepositori itemRepo;
    
    /**
     * Afageix un packItem a la BBDD
     * @param p packItem a afegir
     * @return si el packItem ja existeix
     */
    public PackItem crear(PackItem p){
        return itemRepo.save(p);
    }
    
    public List<PackItem> llistarPerPack(Long idPack){
        return itemRepo.findByPack_Id(idPack);
    }
    
    public List<PackItem> llistarPerEmpresa(Long idPack){
        return itemRepo.findByPack_Id(idPack);
    }
    
    public void eliminarPerPack(Long idPack){
        itemRepo.deleteByPackId(idPack);
    }
}
