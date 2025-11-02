/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.altoque.altoque_server.servei;

import com.altoque.altoque_server.dao.EmpresaDao;
import com.altoque.altoque_server.model.Empresa;
import com.altoque.altoque_server.servidor.GestorException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author mejia
 */
@Component
public class GestorEmpresa {
    @Autowired
    EmpresaDao empresaDao;
    
    public Empresa buscarPerCif(String cif){
        return empresaDao.buscarPerCif(cif);
    }
    
    /**
     * Insereix un nova empresa a la base de dades
     *
     * @param empresa l'empresaa afegir
     * @throws GestorException si l'empresa ja existeix
     */
    public void inserir(Empresa empresa) throws GestorException {
        empresaDao.inserirEmpresa(empresa);
    }
    
    /**
     * Elimina una empresa de la base de dades
     *
     * @param cif el codi de l'empresa
     * @throws GestorException si l'empresa no existeix
     */
    public void eliminar(String cif) throws GestorException {
        empresaDao.eliminarEmpresa(cif);
    }
        
    public List<Empresa> llistar(){
        return empresaDao.llistarEmpreses();
    }
}
