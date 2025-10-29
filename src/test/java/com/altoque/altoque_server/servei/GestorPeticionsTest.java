
package com.altoque.altoque_server.servei;

import com.altoque.altoque_server.dto.Peticio;
import com.altoque.altoque_server.dto.RespostaPeticio;
import com.altoque.altoque_server.servidor.GestorSessions;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.altoque.altoque_server.servei.GestorPeticions.OK_RETURN_CODE;
import static com.altoque.altoque_server.servei.GestorPeticions.ERROR_RETURN_CODE;

/**
 *
 * @author marc mestres mejias
 */
public class GestorPeticionsTest {

    private final Gson gson = new Gson();
    
    /**
     * Login amb credencials correctes d'usuari, ha de retornar codi d'èxit.
     */
    @Test
    void loginUsuariOk(){
        GestorPeticions gestor = new GestorPeticions(new GestorSessions());
        // LOGIN_USUARI
        Peticio login = new Peticio("LOGIN_USUARI");
        login.addDataObject("M13");
        login.addDataObject("123");
        
        String jsonRespLogin = gestor.processar(gson.toJson(login));
        RespostaPeticio respLogin = gson.fromJson(jsonRespLogin, RespostaPeticio.class);
        
        // El login d'usuari hauria de ser correcte
        assertEquals(OK_RETURN_CODE, respLogin.getCodi());
        
    }
    
    /**
     * Login d'usuari amb contrasenya incorrecta ha de retornar codi d'error.
     */
    @Test
    void loginUsuariKo() {
        GestorPeticions gestor = new GestorPeticions(new GestorSessions());

        Peticio login = new Peticio("LOGIN_USUARI");
        login.addDataObject("M13");
        login.addDataObject("xxx");

        RespostaPeticio r = gson.fromJson(gestor.processar(gson.toJson(login)), RespostaPeticio.class);

        assertEquals(ERROR_RETURN_CODE, r.getCodi(), "Amb contrasenya incorrecta s'ha d'obtenir codi d'error");
    }
    
    /**
     * Login amb credencials correctes d'empresa, ha de retornar codi d'èxit.
     */
    @Test
    void loginEmpresaOk(){
        GestorPeticions gestor = new GestorPeticions(new GestorSessions());
        // LOGIN_EMPRESA
        Peticio login = new Peticio("LOGIN_EMPRESA");
        login.addDataObject("001");
        login.addDataObject("123");
        String jsonRespLogin = gestor.processar(gson.toJson(login));
        RespostaPeticio respLogin = gson.fromJson(jsonRespLogin, RespostaPeticio.class);

        assertEquals(OK_RETURN_CODE, respLogin.getCodi(), "El login d'empresa hauria de ser correcte");
        
    }
    
    /**
     * Login d'empresa amb contrasenya incorrecta ha de retornar codi d'error.
     */
    @Test
    void loginEmpresaKo() {
        GestorPeticions gestor = new GestorPeticions(new GestorSessions());

        Peticio login = new Peticio("LOGIN_EMPRESA");
        login.addDataObject("009");
        login.addDataObject("xxx"); // contrasenya incorrecta

        String jsonResp = gestor.processar(gson.toJson(login));
        RespostaPeticio resp = gson.fromJson(jsonResp, RespostaPeticio.class);

        assertEquals(ERROR_RETURN_CODE, resp.getCodi(), "Amb contrasenya incorrecta s'ha d'obtenir codi d'error");
    }
    
    /**
     * Logout amb un token vàlid ha de retornar codi d'èxit.
     */
    @Test
    void logoutUsuariOk() {
        GestorSessions sessions = new GestorSessions();
        GestorPeticions gestor = new GestorPeticions(sessions);

        // Login per obtenir token
        Peticio login = new Peticio("LOGIN_USUARI");
        login.addDataObject("M13");
        login.addDataObject("123");
        RespostaPeticio rLogin = gson.fromJson(gestor.processar(gson.toJson(login)), RespostaPeticio.class);

        String token = (String) rLogin.getData(0, String.class);
        assertNotNull(token);

        // Logout amb el token
        Peticio logout = new Peticio("LOGOUT");
        logout.addDataObject(token);
        RespostaPeticio rLogout = gson.fromJson(gestor.processar(gson.toJson(logout)), RespostaPeticio.class);

        assertEquals(OK_RETURN_CODE, rLogout.getCodi(), "El logout hauria de ser correcte");
    }
    
    /**
     * Logout d'empresa amb un token vàlid ha de retornar codi OK.
     */
    @Test
    void logoutEmpresaOk() {
        GestorSessions sessions = new GestorSessions();
        GestorPeticions gestor = new GestorPeticions(sessions);

        // Login per obtenir token
        Peticio login = new Peticio("LOGIN_EMPRESA");
        login.addDataObject("002");
        login.addDataObject("456");
        RespostaPeticio rLogin = gson.fromJson(gestor.processar(gson.toJson(login)), RespostaPeticio.class);

        String token = (String) rLogin.getData(0, String.class);
        assertNotNull(token);

        // Logout amb el token
        Peticio logout = new Peticio("LOGOUT");
        logout.addDataObject(token);
        RespostaPeticio rLogout = gson.fromJson(gestor.processar(gson.toJson(logout)), RespostaPeticio.class);

        assertEquals(OK_RETURN_CODE, rLogout.getCodi(), "El logout hauria de ser correcte");
    }

    /**
     * Logout amb un token invàlid ha de retornar codi d'error.
     */
    @Test
    void logoutTokenInvalid() {
        GestorPeticions gestor = new GestorPeticions(new GestorSessions());

        Peticio logout = new Peticio("LOGOUT");
        logout.addDataObject("TOKEN_FAKE");

        RespostaPeticio r = gson.fromJson(gestor.processar(gson.toJson(logout)), RespostaPeticio.class);

        assertEquals(ERROR_RETURN_CODE, r.getCodi(), "Amb token invàlid s'ha d'obtenir codi d'error");
    }
   
    /**
     * Enviament de petició invàlida ha de retornar codi d'error.
     */
    @Test
    void peticioInexistent(){
        GestorPeticions gestor = new GestorPeticions(new GestorSessions());
        
        Peticio p = new Peticio("INVENTADA");
        
        String jsonRespLogin = gestor.processar(gson.toJson(p));
        RespostaPeticio respLogin = gson.fromJson(jsonRespLogin, RespostaPeticio.class);
        
        assertEquals(ERROR_RETURN_CODE, respLogin.getCodi());
        
    }
}
