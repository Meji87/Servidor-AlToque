/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.altoque.altoque_server.repositori;

import com.altoque.altoque_server.model.Producte;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mejia
 */
@Repository
public interface ProducteRepositori extends JpaRepository<Producte, String> {
    List<Producte> findByEmpresa_Cif(String cif);
    Producte findByNom(String nom);
}
