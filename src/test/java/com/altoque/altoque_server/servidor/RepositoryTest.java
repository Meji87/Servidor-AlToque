
package com.altoque.altoque_server.servidor;

import com.altoque.altoque_server.model.Empresa;
import com.altoque.altoque_server.model.Producte;
import com.altoque.altoque_server.model.Usuari;
import com.altoque.altoque_server.repositori.EmpresaRepositori;
import com.altoque.altoque_server.repositori.PackItemRepositori;
import com.altoque.altoque_server.repositori.PackRepositori;
import com.altoque.altoque_server.repositori.ProducteRepositori;
import com.altoque.altoque_server.repositori.UsuariRepositori;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author marc mestres
 */

//@DataJpaTest
@SpringBootTest
@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryTest {

    @Autowired UsuariRepositori usuariRepo;
    @Autowired EmpresaRepositori empresaRepo;
    @Autowired ProducteRepositori prodycteRepo;
    @Autowired PackRepositori packRepo;
    @Autowired PackItemRepositori packItemRepo;
    
    
  
    @BeforeEach
    void setUp(){
        usuariRepo.deleteAll();
        empresaRepo.deleteAll();
        prodycteRepo.deleteAll();
    }
    
    @AfterEach
    void tearDown(){
        usuariRepo.deleteAll();
        empresaRepo.deleteAll();
        prodycteRepo.deleteAll();
    }
  
    /**
     * Comprova a crear un Usuari i desar-lo a la BBDD
     * Comprova que un usuari existeix tant per identificador com per nomusuari
     * Comprova que un usuari s'elimina correctament
     */
    @Test
    void crud_usuari_ok() {
        // CREATE
        Usuari u = new Usuari();
        u.setNomusuari("Marc09");
        u.setNom("Marc");
        u.setCognoms("Mestres");
        u.setContrasenya("1234");
        usuariRepo.save(u);

        // READ per identificador
        Usuari byId = usuariRepo.findById("Marc09").orElseThrow();
        assertThat(byId.getNomusuari()).isEqualTo("Marc09");

        // READ per nomusuari
        Usuari byNameList = usuariRepo.findByNomusuari("Marc09");
        assertThat(byNameList.getNomusuari()).isEqualTo("Marc09");

        // UPDATE

        // DELETE
        usuariRepo.deleteById("Marc09");
        assertThat(usuariRepo.findById("Marc09")).isEmpty();
    }
    
    /**
     * Comprova a crear una Empresa i desar-la a la BBDD
     * Comprova que una empresa existeix tant per identificador com per CIF
     * Comprova que una empresa s'elimina correctament
     */
    @Test
    void crud_empresa_ok() {
        // CREATE
        Empresa e = new Empresa();
        e.setCif("00012-B");
        e.setContrasenya("1234");
        empresaRepo.save(e);

        // READ per identificador
        Empresa byId = empresaRepo.findById("00012-B").orElseThrow();
        assertThat(byId.getCif()).isEqualTo("00012-B");

        // READ per CIF
        Empresa byNameList = empresaRepo.findByCif("00012-B");
        assertThat(byNameList.getCif()).isEqualTo("00012-B");

        // UPDATE

        // DELETE
        empresaRepo.deleteById("00012-B");
        assertThat(empresaRepo.findById("00012-B")).isEmpty();
    }
    
    /**
     * Comprova a crear un Producte i desar-lo a la BBDD
     * Comprova que un producte existeix tant pel seu nom com per empresa
     * Comprova que un producte s'elimina correctament
     */
    @Test
    void crud_producte_ok() {
        // CREATE EMPRESA (per poder vincular el producte)
        Empresa e = new Empresa();
        e.setCif("00012-B");
        e.setContrasenya("1234");
        e.setNom("EmpresaNova");
        empresaRepo.save(e);
        
        // CREATE PRODUCTE
        Producte p = new Producte();
        p.setNom("ProducteNou");
        p.setDescripcio("Nou producte en creació");
        p.setPreu(0.6);
        p.setEmpresa(e);
        prodycteRepo.save(p);

        // READ per identificador
        Producte byId = prodycteRepo.findByNom(p.getNom());
        assertThat(byId.getNom()).isEqualTo("ProducteNou");

        // READ per CIF
        List<Producte> byNameList = prodycteRepo.findByEmpresa_Cif(e.getCif());
        Producte res = byNameList.get(0);
        assertThat(res.getNom()).isEqualTo(p.getNom());
        assertThat(res.getDescripcio()).isEqualTo(p.getDescripcio());
        assertThat(res.getPreu()).isEqualTo(p.getPreu());
        assertThat((String)res.getEmpresa().getCif()).isEqualTo((String)p.getEmpresa().getCif());

        // UPDATE

        // DELETE
        prodycteRepo.deleteById(res.getId());
        assertThat(prodycteRepo.findById(p.getId())).isEmpty();
    }
}
