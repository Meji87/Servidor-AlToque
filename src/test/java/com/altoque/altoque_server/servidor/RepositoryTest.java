
package com.altoque.altoque_server.servidor;

import com.altoque.altoque_server.model.Empresa;
import com.altoque.altoque_server.model.Usuari;
import com.altoque.altoque_server.repositori.EmpresaRepositori;
import com.altoque.altoque_server.repositori.UsuariRepositori;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 *
 * @author mejia
 */

//@DataJpaTest
@SpringBootTest
@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryTest {

    @Autowired UsuariRepositori usuariRepo;
    @Autowired EmpresaRepositori empresaRepo;
  
    @BeforeEach
    void setUp(){
        usuariRepo.deleteAll();
        empresaRepo.deleteAll();
    }
    
    @AfterEach
    void tearDown(){
        usuariRepo.deleteAll();
        empresaRepo.deleteAll();
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

        // UPDATE (no toques la PK)

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
}
