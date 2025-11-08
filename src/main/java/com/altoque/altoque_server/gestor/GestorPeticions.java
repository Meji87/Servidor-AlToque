
package com.altoque.altoque_server.gestor;

import com.altoque.altoque_server.Const;
import com.altoque.altoque_server.Const.Rol;
import com.altoque.altoque_server.dto.EmpresaDto;
import com.altoque.altoque_server.dto.RespostaPeticio;
import com.altoque.altoque_server.dto.Peticio;
import com.altoque.altoque_server.dto.ProducteDto;
import com.altoque.altoque_server.model.Empresa;
import com.altoque.altoque_server.model.Producte;
import com.altoque.altoque_server.model.Sessio;
import com.altoque.altoque_server.model.Usuari;
import com.altoque.altoque_server.servidor.GestorException;
import com.google.gson.Gson;
import java.util.List;
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
    private final GestorProducte productes;
    private Gson gson = new Gson();
    
    /**
     * Crea el gestor i carrega dades de prova.
     * @param sessions instància compartida de sessions
     * @param usuaris  instància compartida d'usuaris
     * @param empreses instància compartida d'empreses
     * @param productes  instància compartida de productes
     */
    public GestorPeticions(GestorSessions sessions, GestorUsuari usuaris, GestorEmpresa empreses, GestorProducte productes) {
        this.sessions = sessions;
        this.usuaris = usuaris;
        this.empreses = empreses;
        this.productes = productes;
    }
    
    /**
     * Rep un missatge JSON, l'interpreta i retorna la resposta JSON.
     * Si la petició és de login, comprova credencials; altre cas valida el token.
     * @param peticioJson JSON rebut del client
     * @return JSON amb la resposta
     */
    public String processar (String peticioJson){

        try{
            Peticio p = gson.fromJson(peticioJson, Peticio.class);
            RespostaPeticio r = router(p);
            return gson.toJson(r);
            
        } catch (GestorException ge){
            return gson.toJson(new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, ge.getMessage()));
        }
    }
    
    private RespostaPeticio router(Peticio p) throws GestorException {
        final String op = p.getPeticio();
        switch (op) {
            case Const.Peticio.LOGOUT:
                return logout(p);
                
            case Const.Peticio.LOGIN:
                return login(p);
                
            case Const.Peticio.USUARI_ADD:
                return afegirUsuari(p);
                
            case Const.Peticio.USUARI_DEL:
                return eliminarUsuari(p);
                
            case Const.Peticio.EMPRESA_ADD:
                return afegirEmpresa(p);
                
            case Const.Peticio.EMPRESA_DEL: 
                return eliminarEmpresa(p);
                
            case Const.Peticio.EMPRESA_LLISTAR:
                return llistarEmpreses(p);

            case Const.Peticio.PRODUCTE_ADD:
                return afegirProducte(p);

            case Const.Peticio.PRODUCTE_LLISTAR:
                return llistarProductes(p);

            
            default:
                throw new GestorException(Const.Missatge.ERR_PETICIO_INEXISTENT + op);
        }
    }
    
    private RespostaPeticio login(Peticio p) throws GestorException{
        // Validar dades
        if (p == null) throw new GestorException(Const.Missatge.ERR_PETICIO_INEXISTENT);
        if (p.esDataBuit()) throw new GestorException(Const.Missatge.ERR_PARAMETRE_BUIT);
        if (p.sizeData() < 2) throw new GestorException(Const.Missatge.ERR_PARAMETRE_INEXISTENT); 

        String identificador = (String)p.get(0, String.class); 
        String contrasenya   = (String)p.get(1, String.class);

        // 1) Probar com USUARI
        Usuari u = usuaris.buscarPerNomUsuari(identificador);
        if (u != null && contrasenya != null && contrasenya.equals(u.getContrasenya())) {
            Sessio s = sessions.iniciarSessio(identificador, Rol.USUARI); // guardamos el sujeto
            RespostaPeticio r = new RespostaPeticio(Const.Resposta.OK_RETURN_CODE,
                                                    Const.Missatge.OK_LOGIN_USUARI);
            r.add(s.token);
            r.add(Rol.USUARI.name());  
            r.add(u.getNom());        
            return r;
        }

        // 2) Probar com EMPRESA
        Empresa e = empreses.buscarPerCif(identificador);
        if (e != null && contrasenya != null && contrasenya.equals(e.getContrasenya())) {
            Sessio s = sessions.iniciarSessio(identificador, Rol.EMPRESA);
            RespostaPeticio r = new RespostaPeticio(Const.Resposta.OK_RETURN_CODE,
                                                    Const.Missatge.OK_LOGIN_EMPRESA);
            r.add(s.token);
            r.add(Rol.EMPRESA.name());         
            r.add(e.getCif());        
            return r;
        }

        // 3) Error de credencials
        return new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE,
                                   Const.Missatge.ERR_LOGIN_USUARI);
    }
    
    private RespostaPeticio logout(Peticio p) throws GestorException{
        // Validar dades
        if (p == null) throw new GestorException(Const.Missatge.ERR_PETICIO_INEXISTENT);
        
        if (!sessions.esValida(p.getToken())) throw new GestorException(Const.Missatge.ERR_SESSIO_INVALIDA);
        sessions.tancarSessio(p.getToken());
        
        return new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_SESSIO_TANCADA);
    }

    private RespostaPeticio afegirUsuari(Peticio p) throws GestorException {
        // Validar dades
        if (p == null) throw new GestorException(Const.Missatge.ERR_PETICIO_INEXISTENT);
        if (p.esDataBuit()) throw new GestorException(Const.Missatge.ERR_PARAMETRE_BUIT);
        if (p.sizeData() < 4) throw new GestorException(Const.Missatge.ERR_PARAMETRE_INEXISTENT); 
        //if (!sessions.esValida(p.getToken())) throw new GestorException(Const.Missatge.ERR_SESSIO_INVALIDA);
        
        String nomusuari = (String)p.get(0, String.class);
        String contrasenya = (String)p.get(1, String.class);
        String nom = (String)p.get(2, String.class);
        String cognoms = (String)p.get(3, String.class);

        if (usuaris.buscarPerNomUsuari(nomusuari) != null) {
            throw new GestorException(Const.Missatge.ERR_USUARI_EXISTENT);
        }

        Usuari u = new Usuari();
        u.setNomusuari(nomusuari);
        u.setContrasenya(contrasenya);
        u.setNom(nom);
        u.setCognoms(cognoms);
        usuaris.inserir(u);

        return new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_USUARI_ADD);
    }
    
    private RespostaPeticio eliminarUsuari(Peticio p) throws GestorException{
        // Validar dades
        if (p == null) throw new GestorException(Const.Missatge.ERR_PETICIO_INEXISTENT);
        if (!sessions.esValida(p.getToken())) throw new GestorException(Const.Missatge.ERR_SESSIO_INVALIDA);
        
        Sessio s = sessions.buscarSessio(p.getToken());
        
        // Comprobar que l'usuari existeix
        Usuari usuariExisteix = usuaris.buscarPerNomUsuari(s.nomUsuari);
        
        if (usuariExisteix == null)throw new GestorException(Const.Missatge.ERR_USUARI_INEXISTENT);
        
        usuaris.eliminar(s.nomUsuari);
        sessions.tancarSessio(p.getToken());
        
        return new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_USUARI_DEL);
    }
    
    private RespostaPeticio afegirEmpresa(Peticio p) throws GestorException {
        // Validar dades
        if (p == null) throw new GestorException(Const.Missatge.ERR_PETICIO_INEXISTENT);
        if (p.esDataBuit()) throw new GestorException(Const.Missatge.ERR_PARAMETRE_BUIT);
        if (p.sizeData() < 2) throw new GestorException(Const.Missatge.ERR_PARAMETRE_INEXISTENT);
        
        String cif = (String)p.get(0, String.class);
        String contrasenya = (String)p.get(1, String.class);

        if (empreses.buscarPerCif(cif) != null) {
            throw new GestorException(Const.Missatge.ERR_EMPRESA_EXISTENT);
        }

        Empresa e = new Empresa();
        e.setCif(cif);
        e.setContrasenya(contrasenya);
        empreses.inserir(e);

        return new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_EMPRESA_ADD);
    }
    
    private RespostaPeticio eliminarEmpresa(Peticio p) throws GestorException{
        // Validar dades
        if (p == null) throw new GestorException(Const.Missatge.ERR_PETICIO_INEXISTENT);
        if (!sessions.esValida(p.getToken())) throw new GestorException(Const.Missatge.ERR_SESSIO_INVALIDA);
        
        Sessio s = sessions.buscarSessio(p.getToken());
        
        // Comprobar que l'usuari existeix
        Empresa empresaExisteix = empreses.buscarPerCif(s.nomUsuari);
        if (empresaExisteix == null)throw new GestorException(Const.Missatge.ERR_EMPRESA_INEXISTENT);
        
        empreses.eliminar(s.nomUsuari);
        sessions.tancarSessio(p.getToken());
        
        return new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_EMPRESA_DEL);
    }
    
    private RespostaPeticio llistarEmpreses(Peticio p) throws GestorException{
        // Validar dades
        if (p == null) throw new GestorException(Const.Missatge.ERR_PETICIO_INEXISTENT);
        if (!sessions.esValida(p.getToken())) throw new GestorException(Const.Missatge.ERR_SESSIO_INVALIDA);
        
        List<Empresa> llista = empreses.llistar();
        
        // —— pasar llista a dto ——
        EmpresaDto[] dto = new EmpresaDto[llista.size()];
        for (int i = 0; i < llista.size(); i++) {
            Empresa emp = llista.get(i);
            dto[i] = new EmpresaDto(
                emp.getCif(),
                emp.getNom()
            );
        }
        
        RespostaPeticio r = new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, "");
        r.add(dto);
        return r; 
    }

    private RespostaPeticio afegirProducte(Peticio p) throws GestorException {
        if (p == null) throw new GestorException(Const.Missatge.ERR_PETICIO_INEXISTENT);
        if (p.esDataBuit()) throw new GestorException(Const.Missatge.ERR_PARAMETRE_BUIT);
        if (p.sizeData() < 3) throw new GestorException(Const.Missatge.ERR_PARAMETRE_INEXISTENT); 
        if (!sessions.esValida(p.getToken())) throw new GestorException(Const.Missatge.ERR_SESSIO_INVALIDA);
        
        String nom = (String)p.get(0, String.class);
        String desc = (String)p.get(1, String.class);
        String preu = (String)p.get(2, String.class);
        
        Sessio s = sessions.buscarSessio(p.getToken());
        if (s == null) throw new GestorException(Const.Missatge.ERR_SESSIO_INVALIDA);
        Empresa e = empreses.buscarPerCif(s.nomUsuari);
        if (e == null) throw new GestorException(Const.Missatge.ERR_EMPRESA_INEXISTENT);
        
        // Validar i convertir preu
        long preuNum;
        try{
            preuNum = Long.parseLong(preu);
        }catch(NumberFormatException ex){
            throw new GestorException(Const.Missatge.ERR_PRODUCTE_PREU_VALID);
        }
        
        // Crear producte
        Producte pr = new Producte();
        pr.setNom(nom);
        pr.setDescripcio(desc);
        pr.setPreu(preuNum); 
        //e.afegirProducte(pr);
        pr.setEmpresa(e);
        Producte guardat = productes.inserir(pr);

        return new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_PRODUCTE_ADD + "|" + guardat.getId());
    }
    

    private RespostaPeticio llistarProductes(Peticio p) throws GestorException {
        // Validar dades
        if (p == null) throw new GestorException(Const.Missatge.ERR_PETICIO_INEXISTENT);
        if (!sessions.esValida(p.getToken())) throw new GestorException(Const.Missatge.ERR_SESSIO_INVALIDA);
        
        // data opcional: [cif]
        List<Producte> llista;
        if (!p.esDataBuit()) {
            String cif = (String)p.get(0, String.class);
            llista = productes.llistarPerEmpresa(cif);
        } else {
            llista = productes.llistar();
        }
        
        // —— pasar llista a dto ——
        ProducteDto[] dto = new ProducteDto[llista.size()];
        for (int i = 0; i < llista.size(); i++) {
            Producte pr = llista.get(i);
            String cif = null;
            if (pr.getEmpresa() != null) {         
                cif = pr.getEmpresa().getCif();
            }
            dto[i] = new ProducteDto(
                pr.getId(),
                pr.getNom(),
                pr.getDescripcio(),
                pr.getPreu(),
                cif
            );
        }
        
        RespostaPeticio r = new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_PRODUCTE_LIST);
        r.add(dto);
        return r;
    }
    
}
