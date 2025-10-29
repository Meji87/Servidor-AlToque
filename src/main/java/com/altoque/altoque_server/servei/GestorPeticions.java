
package com.altoque.altoque_server.servei;

import com.altoque.altoque_server.dto.RespostaPeticio;
import com.altoque.altoque_server.dto.Peticio;
import com.altoque.altoque_server.model.Empresa;
import com.altoque.altoque_server.model.Usuari;
import com.altoque.altoque_server.servidor.GestorSessions;
import com.google.gson.Gson;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Punt central que gestiona les peticions.
 * Valida sessions i executa accions com el login o logout.
 * Manté dades de prova en memòria per poder treballar sense base de dades.
 * 
 * @author marc mestres
 */
public class GestorPeticions {
    
    private final GestorSessions gestorSessions;
    // dades de prova en memòria
    private final Map<String, Usuari> usuaris = new ConcurrentHashMap<>();
    private final Map<String, Empresa> empreses = new ConcurrentHashMap<>();
    
    /**
     * Crea el gestor i carrega dades de prova.
     * @param sessions instància compartida de sessions
     */
    public GestorPeticions(GestorSessions sessions) {
        this.gestorSessions = sessions;
        carregarDadesProva();
    }

    // nom de les peticions
    public static final String LOGIN_USUARI = "LOGIN_USUARI";
    public static final String LOGIN_EMPRESA = "LOGIN_EMPRESA";
    public static final String LOGOUT = "LOGOUT";
    public static final String USUARI_ADD = "USUARI_ADD";
    public static final String USUARI_DEL = "USUARI_DEL";
    public static final String EMPRESA_ADD = "EMPRESA_ADD";
    public static final String EMPRESA_DEL = "EMPRESA_DEL";
    
    // codis
    public static final int OK_RETURN_CODE = 0;
    public static final int ERROR_RETURN_CODE = 1; 
    
    
    //private boolean isLoggedIn = false; 
    
    /**
     * Carrega usuaris i empreses de mostra en memòria.
     */
    private void carregarDadesProva(){
        //Usuaris
        usuaris.put("M13", new Usuari("M13", "123"));
        usuaris.put("M01", new Usuari("M01", "456"));
        usuaris.put("M02", new Usuari("M02", "789"));
        
        //Empreses
        empreses.put("001", new Empresa("001", "123"));
        empreses.put("002", new Empresa("002", "456"));
        empreses.put("003", new Empresa("003", "789"));
    }

    /**
     * Rep un missatge JSON, l'interpreta i retorna la resposta JSON.
     * Si la petició és de login, comprova credencials; altre cas valida el token.
     * @param tipusPeticio JSON rebut del client
     * @return JSON amb la resposta
     */
    public String processar (String tipusPeticio){

        Gson gson = new Gson();
        Peticio input = gson.fromJson(tipusPeticio, Peticio.class);
     
        RespostaPeticio resposta;
                
        String peticio = input.getPeticio();

        // Si tipusPeticio LOGIN_USUARI 
        if(peticio.equals(LOGIN_USUARI)){

            // Recuperem els parametres de la petició (nomUsuari i contrasenya)
            String nomusuari= (String) input.getData(0,String.class);
            String contrasenya = (String) input.getData(1,String.class);
            
            // Buscar l'usuari de la petició a les dades
            Usuari usuari = usuaris.get(nomusuari);
            
            // Si les credencials de la petició coincideixen amb les de la BBDD
            if(usuari != null && usuari.getContrasenya().equals(contrasenya)){
                String token = gestorSessions.iniciarSessio(nomusuari); 
                resposta = new RespostaPeticio(OK_RETURN_CODE, "OK: Usuari connectat correctament");
                resposta.addDataObject(token); //SESSION_CODE
            // Si les credencials de la petició NO coincideixen amb les de la BBDD
            }else {
                resposta = new RespostaPeticio(ERROR_RETURN_CODE,"ERROR: Accés usuari denegat!");
            }
        
        // Si tipusPeticio LOGIN_EMPRESA 
        }else if(peticio.equals(LOGIN_EMPRESA)){
            
            // Recuperem els parametres de la petició (cif i contrasenya)
            String cif= (String) input.getData(0,String.class);
            String contrasenya = (String) input.getData(1,String.class);
            
            // Buscar l'empresa de la petició a les dades
            Empresa empresa = empreses.get(cif);
            
            // Si les credencials de la petició coincideixen amb les de la BBDD
            if(empresa != null && empresa.getContrasenya().equals(contrasenya)){
                String token = gestorSessions.iniciarSessio(cif); 
                resposta = new RespostaPeticio(OK_RETURN_CODE, "OK: Empresa connectada correctament");
                resposta.addDataObject(token);
            // Si les credencials de la petició NO coincideixen amb les de la BBDD
            }else {
                resposta = new RespostaPeticio(ERROR_RETURN_CODE,"ERROR: Accés empresa denegat!");
            }
        
        // Altres tipus tipusPeticio 
        }else{ 
            
            // Només LOGOUT necessita token. La resta: error directe.
            if (peticio.equals(LOGOUT)) {
                // Llegim el token de forma segura
                String token;
                try {
                    token = (String)input.getData(0, String.class);
                } catch (IndexOutOfBoundsException e) {
                    // No han enviat token
                    resposta = new RespostaPeticio(ERROR_RETURN_CODE, "Dades insuficients");
                    return gson.toJson(resposta);
                }

                // Validem el token a GestorSessions
                if (!gestorSessions.esValida(token)) {
                    resposta = new RespostaPeticio(ERROR_RETURN_CODE, "Sessió no vàlida");
                } else {
                    gestorSessions.borrarSessio(token);
                    resposta = new RespostaPeticio(OK_RETURN_CODE, "Sessió tancada correctament");
                }

            } else {
                // Petició desconeguda:
                resposta = new RespostaPeticio(ERROR_RETURN_CODE, "Tipus petició no suportat");
            }

            
//            
//            // Recuperem el parametres de la petició (token)
//            String token= (String) input.getData(0,String.class);
//            
//            // Comprovar que el token enviat existeix a les sessions, si no és vàlid, enviar resposta d'error
//            if(!gestorSessions.esValida(token)){ //sessionNumber!=SESSION_CODE
//                   resposta = new RespostaPeticio(ERROR_RETURN_CODE,"Sessió no vàlida"); 
//                   
//            // Sessió existeix, comprovar tipus de peticio per fer una acció o altre
//            }else if(peticio.equals(LOGOUT)){
//                gestorSessions.borrarSessio(token);
//                resposta = new RespostaPeticio(OK_RETURN_CODE, "Sessió tencada correctament");
//            
//            // La sessió existeix però no s'ha trobat el tipus de petició
//            }else{
//                resposta = new RespostaPeticio(ERROR_RETURN_CODE, "Tipus peticio no suportat");            
//            }
        }
        // Retorna un JSON amb la resposta
       return gson.toJson(resposta);

    }
}
