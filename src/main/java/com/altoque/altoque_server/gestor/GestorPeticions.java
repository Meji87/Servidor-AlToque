
package com.altoque.altoque_server.gestor;

import com.altoque.altoque_server.Const;
import com.altoque.altoque_server.Const.Rol;
import com.altoque.altoque_server.dto.EmpresaDto;
import com.altoque.altoque_server.peticio.RespostaPeticio;
import com.altoque.altoque_server.peticio.Peticio;
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
 * Gestiona una BBDD.
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
     * Si la petició és de login, usuariAdd o empresaAdd no és comprova sessió; altre cas valida el token.
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
        } catch (Exception ex){
            return gson.toJson(new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, 
                    Const.Missatge.ERR_DESCONEGUT + " | " + ex.getMessage()));
        }
    }
    
    /**
     * Funcio que dirigeix la petició cap a la funció correcponent
     * @param p Petició a dirigir
     * @return RespostaPeticio amb codi, missatge i dades del resultat.
     * @throws GestorException 
     */
    private RespostaPeticio router(Peticio p) throws GestorException {
        final String op = p.getPeticio();
        switch (op) {
            
            case Const.Peticio.LOGIN:
                return login(p);
                
            case Const.Peticio.LOGOUT:
                return logout(p);
                
            case Const.Peticio.USUARI_ADD:
                return afegirUsuari(p);
                
            case Const.Peticio.USUARI_DEL:
                return eliminarUsuari(p);
                
            case Const.Peticio.EMPRESA_ADD:
                return afegirEmpresa(p);
                
            case Const.Peticio.EMPRESA_DEL: 
                return eliminarEmpresa(p);
                
            case Const.Peticio.EMPRESA_LIST:
                return llistarEmpreses(p);

            case Const.Peticio.PRODUCTE_ADD:
                return afegirProducte(p);
                
            case Const.Peticio.PRODUCTE_DEL:
                return eliminarProducte(p);

            case Const.Peticio.PRODUCTE_LIST:
                return llistarProductes(p);

            
            default:
                throw new GestorException(Const.Missatge.ERR_PETICIO_INEXISTENT + " | " + op);
        }
    }

    /**
     * Funcio que rep una petició i valida que no sigui null
     * @param p Petició a validar
     * @throws GestorException si peticio null
     */
    private void requereixPeticioNoNull(Peticio p) throws GestorException{
        if (p == null)
            throw new GestorException(Const.Missatge.ERR_PETICIO_INEXISTENT);
    }
    

    /**
     * Funcio que rep una petició i valida que la llista de paràmetres data no sigui buida
     * @param p Petició a validar
     * @throws GestorException si parametre data es buit
     */
    private void requereixData(Peticio p) throws GestorException{
        if (p.esDataBuit())
            throw new GestorException(Const.Missatge.ERR_PARAMETRE_BUIT);
    }

    /**
     * Funcio que rep una petició i valida que el nombre de paràmtres de data sigui major que el passat per paràmetre
     * @param p Petició a validar
     * @throws GestorException si nombre de paràmetres inferior al passat per peràmetre
     */
    private void requereixMinParams(Peticio p, int min) throws GestorException{
        if (p.sizeData() < min)
            throw new GestorException(Const.Missatge.ERR_PARAMETRE_INEXISTENT);
    }

    /**
     * Funcio que rep una petició i valida que el token passat per paràmetre correspongui a una sessió activa
     * @param p Petició a validar
     * @throws GestorException si sessió no vàlida
     */
    private void requereixSessioValida(String token) throws GestorException{
        if (!sessions.esValida(token))
            throw new GestorException(Const.Missatge.ERR_SESSIO_INVALIDA);
    }

    /**
     * Funció que rep una peticio de LOGIN amb identificació i contrasenya. Si credencials correctes retorna identificació de sessió i rol.
     * @param p peticio de LOGIN amb identificador i contrasenya
     * @return si tot correcte retorna identificador de sessió (token) i rol associat a la sessió
     * @throws GestorException llença excepció si no hi ha com a mínim 2 parametres
     */
    private RespostaPeticio login(Peticio p) throws GestorException{
        // Validar dades
        requereixPeticioNoNull(p);
        requereixData(p);
        requereixMinParams(p, 2);

        String identificador = (String)p.getData(0, String.class); 
        String contrasenya   = (String)p.getData(1, String.class);

        // 1) Probar com USUARI
        Usuari u = usuaris.buscarPerNomUsuari(identificador);
        if (u != null && contrasenya != null && contrasenya.equals(u.getContrasenya())) {
            Sessio s = sessions.iniciarSessio(identificador, Rol.USUARI); // guardamos el sujeto
            RespostaPeticio r = new RespostaPeticio(Const.Resposta.OK_RETURN_CODE,
                                                    Const.Missatge.OK_LOGIN_USUARI);
            r.addData(s.token);
            r.addData(Rol.USUARI.name());  
            //r.addData(u.getNom());        
            return r;
        }

        // 2) Probar com EMPRESA
        Empresa e = empreses.buscarPerCif(identificador);
        if (e != null && contrasenya != null && contrasenya.equals(e.getContrasenya())) {
            Sessio s = sessions.iniciarSessio(identificador, Rol.EMPRESA);
            RespostaPeticio r = new RespostaPeticio(Const.Resposta.OK_RETURN_CODE,
                                                    Const.Missatge.OK_LOGIN_EMPRESA);
            r.addData(s.token);
            r.addData(Rol.EMPRESA.name());         
            //r.addData(e.getCif());        
            return r;
        }

        // 3) Error de credencials
        return new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE,
                                   Const.Missatge.ERR_LOGIN_USUARI);
    }
    
    /**
     * Funció que rep una peticio de LOGOUT amb identificació de sessió vàlid a tencar.
     * @param p Peticio amb token per tencar sessió
     * @return retorna RespostaPeticio amb codi i missatge del resultat.
     * @throws GestorException si la sessió no és vàlida
     */
    private RespostaPeticio logout(Peticio p) throws GestorException{
        // Validar dades
        requereixPeticioNoNull(p);
        requereixSessioValida(p.getToken());
        
        sessions.tancarSessio(p.getToken());
        
        return new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_SESSIO_TANCADA);
    }

    /**
     * Funció que rep una peticio de USUARI_ADD amb els pàrametres nomusuari, contrasenya, nom, cognoms
     * @param p Peticio amb els paràmetres corresponents
     * @return retorna RespostaPeticio amb codi i missatge del resultat.
     * @throws GestorException si no hi ha els paràmetres requerits
     */
    private RespostaPeticio afegirUsuari(Peticio p) throws GestorException {
        // Validar dades
        requereixPeticioNoNull(p);
        requereixData(p);
        requereixMinParams(p, 4);
        //requereixSessioValida(p.getToken());
        
        String nomusuari = (String)p.getData(0, String.class);
        String contrasenya = (String)p.getData(1, String.class);
        String nom = (String)p.getData(2, String.class);
        String cognoms = (String)p.getData(3, String.class);

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
    
    /**
     * Funció que rep una peticio USUARI_DEL que eliminarà l'usuari amb l'identificador de sessió vinculat
     * @param p Petició amb token
     * @return retorna RespostaPeticio amb codi i missatge del resultat.
     * @throws GestorException si la sessió no és vàlida
     */
    private RespostaPeticio eliminarUsuari(Peticio p) throws GestorException{
        // Validar dades
        requereixPeticioNoNull(p);
        requereixSessioValida(p.getToken());
        
        Sessio s = sessions.buscarSessio(p.getToken());
        
        // Comprobar que l'usuari existeix
        Usuari usuariExisteix = usuaris.buscarPerNomUsuari(s.nomUsuari);
        
        if (usuariExisteix == null)throw new GestorException(Const.Missatge.ERR_USUARI_INEXISTENT);
        
        usuaris.eliminar(s.nomUsuari);
        sessions.tancarSessio(p.getToken());
        
        return new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_USUARI_DEL);
    }
    
    /**
     * Funció que rep una peticio EMPRESA_ADD amb els paràmetres cif, contrasenya i nom
     * @param p Petició amb els parametres corresponents
     * @return retorna RespostaPeticio amb codi i missatge del resultat.
     * @throws GestorException si falta algún paràmetre
     */
    private RespostaPeticio afegirEmpresa(Peticio p) throws GestorException {
        // Validar dades
        requereixPeticioNoNull(p);
        requereixData(p);
        requereixMinParams(p, 3);
        //requereixSessioValida(p.getToken());
        
        String cif = (String)p.getData(0, String.class);
        String contrasenya = (String)p.getData(1, String.class);
        String nom = (String)p.getData(2, String.class);

        if (empreses.buscarPerCif(cif) != null) {
            throw new GestorException(Const.Missatge.ERR_EMPRESA_EXISTENT);
        }

        Empresa e = new Empresa();
        e.setCif(cif);
        e.setContrasenya(contrasenya);
        e.setNom(nom);
        empreses.inserir(e);

        return new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_EMPRESA_ADD);
    }
    
    /**
     * Funció que rep una peticio EMPRESA_DEL que eliminarà l'empresa amb l'identificador de sessió vinculat
     * @param p Petició amb token vàlid
     * @return retorna RespostaPeticio amb codi i missatge del resultat.
     * @throws GestorException si la sessió no és vàlida
     */
    private RespostaPeticio eliminarEmpresa(Peticio p) throws GestorException{
        // Validar dades
        requereixPeticioNoNull(p);
        requereixSessioValida(p.getToken());
        
        Sessio s = sessions.buscarSessio(p.getToken());
        
        // Comprobar que l'empresa existeix
        Empresa empresaExisteix = empreses.buscarPerCif(s.nomUsuari);
        if (empresaExisteix == null)throw new GestorException(Const.Missatge.ERR_EMPRESA_INEXISTENT);
        
        empreses.eliminar(s.nomUsuari);
        sessions.tancarSessio(p.getToken());
        
        return new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_EMPRESA_DEL);
    }
    
    /**
     * Funció que rep una peticio EMPRESA_LIST que llista les empreses registrades a la BBDD en format EmpresaDto
     * @param p Petició amb token d'empresa requerit
     * @return RespostaPeticio amb codi, missatge i llista d'EmpresaDto del resultat.
     * @throws GestorException 
     */
    private RespostaPeticio llistarEmpreses(Peticio p) throws GestorException{
        // Validar dades
        requereixPeticioNoNull(p);
        requereixSessioValida(p.getToken());
        
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
        r.addData(dto);
        return r; 
    }

    /**
     * Funció que rep una peticio PRODUCTE_ADD amb token d'empresa i els paràmetres nom, descripció i preu
     * @param p Petició amb el producte a afegir
     * @return RespostaPeticio amb codi i missatge del resultat.
     * @throws GestorException 
     */
    private RespostaPeticio afegirProducte(Peticio p) throws GestorException {
        // Validar dades
        requereixPeticioNoNull(p);
        requereixData(p);
        requereixMinParams(p, 3);
        requereixSessioValida(p.getToken());
        
        String nom = (String)p.getData(0, String.class);
        String desc = (String)p.getData(1, String.class);
        String preu = (String)p.getData(2, String.class);
        
        Sessio s = sessions.buscarSessio(p.getToken());
        if (s == null) throw new GestorException(Const.Missatge.ERR_SESSIO_INVALIDA);
        Empresa e = empreses.buscarPerCif(s.nomUsuari);
        if (e == null) throw new GestorException(Const.Missatge.ERR_EMPRESA_INEXISTENT);
        
        // Validar i convertir preu
        double preuDbl;
        try{
            String preuConvertit = preu.trim().replace(',', '.');
            preuDbl = Double.parseDouble(preuConvertit);
        }catch(NumberFormatException ex){
            
            throw new GestorException(Const.Missatge.ERR_PRODUCTE_PREU_INVALID + " | " + ex.getMessage(), ex);
        }
        
        // Crear producte
        Producte pr = new Producte();
        pr.setNom(nom);
        pr.setDescripcio(desc);
        pr.setPreu(preuDbl); 
        //e.afegirProducte(pr);
        pr.setEmpresa(e);
        Producte guardat = productes.inserir(pr);

        return new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_PRODUCTE_ADD + " | " + guardat.getId());
    }
    
    /**
     * Funció que rep una peticio PRODUCTE_DEL que eliminarà el producte de l'empresa amb l'identificador de sessió vinculat
     * @param p Petició amb token
     * @return RespostaPeticio amb codi i missatge del resultat.
     * @throws GestorException 
     */
    private RespostaPeticio eliminarProducte(Peticio p) throws GestorException{
        requereixPeticioNoNull(p);
        requereixData(p);
        requereixMinParams(p, 1);
        requereixSessioValida(p.getToken());
        
        String idProducte = (String)p.getData(0, String.class);
        
        long idRebut;
        try{
            idRebut = Long.parseLong(idProducte);
        }catch(NumberFormatException ex){
            throw new GestorException(Const.Missatge.ERR_PRODUCTE_NO_TROBAT_PER_NOM + " | " + ex.getMessage(), ex);
        }
        
        // Comprobar que el producte existeix
        Producte producteExisteix = productes.buscarPerId(idRebut);
        if (producteExisteix == null)throw new GestorException(Const.Missatge.ERR_PRODUCTE_INEXISTENT);
        
        productes.eliminar(producteExisteix.getId());
        
        return new RespostaPeticio(Const.Resposta.OK_RETURN_CODE, Const.Missatge.OK_PRODUCTE_DEL);
    }

    /**
     * Funció que rep una peticio PRODUCTE_LIST que llista els productes registrats en format ProducteDto i si per paràmetre se li passa el identificador (cif) d'una empresa, es llistaràn els productes d'aquesta empresa.
     * @param p Petició amb parametre opcional
     * @return RespostaPeticio amb codi, missatge i llista de ProducteDto del resultat.
     * @throws GestorException 
     */
    private RespostaPeticio llistarProductes(Peticio p) throws GestorException {
        // Validar dades
        requereixPeticioNoNull(p);
        requereixSessioValida(p.getToken());
        
        // data opcional: [cif]
        List<Producte> llista;
        if (!p.esDataBuit()) {
            String cif = (String)p.getData(0, String.class);
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
        r.addData(dto);
        return r;
    }
    
}
