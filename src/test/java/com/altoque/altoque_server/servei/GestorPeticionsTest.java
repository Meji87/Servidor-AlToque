
package com.altoque.altoque_server.servei;

import com.altoque.altoque_server.Const;
import static com.altoque.altoque_server.Const.Resposta.ERROR_RETURN_CODE;
import static com.altoque.altoque_server.Const.Resposta.OK_RETURN_CODE;
import com.altoque.altoque_server.dto.Peticio;
import com.altoque.altoque_server.dto.RespostaPeticio;
import com.altoque.altoque_server.repositori.EmpresaRepositori;
import com.altoque.altoque_server.repositori.UsuariRepositori;
import com.altoque.altoque_server.servidor.GestorSessions;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



//@DataJpaTest
//@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

/**
 *
 * @author marc mestres mejias
 */
@SpringBootTest
public class GestorPeticionsTest {
    
    @Autowired
    private GestorPeticions gestor;
    @Autowired
    private UsuariRepositori usuariRepo;
    @Autowired
    private EmpresaRepositori empresaRepo;

    private final Gson gson = new Gson();
    
    private RespostaPeticio call(Peticio p){
        String jsonReq = gson.toJson(p);
        String jsonRes = gestor.processar(jsonReq);
        return gson.fromJson(jsonRes, RespostaPeticio.class);
    }
    
    @BeforeEach
    void setUp(){
        // estado conocido antes de cada prueba
        usuariRepo.deleteAll();
        empresaRepo.deleteAll();
    }
    
    @AfterEach
    void tearDown(){
        // dejarla vacia despues
        usuariRepo.deleteAll();
        empresaRepo.deleteAll();
    }
    
    /**
     * Login amb credencials correctes d'usuari, ha de retornar codi d'èxit.
     */
    @Test
    void loginUsuariOk(){
        //GestorPeticions gestor = new GestorPeticions(new GestorSessions());
        // AFEGIR_USUARI
        Peticio alta = new Peticio(Const.Peticio.USUARI_ADD);
        alta.addDataObject("M13");
        alta.addDataObject("123");
        alta.addDataObject("Marc");
        alta.addDataObject("Mestres");
        call(alta);
        
        // LOGIN_USUARI
        Peticio login = new Peticio(Const.Peticio.LOGIN_USUARI);
        login.addDataObject("M13");
        login.addDataObject("123");
        RespostaPeticio r = call(login);
        
        // El login d'usuari hauria de ser correcte
        assertEquals(OK_RETURN_CODE, r.getCodi());
        
    }
    
    /**
     * Login d'usuari amb contrasenya incorrecta ha de retornar codi d'error.
     */
    @Test
    void loginUsuariKo() {
        //GestorPeticions gestor = new GestorPeticions(new GestorSessions());

        Peticio login = new Peticio(Const.Peticio.LOGIN_USUARI);
        login.addDataObject("M13");
        login.addDataObject("falsa");

        RespostaPeticio r = call(login); //gson.fromJson(gestor.processar(gson.toJson(login)), RespostaPeticio.class);

        assertEquals(ERROR_RETURN_CODE, r.getCodi(), "Amb contrasenya incorrecta s'ha d'obtenir codi d'error");
    }
    
    /**
     * Login amb credencials correctes d'empresa, ha de retornar codi d'èxit.
     */
    @Test
    void loginEmpresaOk(){
        //GestorPeticions gestor = new GestorPeticions(new GestorSessions());
        // AFEGIR_EMPRESA
        Peticio alta = new Peticio(Const.Peticio.EMPRESA_ADD);
        alta.addDataObject("001");
        alta.addDataObject("123");
        call(alta);
        
        // LOGIN_EMPRESA
        Peticio login = new Peticio(Const.Peticio.LOGIN_EMPRESA);
        login.addDataObject("001");
        login.addDataObject("123");
        
        RespostaPeticio r = call(login);
        
//        String jsonRespLogin = gestor.processar(gson.toJson(login));
//        RespostaPeticio respLogin = gson.fromJson(jsonRespLogin, RespostaPeticio.class);

        assertEquals(OK_RETURN_CODE, r.getCodi(), "El login d'empresa hauria de ser correcte");
        
    }
    
    /**
     * Login d'empresa amb contrasenya incorrecta ha de retornar codi d'error.
     */
    @Test
    void loginEmpresaKo() {
        //GestorPeticions gestor = new GestorPeticions(new GestorSessions());

        Peticio login = new Peticio(Const.Peticio.LOGIN_EMPRESA);
        login.addDataObject("009");
        login.addDataObject("xxx"); // contrasenya incorrecta

        //String jsonResp = gestor.processar(gson.toJson(login));
        RespostaPeticio resp = call(login); //gson.fromJson(jsonResp, RespostaPeticio.class);

        assertEquals(ERROR_RETURN_CODE, resp.getCodi(), "Amb contrasenya incorrecta s'ha d'obtenir codi d'error");
    }
    
    /**
     * Logout amb un token vàlid ha de retornar codi d'èxit.
     */
    @Test
    void logoutUsuariOk() {
        // ALTA_USUARI
        Peticio alta = new Peticio(Const.Peticio.USUARI_ADD);
        alta.addDataObject("Marc09");
        alta.addDataObject("xxx");
        alta.addDataObject("Marc");
        alta.addDataObject("Mestres");
        call(alta);
        
        // LOGIN_USUARI
        Peticio login = new Peticio(Const.Peticio.LOGIN_USUARI);
        login.addDataObject("Marc09");
        login.addDataObject("xxx");
        RespostaPeticio rLogin = call(login);
        String token = (String)rLogin.getData(0, String.class);
        assertNotNull(token);

        // LOGOUT amb el token
        Peticio logout = new Peticio(Const.Peticio.LOGOUT);
        logout.addDataObject(token);
        RespostaPeticio rLogout = call(logout);
        
        //RespostaPeticio rLogout = gson.fromJson(gestor.processar(gson.toJson(logout)), RespostaPeticio.class);

        assertEquals(OK_RETURN_CODE, rLogout.getCodi(), "El logout hauria de ser correcte");
    }
    
    /**
     * Logout d'empresa amb un token vàlid ha de retornar codi OK.
     */
    @Test
    void logoutEmpresaOk() {
        // ALTA_EMPRESA
        Peticio alta = new Peticio(Const.Peticio.EMPRESA_ADD);
        alta.addDataObject("001");
        alta.addDataObject("123");
        call(alta);

        // LOGIN_EMPRESA
        Peticio login = new Peticio(Const.Peticio.LOGIN_EMPRESA);
        login.addDataObject("001");
        login.addDataObject("123");
        RespostaPeticio rLogin = call(login);
        String token = (String) rLogin.getData(0, String.class);
        assertNotNull(token);

        // LOGOUT amb el token
        Peticio logout = new Peticio(Const.Peticio.LOGOUT);
        logout.addDataObject(token);
        RespostaPeticio rLogout = call(logout); //gson.fromJson(gestor.processar(gson.toJson(logout)), RespostaPeticio.class);

        assertEquals(OK_RETURN_CODE, rLogout.getCodi(), "El logout hauria de ser correcte");
    }

    /**
     * Logout amb un token invàlid ha de retornar codi d'error.
     */
    @Test
    void logoutTokenInvalid() {
        //GestorPeticions gestor = new GestorPeticions(new GestorSessions());

        Peticio logout = new Peticio(Const.Peticio.LOGOUT);
        logout.addDataObject("TOKEN_FAKE");

        RespostaPeticio r = call(logout); //gson.fromJson(gestor.processar(gson.toJson(logout)), RespostaPeticio.class);

        assertEquals(ERROR_RETURN_CODE, r.getCodi(), "Amb token invàlid s'ha d'obtenir codi d'error");
    }
   
    /**
     * Enviament de petició invàlida ha de retornar codi d'error.
     */
    @Test
    void peticioInexistent(){
        //GestorPeticions gestor = new GestorPeticions(new GestorSessions());
        
        Peticio p = new Peticio("INVENTADA");
        
        //String jsonRespLogin = gestor.processar(gson.toJson(p));
        RespostaPeticio respLogin = call(p); //gson.fromJson(jsonRespLogin, RespostaPeticio.class);
        
        assertEquals(ERROR_RETURN_CODE, respLogin.getCodi());
        
    }
}
