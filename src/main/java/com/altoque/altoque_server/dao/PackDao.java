
package com.altoque.altoque_server.dao;

import com.altoque.altoque_server.model.Pack;
import com.altoque.altoque_server.repositori.PackRepositori;
import com.altoque.altoque_server.servidor.GestorException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marc mestres
 */
@Component
public class PackDao {
    @Autowired
    private PackRepositori packRepo;
    
    /**
     * Afageix un pack a la BBDD
     * @param p Pack a afegir
     * @return si el Pack ja existeix
     */
    public Pack crear(Pack p){
        return packRepo.save(p);
    }
    
    /**
     * Llista tots els Packs de la BBDD
     * @return llista de Packs
     */
    public List<Pack> llistar(){
        return packRepo.findAll();
    }
    
    /**
     * Llista tots els Packs de la BBDD que partanyin a l'empresa amb identificador passat per paràmetre
     * @param cif identificador de l'empresa 
     * @return llista de Packs de l'empresa corresponent
     */
    public List<Pack> llistarPerEmpresa(String cif){
        return packRepo.findByEmpresa_Cif(cif);
    }    
    
    /**
     * Elimina un Pack de la BBDD
     * @param id identificador del Pack a eliminar
     */
    public void eliminar(Long id){
        packRepo.deleteById(id);
    }
    
    /**
     * Busca un producte a la BBDD 
     * @param nom nom del producte a buscar
     * @return Producte trobat, cas contrari null
     */
    public List<Pack> buscarPerNom(String nom){
        return packRepo.findByNom(nom);           
    }
    
    /**
     * Busca un producte a la BBDD
     * @param id identificador del producte a buscar
     * @return Producte trobat, cas contrari null
     */
    public Pack buscarPerId(long id){
        return packRepo.findById(id).orElse(null);
    }
    
    public void modificarPack(Pack p) throws GestorException{
        if (packRepo.existsById(p.getId())){
            packRepo.save(p);
        } else {
            throw new GestorException("El pack no existeix");
        }
    }
    
}
