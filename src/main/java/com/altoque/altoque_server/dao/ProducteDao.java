
package com.altoque.altoque_server.dao;

import com.altoque.altoque_server.model.Producte;
import com.altoque.altoque_server.repositori.ProducteRepositori;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marc mestres
 */
@Component
public class ProducteDao {
    @Autowired
    private ProducteRepositori producteRepo;
    
    /**
     * Afageix un producte a la BBDD
     * @param p Producte a afegir
     * @return si el producte ja existeix
     */
    public Producte crear(Producte p){
        return producteRepo.save(p);
    }
    
    /**
     * Llista tots els productes de la BBDD
     * @return llista de productes
     */
    public List<Producte> llistar(){
        return producteRepo.findAll();
    }
    
    /**
     * Llista tots els productes de la BBDD que partanyin a l'empresa amb identificador passat per paràmetre
     * @param cif identificador de l'empresa 
     * @return llista de productes de l'empresa corresponent
     */
    public List<Producte> llistarPerEmpresa(String cif){
        return producteRepo.findByEmpresa_Cif(cif);
    }
    
    /**
     * Elimina un producte de la BBDD
     * @param id identificador del producte a eliminar
     */
    public void eliminar(Long id){
        producteRepo.deleteById(id);
    }
    
    /**
     * Busca un producte a la BBDD 
     * @param nom nom del producte a buscar
     * @return Producte trobat, cas contrari null
     */
    public Producte buscarPerNom(String nom){
        return producteRepo.findByNom(nom);           
    }
    
    /**
     * Busca un producte a la BBDD
     * @param id identificador del producte a buscar
     * @return Producte trobat, cas contrari null
     */
    public Producte buscarPerId(long id){
        return producteRepo.findById(id).orElse(null);
    }
}
