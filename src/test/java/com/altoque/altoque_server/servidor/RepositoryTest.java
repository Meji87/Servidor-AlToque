
package com.altoque.altoque_server.servidor;

import com.altoque.altoque_server.model.Empresa;
import com.altoque.altoque_server.model.Usuari;
import com.altoque.altoque_server.repositori.EmpresaRepositori;
import com.altoque.altoque_server.repositori.UsuariRepositori;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author mejia
 */

//@DataJpaTest
//@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@DataJpaTest
class RepositoryTest {

  @Autowired UsuariRepositori usuariRepo;
  @Autowired EmpresaRepositori empresaRepo;
  
    @Test
    void crud_usuari_ok() {
        // CREATE
        var u = new Usuari();
        u.setNomusuari("Marc09");
        u.setNom("Marc");
        u.setCognoms("Mestres");
        u.setContrasenya("1234");
        usuariRepo.save(u);

        // READ por ID
        var byId = usuariRepo.findById("Marc09").orElseThrow();
        assertThat(byId.getNomusuari()).isEqualTo("Marc09");

        // READ por finder derivado
        var byNameList = usuariRepo.findByNomusuari("Marc09");
        assertThat(byNameList.getNomusuari()).isEqualTo("Marc09");

        // UPDATE (no toques la PK)
        //var saved = byNameList.getNomusuari() ;// get(0);
        byNameList.setCognoms("Mestres Actualitzat");
        usuariRepo.save(byNameList);

        var again = usuariRepo.findById("Marc09").orElseThrow();
        assertThat(again.getCognoms()).isEqualTo("Mestres Actualitzat");

        // DELETE
        usuariRepo.deleteById("Marc09");
        assertThat(usuariRepo.findById("Marc09")).isEmpty();
    }
    
    @Test
    void crud_empresa_ok() {
        // CREATE
        var e = new Empresa();
        e.setCif("00012-B");
        e.setContrasenya("1234");
        empresaRepo.save(e);

        // READ por ID
        var byId = empresaRepo.findById("00012-B").orElseThrow();
        assertThat(byId.getCif()).isEqualTo("00012-B");

        // READ por finder derivado
        var byNameList = empresaRepo.findByCif("00012-B");
        assertThat(byNameList.getCif()).isEqualTo("00012-B");

        // UPDATE (no toques la PK)
        //var saved = byNameList.get(0);
        byNameList.setContrasenya("xxx");
        empresaRepo.save(byNameList);

        var again = empresaRepo.findById("00012-B").orElseThrow();
        assertThat(again.getContrasenya()).isEqualTo("xxx");

        // DELETE
        empresaRepo.deleteById("00012-B");
        assertThat(empresaRepo.findById("00012-B")).isEmpty();
    }
}
