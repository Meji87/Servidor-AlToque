
package com.altoque.altoque_server.dao;

import com.altoque.altoque_server.model.Empresa;
import com.altoque.altoque_server.repositori.EmpresaRepositori;
import com.altoque.altoque_server.servidor.GestorException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author marc mestres
 */
@Component
public class EmpresaDao {
    @Autowired
    private EmpresaRepositori empresaRepo;
    
    /**
     * Llista de totes les empreses registrades a la BBDD
     * @return 
     */
    public List<Empresa> llistarEmpreses(){
        return empresaRepo.findAll();
    }
    
    /**
     * Busca una empresa a partir del seu identificador
     * @param cif identificador de l'empresa a buscar
     * @return Empresa trobada, en cas contrari null
     */
    public Empresa buscarPerCif(String cif){
        return empresaRepo.findByCif(cif);
    }
    
    /**
     * Insereix una nova empresa a la base de dades
     *
     * @param empresa l'empresa a afegir
     * @throws GestorException si l'empresa ja existeix
     */
    public void inserirEmpresa(Empresa empresa) throws GestorException {
        if (!empresaRepo.existsById(empresa.getCif())) {
            empresaRepo.save(empresa);
        } else {
            throw new GestorException("L'empresa ja existeix");
        }
    }
    
    /**
     * Elimina una empresa de la base de dades
     *
     * @param cif el codi de l'empresa
     * @throws GestorException si l'nomUsuari no existeix
     */
    public void eliminarEmpresa(String cif) throws GestorException {
        if (empresaRepo.existsById(cif)) {
            empresaRepo.deleteById(cif);
        } else {
            throw new GestorException("L'empresa no existeix");
        }
    }
    
}
