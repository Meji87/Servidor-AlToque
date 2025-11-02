
package com.altoque.altoque_server.servei;

import com.altoque.altoque_server.Const;
import com.altoque.altoque_server.dto.RespostaPeticio;
import com.altoque.altoque_server.dto.Peticio;
import com.altoque.altoque_server.model.Empresa;
import com.altoque.altoque_server.model.Usuari;
import com.altoque.altoque_server.servidor.GestorException;
import com.altoque.altoque_server.servidor.GestorSessions;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

/**
 * Punt central que gestiona les peticions.
 * Valida sessions i executa accions com el login o logout.
 * Manté dades de prova en memòria per poder treballar sense base de dades.
 * 
 * @author marc mestres
 */
@Component
public class GestorPeticions {
    
    private final GestorSessions sessions;
    private final GestorUsuari usuaris;
    private final GestorEmpresa empreses;
    
    /**
     * Crea el gestor i carrega dades de prova.
     * @param sessions instància compartida de sessions
     * @param usuaris  instància compartida d'usuaris
     * @param empreses instància compartida d'empreses
     */
    public GestorPeticions(GestorSessions sessions, GestorUsuari usuaris, GestorEmpresa empreses) {
        this.sessions = sessions;
        this.usuaris = usuaris;
        this.empreses = empreses;
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
     
        //RespostaPeticio resposta;
                
        String peticio = input.getPeticio();

        try{
            // Si tipusPeticio LOGIN_USUARI 
            if(peticio.equals(Const.Peticio.LOGIN_USUARI)){
                // Recuperem els parametres de la petició (nomUsuari i contrasenya)
                String nomusuari= RequereixParametre(input, 0, Const.Dades.NOM_USUARI); //(String) input.getData(0,String.class);
                String contrasenya = RequereixParametre(input, 1, Const.Dades.CONTRASENYA); //(String) input.getData(1,String.class);

                RespostaPeticio resposta = loginUsuari(nomusuari, contrasenya);
                return gson.toJson(resposta);

            // Si tipusPeticio LOGIN_EMPRESA 
            }else if(peticio.equals(Const.Peticio.LOGIN_EMPRESA)){
                // Recuperem els parametres de la petició (cif i contrasenya)
                String cif= RequereixParametre(input, 0, Const.Dades.CIF); //(String) input.getData(0,String.class);
                String contrasenya = RequereixParametre(input, 1, Const.Dades.CONTRASENYA); //(String) input.getData(1,String.class);

                RespostaPeticio resposta = loginEmpresa(cif, contrasenya);
                return gson.toJson(resposta);
            
            // Si tipusPeticio USUARI_ADD
            } else if (peticio.equals(Const.Peticio.USUARI_ADD)){
                String nomusuari= RequereixParametre(input, 0, Const.Dades.NOM_USUARI); 
                String contrasenya = RequereixParametre(input, 1, Const.Dades.CONTRASENYA);
                String nom = RequereixParametre(input, 2, Const.Dades.NOM);
                String cognoms = RequereixParametre(input, 3, Const.Dades.COGNOMS);
                
                RespostaPeticio resposta = afegirUsuari(nomusuari, contrasenya, nom, cognoms);
                return gson.toJson(resposta);
                
            // Si tipusPeticio USUARI_DEL
            } else if (peticio.equals(Const.Peticio.USUARI_DEL)){
                String token= RequereixParametre(input, 0, Const.Dades.TOKEN); 
                String nomusuari = RequereixParametre(input, 1, Const.Dades.NOM_USUARI);
                
                RespostaPeticio resposta = eliminarUsuari(nomusuari);
                return gson.toJson(resposta);
                
            // Si tipusPeticio EMPRESA_ADD
            } else if (peticio.equals(Const.Peticio.EMPRESA_ADD)){
                String cif = RequereixParametre(input, 0, Const.Dades.CIF);
                String contrasenya = RequereixParametre(input, 1, Const.Dades.CONTRASENYA);
                
                RespostaPeticio resposta = afegirEmpresa(cif, contrasenya);
                return gson.toJson(resposta);
                
            // Si tipusPeticio EMPRESA_DEL
            } else if (peticio.equals(Const.Peticio.EMPRESA_DEL)){
                String token= RequereixParametre(input, 0, Const.Dades.TOKEN); 
                String cif = RequereixParametre(input, 1, Const.Dades.CIF);
                
                RespostaPeticio resposta = eliminarEmpresa(cif);
                return gson.toJson(resposta);
                
            // Altres tipus tipusPeticio 
            }else{
                // Només LOGOUT necessita token. La resta: error directe.
                if (peticio.equals(Const.Peticio.LOGOUT)) {
                    // Llegim el token de forma segura
                    String token= RequereixParametre(input, 0, Const.Dades.TOKEN); 

                    RespostaPeticio resposta = logout(token);
                    return gson.toJson(resposta);
                    
                } else {
                    // Petició desconeguda:
                    RespostaPeticio resposta = new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_PETICIO_INEXISTENT);
                    return gson.toJson(resposta);
                }
            }
            
        } catch(GestorException ex){
            return gson.toJson(new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_DESCONEGUT + ex.getMessage()));
        }
    }
    
    private String RequereixParametre(Peticio input, int index, String nom) throws GestorException{
        Object r = input.getData(index, String.class);
        if (r == null || input.tamany() <= index){
            throw new GestorException(Const.Missatge.ERR_PARAMETRE_INEXISTENT + nom);
        }
        String s = (String)r;
        if (s.isBlank()){
            throw new GestorException(Const.Missatge.ERR_PARAMETRE_BUIT + nom);
        }
        return s;
    }
    
    private RespostaPeticio loginUsuari(String nomUsuari, String contrasenya) {
        Usuari u = usuaris.buscarPerNomUsuari(nomUsuari);
        if(u != null && u.getContrasenya().equals(contrasenya)){
            String sessioExisteix = sessions.buscarTokenUsuari(nomUsuari);
            if(sessioExisteix != null){
                sessions.borrarSessio(sessioExisteix);
            }
            String token = sessions.iniciarSessio(nomUsuari);
            RespostaPeticio r = new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_LOGIN_USUARI);
            r.addDataObject(token);
            return r;
        }
        
        return new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_LOGIN_USUARI);
    }
    
    private RespostaPeticio loginEmpresa(String cif, String contrasenya){
        Empresa e = empreses.buscarPerCif(cif);
        if (e !=null && e.getContrasenya().equals(contrasenya)) {
            String sessioExisteix = sessions.buscarTokenUsuari(cif);
            if(sessioExisteix != null){
                sessions.borrarSessio(sessioExisteix);
            }
            String token = sessions.iniciarSessio(cif);
            RespostaPeticio r = new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_LOGIN_EMPRESA);
            r.addDataObject(token);
            return r;
        }
        
        return new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_LOGIN_EMPRESA);
    }
    
    private RespostaPeticio logout(String token){
        RespostaPeticio r;
        // Validem el token a GestorSessions
        if (!sessions.esValida(token)) {
            r = new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_SESSIO_INVALIDA);
        } else {
            sessions.borrarSessio(token);
            r = new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_SESSIO_TANCADA);
        }
        return r;
    }
    
    private RespostaPeticio afegirUsuari(String nomusuari, String contrasenya, String nom, String cognoms) throws GestorException{
        try{
            // Comprobar que l'usuari no existeix
            Usuari usuariExisteix = usuaris.buscarPerNomUsuari(nomusuari);
            if (usuariExisteix != null){
                RespostaPeticio r = new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_USUARI_EXISTENT);
                return r;
            }

            // Si no existeix, crear un nou usuari
            Usuari u = new Usuari();
            u.setNomusuari(nomusuari);
            u.setContrasenya(contrasenya);
            u.setNom(nom);
            u.setCognoms(cognoms);

            // Afegir nou usuari a la BBDD
            usuaris.inserir(u);
            
        }catch(GestorException ge){
            return new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_USUARI_ADD + ge.getMessage());
        }catch(Exception e){
            return new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_USUARI_ADD + e.getMessage());
            //RespostaPeticio r = new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_USUARI_ADD + e.getMessage());
            //return r;
        }
        
        // Respondre petició
        RespostaPeticio r = new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_USUARI_ADD);
        return r;

        
        
    }
    
    private RespostaPeticio eliminarUsuari(String nomusuari){
        // Comprobar que l'usuari existeix
        Usuari usuariExisteix = usuaris.buscarPerNomUsuari(nomusuari);
        if (usuariExisteix == null){
            RespostaPeticio r = new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_USUARI_INEXISTENT);
            return r;
        }
        
        // Eliminar usuari
        try{
            usuaris.eliminar(nomusuari);
        }catch(Exception ex){
            RespostaPeticio r = new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_USUARI_DEL + ex.getMessage());
            return r;
        }
        
        // Respondre petició
        RespostaPeticio r = new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_USUARI_DEL);
        return r;
    }
    
    
    private RespostaPeticio afegirEmpresa(String cif, String contrasenya){
        // Comprobar que l'usuari no existeix
        Empresa empresaExisteix = empreses.buscarPerCif(cif);
        if (empresaExisteix != null){
            RespostaPeticio r = new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_EMPRESA_EXISTENT);
            return r;
        }
        
        // Si no existeix, crear nova empresa
        Empresa e = new Empresa();
        e.setCif(cif);
        e.setContrasenya(contrasenya);
        
        // Afegir nova empresa a la BBDD
        try{
            empreses.inserir(e);
        }catch(Exception ex){
            //throw new GestorException("No s'ha pogut crear l'usuari", e);
            RespostaPeticio r = new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_EMPRESA_ADD + ex.getMessage());
            return r;
        }
        
        // Respondre petició
        RespostaPeticio r = new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_EMPRESA_ADD);
        return r;
        
    }
    
    private RespostaPeticio eliminarEmpresa(String cif){
        // Comprobar que l'empresa existeix
        Empresa empresaExisteix = empreses.buscarPerCif(cif);
        if (empresaExisteix == null){
            RespostaPeticio r = new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_EMPRESA_EXISTENT);
            return r;
        }
        
        // Eliminar empresa
        try{
            empreses.eliminar(cif);
        }catch(Exception ex){
            RespostaPeticio r = new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, Const.Missatge.ERR_EMPRESA_DEL + ex.getMessage());
            return r;
        }
        
        // Respondre petició
        RespostaPeticio r = new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_EMPRESA_DEL);
        return r;
    }
}
