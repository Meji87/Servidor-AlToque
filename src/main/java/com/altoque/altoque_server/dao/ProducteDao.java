/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.altoque.altoque_server.dao;

import com.altoque.altoque_server.Const;
import com.altoque.altoque_server.model.Producte;
import com.altoque.altoque_server.repositori.ProducteRepositori;
import com.altoque.altoque_server.servidor.GestorException;
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
    
    public Producte crear(Producte p){
        return producteRepo.save(p);
    }
    
    public List<Producte> llistar(){
        return producteRepo.findAll();
    }
    
    public List<Producte> llistarPerEmpresa(String cif){
        return producteRepo.findByEmpresa_Cif(cif);
    }
    
    public void eliminar(Long id){
        producteRepo.deleteById(id);
    }
    
    public Producte buscarPerNom(String nom){
        return producteRepo.findByNom(nom);           
    }
    
    public Producte buscarPerId(long id){
        return producteRepo.findById(id).orElse(null);
    }
}
