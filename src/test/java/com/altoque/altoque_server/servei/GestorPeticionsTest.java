
package com.altoque.altoque_server.servei;

import com.altoque.altoque_server.Const;
import static com.altoque.altoque_server.Const.Resposta.ERROR_RETURN_CODE;
import static com.altoque.altoque_server.Const.Resposta.OK_RETURN_CODE;
import com.altoque.altoque_server.Const.Rol;
import com.altoque.altoque_server.dto.PackDto;
import com.altoque.altoque_server.peticio.Peticio;
import com.altoque.altoque_server.dto.ProducteDto;
import com.altoque.altoque_server.gestor.GestorContrasenyes;
import com.altoque.altoque_server.peticio.RespostaPeticio;
import com.altoque.altoque_server.gestor.GestorPeticions;
import com.altoque.altoque_server.model.Empresa;
import com.altoque.altoque_server.model.Producte;
import com.altoque.altoque_server.model.Usuari;
import com.altoque.altoque_server.repositori.EmpresaRepositori;
import com.altoque.altoque_server.repositori.PackItemRepositori;
import com.altoque.altoque_server.repositori.PackRepositori;
import com.altoque.altoque_server.repositori.ProducteRepositori;
import com.altoque.altoque_server.repositori.UsuariRepositori;
import com.altoque.altoque_server.servidor.GestorException;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


/**
 *
 * @author marc mestres mejias
 */
@SpringBootTest
@ActiveProfiles("test") //utilitza application-test.properties en lloc de application.properties
public class GestorPeticionsTest {
    
    @Autowired
    private GestorPeticions gestor;
    @Autowired
    private UsuariRepositori usuariRepo;
    @Autowired
    private EmpresaRepositori empresaRepo;
    @Autowired
    private ProducteRepositori producteRepo;
    @Autowired 
    private PackRepositori packRepo;
    @Autowired
    private PackItemRepositori packItemRepo;
    @Autowired
    private GestorContrasenyes gestorPass;


    private final Gson gson = new Gson();
    
    private RespostaPeticio resposta(Peticio p){
        String jsonReq = gson.toJson(p);
        String jsonRes = gestor.processar(jsonReq);
        return gson.fromJson(jsonRes, RespostaPeticio.class);
    }
    
    /**
    * Crea unusuari de prova
    */
    private void crearUsuariProva() throws GestorException {
        Usuari u = new Usuari();
        u.setNomusuari("Marc09");
        u.setNom("Marc");
        u.setCognoms("Mestres");
        u.setContrasenya(gestorPass.hash("123"));
        usuariRepo.save(u);
    }
    
    /**
    * Crea una empresa de prova
    */
    private void crearEmpresaProva() throws GestorException {
        Empresa e = new Empresa();
        e.setCif("B-01287");
        e.setNom("DevTech");
        e.setContrasenya(gestorPass.hash("123"));
        empresaRepo.save(e);
    }
    
    private long crearProducteProva(Empresa e, String nom) {
        Producte pr = new Producte();
        pr.setNom(nom);
        pr.setDescripcio("Desc " + nom);
        pr.setPreu(10.0);
        pr.setEmpresa(e);
        return producteRepo.save(pr).getId();
    }

    
    /**
    * Crea una empresa/usuari de prova i retorna un token de sessió vàlid.
    */
    private String loginDeProva(Rol rol) throws GestorException {
        String identificador = "";
        if (rol.equals(Rol.USUARI)) {
            crearUsuariProva();
            identificador = "Marc09";
        } else if (rol.equals(Rol.EMPRESA)){
            crearEmpresaProva();
            identificador = "B-01287";
        }
        
        Peticio login = new Peticio(Const.Peticio.LOGIN);
        login.addData(identificador);
        login.addData("123");

        RespostaPeticio rLogin = resposta(login);
        assertEquals(OK_RETURN_CODE, rLogin.getCodi(), "El login d'empresa hauria de ser correcte");

        String token = rLogin.getData(0, String.class);
        assertNotNull(token);
        return token;
    }
    
    @BeforeEach
    void setUp(){
        packItemRepo.deleteAll();
        packRepo.deleteAll();
        producteRepo.deleteAll();
        usuariRepo.deleteAll();
        empresaRepo.deleteAll();
        
        
    }
    
    @AfterEach
    void tearDown(){
        packItemRepo.deleteAll();
        packRepo.deleteAll();
        producteRepo.deleteAll();
        usuariRepo.deleteAll();
        empresaRepo.deleteAll();
    }
    
    /**
     * Login amb credencials correctes d'usuari, ha de retornar codi d'èxit.
     */
    @Test
    void loginUsuariOk() throws GestorException{
        // AFEGIR_USUARI
        crearUsuariProva();
        
        // LOGIN_USUARI
        Peticio login = new Peticio(Const.Peticio.LOGIN);
        login.addData("Marc09");
        login.addData("123");
        RespostaPeticio rp = resposta(login);
        
        // El login d'usuari hauria de ser correcte
        assertEquals(OK_RETURN_CODE, rp.getCodi());
        
    }
    
    /**
     * LOGIN d'usuari amb contrasenya incorrecta ha de retornar codi d'error.
     */
    @Test
    void loginUsuariKo() throws GestorException {
        //GestorPeticions gestor = new GestorPeticions(new GestorSessions());
        crearUsuariProva();

        Peticio login = new Peticio(Const.Peticio.LOGIN);
        login.addData("Marc09");
        login.addData("falsa");

        RespostaPeticio rp = resposta(login);

        assertEquals(ERROR_RETURN_CODE, rp.getCodi(), "Amb contrasenya incorrecta s'ha d'obtenir codi d'error");
    }
    
    /**
     * LOGIN amb credencials correctes d'empresa, ha de retornar codi d'èxit.
     */
    @Test
    void loginEmpresaOk() throws GestorException{
        // AFEGIR_EMPRESA
        crearEmpresaProva();
        
        // LOGIN_EMPRESA
        Peticio login = new Peticio(Const.Peticio.LOGIN);
        login.addData("B-01287");
        login.addData("123");
        
        RespostaPeticio rp = resposta(login);

        assertEquals(OK_RETURN_CODE, rp.getCodi(), "El login d'empresa hauria de ser correcte");
    }
    
    /**
     * LOGIN d'empresa amb contrasenya incorrecta ha de retornar codi d'error.
     */
    @Test
    void loginEmpresaKo() throws GestorException {
        // AFEGIR_EMPRESA
        crearEmpresaProva();

        // LOGIN_EMPRESA
        Peticio login = new Peticio(Const.Peticio.LOGIN);
        login.addData("B-01287");
        login.addData("xxx"); // contrasenya incorrecta

        RespostaPeticio rp = resposta(login);

        assertEquals(ERROR_RETURN_CODE, rp.getCodi(), "Amb contrasenya incorrecta s'ha d'obtenir codi d'error");
    }
    
    /**
     * LOGOUT amb un token vàlid ha de retornar codi d'èxit.
     */
    @Test
    void logoutUsuariOk() {
        // ALTA_USUARI
        Peticio alta = new Peticio(Const.Peticio.USUARI_ADD);
        alta.addData("Marc09");
        alta.addData("xxx");
        alta.addData("Marc");
        alta.addData("Mestres");
        resposta(alta);
        
        // LOGIN_USUARI
        Peticio login = new Peticio(Const.Peticio.LOGIN);
        login.addData("Marc09");
        login.addData("xxx");
        RespostaPeticio rLogin = resposta(login);
        assertEquals(OK_RETURN_CODE, rLogin.getCodi());
        String token = (String)rLogin.getData(0, String.class);
        assertNotNull(token);

        // LOGOUT amb el token
        Peticio logout = new Peticio(Const.Peticio.LOGOUT, token);
        RespostaPeticio rp = resposta(logout);

        assertEquals(OK_RETURN_CODE, rp.getCodi(), "El logout hauria de ser correcte");
    }
    
    /**
     * LOGOUT d'empresa amb un token vàlid ha de retornar codi OK.
     */
    @Test
    void logoutEmpresaOk() throws GestorException {
        // ALTA_EMPRESA
        crearEmpresaProva();

        // LOGIN_EMPRESA
        Peticio login = new Peticio(Const.Peticio.LOGIN);
        login.addData("B-01287");
        login.addData("123");
        RespostaPeticio rLogin = resposta(login);
        assertEquals(OK_RETURN_CODE, rLogin.getCodi());
        String token = (String) rLogin.getData(0, String.class);
        assertNotNull(token);

        // LOGOUT amb el token
        Peticio logout = new Peticio(Const.Peticio.LOGOUT, token);
        RespostaPeticio rp = resposta(logout); //gson.fromJson(gestor.processar(gson.toJson(logout)), RespostaPeticio.class);

        assertEquals(OK_RETURN_CODE, rp.getCodi(), "El logout hauria de ser correcte");
    }

    /**
     * LOGOUT amb un token invàlid ha de retornar codi d'error.
     */
    @Test
    void logoutTokenInvalid() {

        Peticio logout = new Peticio(Const.Peticio.LOGOUT);
        logout.addData("TOKEN_FAKE");

        RespostaPeticio rp = resposta(logout); //gson.fromJson(gestor.processar(gson.toJson(logout)), RespostaPeticio.class);

        assertEquals(ERROR_RETURN_CODE, rp.getCodi(), "Amb token invàlid s'ha d'obtenir codi d'error");
    }
   
    /**
     * Enviament de petició invàlida ha de retornar codi d'error.
     */
    @Test
    void peticioInexistent(){
        //GestorPeticions gestor = new GestorPeticions(new GestorSessions());
        
        Peticio p = new Peticio("INVENTADA");
        
        RespostaPeticio rp = resposta(p);
        
        assertEquals(ERROR_RETURN_CODE, rp.getCodi());
        
    }
    
    /**
    * USUARI_ADD amb dades correctes ha de crear un usuari i retornar codi OK.
    */
    @Test
    void usuariAdd(){        
        Peticio alta = new Peticio(Const.Peticio.USUARI_ADD);
        alta.addData("Marc09");
        alta.addData("123");
        alta.addData("Marc");
        alta.addData("Mestres");
        
        RespostaPeticio rp = resposta(alta);

        assertEquals(OK_RETURN_CODE, rp.getCodi(), "L'alta de l'usuari hauria de ser correcta");
    }
    
    /**
    * USUARI_DEL amb sessió correcte ha d'eliminar un usuari i retornar codi OK.
    */
    @Test
    void usuariEliminar() throws GestorException{
        String token = loginDeProva(Rol.USUARI);
        Peticio baixa = new Peticio(Const.Peticio.USUARI_DEL, token);
        RespostaPeticio rp = resposta(baixa);

        assertEquals(OK_RETURN_CODE, rp.getCodi(), "L'usuari s'hauria d'haber eliminat correctament");
    }
    
    /**
    * EMPRESA_ADD amb dades correctes ha de crear una empresa i retornar codi OK.
    */
    @Test
    void empresaAdd(){
        Peticio alta = new Peticio(Const.Peticio.EMPRESA_ADD);
        alta.addData("B-01287");
        alta.addData("123");
        alta.addData("DevTech");
        
        RespostaPeticio rp = resposta(alta);

        assertEquals(OK_RETURN_CODE, rp.getCodi(), "L'alta de l'empresa hauria de ser correcta");
        assertEquals(1, empresaRepo.count());

        var em = empresaRepo.findAll().get(0);
        assertEquals("B-01287", em.getCif());
        assertEquals("DevTech", em.getNom());
    }
    
    /**
    * EMPRESA_DEL amb sessió correcte ha d'eliminar un usuari i retornar codi OK.
    */
    @Test
    void empresaEliminar() throws GestorException{
        String token = loginDeProva(Rol.EMPRESA);
        Peticio baixa = new Peticio(Const.Peticio.EMPRESA_DEL, token);
        RespostaPeticio rp = resposta(baixa);

        assertEquals(OK_RETURN_CODE, rp.getCodi(), "L'empresa s'hauria d'haber eliminat correctament");
    }
    
    
    /**
    * PRODUCTE_ADD amb dades correctes ha de crear el producte i retornar codi OK.
    */
   @Test
   void producteAddOk() throws GestorException {
       String token = loginDeProva(Rol.EMPRESA);

       Peticio alta = new Peticio(Const.Peticio.PRODUCTE_ADD, token);
       alta.addData("Gin Premium");
       alta.addData("Ampolla 70cl");
       alta.addData("15.50");   // preu en text

       RespostaPeticio rp = resposta(alta);

       assertEquals(OK_RETURN_CODE, rp.getCodi(), "L'alta de producte hauria de ser correcta");
       assertEquals(1, producteRepo.count());

       var pr = producteRepo.findAll().get(0);
       assertEquals("Gin Premium", pr.getNom());
       assertEquals("Ampolla 70cl", pr.getDescripcio());
       assertEquals(15.50, pr.getPreu());
       assertEquals("B-01287", pr.getEmpresa().getCif());
   }
   
    /**
     * PRODUCTE_ADD amb un preu no numèric ha de retornar codi d'error.
     */
    @Test
    void producteAddPreuInvalid() throws GestorException {
        String token = loginDeProva(Rol.EMPRESA);

        Peticio alta = new Peticio(Const.Peticio.PRODUCTE_ADD, token);
        alta.addData("Gin Raro");
        alta.addData("Ampolla 50cl");
        alta.addData("ABC");   // preu incorrecte

        RespostaPeticio rp = resposta(alta);

        assertEquals(ERROR_RETURN_CODE, rp.getCodi(), "Amb preu invàlid s'ha d'obtenir codi d'error");
    }
    
    /**
     * PRODUCTE_LIST ha de retornar tots els productes de la BBDD 
     */
    @Test
    void producteLlistar() throws GestorException {
        // Empresa 1
        String tokenEmpresa1 = loginDeProva(Rol.EMPRESA);

        // Afegim 2 productes per empresa1
        Peticio alta1 = new Peticio(Const.Peticio.PRODUCTE_ADD, tokenEmpresa1);
        alta1.addData("Gin A");
        alta1.addData("Desc A");
        alta1.addData("10.0");
        resposta(alta1);

        Peticio alta2 = new Peticio(Const.Peticio.PRODUCTE_ADD, tokenEmpresa1);
        alta2.addData("Gin B");
        alta2.addData("Desc B");
        alta2.addData("12.0");
        resposta(alta2);
        
        Peticio llistarProductes = new Peticio(Const.Peticio.PRODUCTE_LIST, tokenEmpresa1);

        RespostaPeticio rList = resposta(llistarProductes);
        assertEquals(OK_RETURN_CODE, rList.getCodi());
    }
    
    /**
     * PRODUCTE_LIST ha de retornar només els productes de l'empresa indicada,
     */
    @Test
    void producteEmpresaLlistar() throws GestorException {
        // Empresa 1
        String tokenEmpresa1 = loginDeProva(Rol.EMPRESA);

        // Afegim 2 productes per empresa1
        Peticio alta1 = new Peticio(Const.Peticio.PRODUCTE_ADD, tokenEmpresa1);
        alta1.addData("Gin A");
        alta1.addData("Desc A");
        alta1.addData("10.0");
        resposta(alta1);

        Peticio alta2 = new Peticio(Const.Peticio.PRODUCTE_ADD, tokenEmpresa1);
        alta2.addData("Gin B");
        alta2.addData("Desc B");
        alta2.addData("12.0");
        resposta(alta2);
        
        // Empresa 2 amb un producte més (per comprovar filtre)
        Empresa e2 = new Empresa();
        e2.setCif("B-00002");
        e2.setNom("Empresa2");
        e2.setContrasenya(gestorPass.hash("999"));
        empresaRepo.save(e2);

        Peticio login2 = new Peticio(Const.Peticio.LOGIN);
        login2.addData("B-00002");
        login2.addData("999");
        RespostaPeticio rLogin2 = resposta(login2);
        String tokenEmpresa2 = rLogin2.getData(0, String.class);

        Peticio alta3 = new Peticio(Const.Peticio.PRODUCTE_ADD, tokenEmpresa2);
        alta3.addData("Gin C");
        alta3.addData("Desc C");
        alta3.addData("9.5");
        resposta(alta3);

        // Llistar només productes de l'empresa 2
        Peticio llistarEmpresa1 = new Peticio(Const.Peticio.PRODUCTE_LIST, tokenEmpresa1);
        llistarEmpresa1.addData("B-00002");   // CIF de l'empresa 2
        
        RespostaPeticio rList = resposta(llistarEmpresa1);
        assertEquals(OK_RETURN_CODE, rList.getCodi());
    }

    /**
     * PRODUCTE_DEL ha d'eliminar un producte existent.
     */
    @Test
    public void producteEliminar() throws GestorException {
        String token = loginDeProva(Rol.EMPRESA);

        Peticio alta1 = new Peticio(Const.Peticio.PRODUCTE_ADD, token);
        alta1.addData("Gin Premium");
        alta1.addData("Ampolla 70cl");
        alta1.addData("15.50");
        RespostaPeticio rp = resposta(alta1);
        assertEquals(OK_RETURN_CODE, rp.getCodi(), "L'alta de producte hauria de ser correcta");
        
        Peticio alta2 = new Peticio(Const.Peticio.PRODUCTE_ADD, token);
        alta2.addData("Gin A");
        alta2.addData("Ampolla 100cl");
        alta2.addData("18.60");
        RespostaPeticio rps = resposta(alta2);
        assertEquals(OK_RETURN_CODE, rps.getCodi(), "L'alta de producte hauria de ser correcta");
        
        Peticio llista = new Peticio(Const.Peticio.PRODUCTE_LIST, token);
        RespostaPeticio rList = resposta(llista);
        ProducteDto[] arr = rList.getData(0, ProducteDto[].class);
        assertEquals(2, arr.length, "Hi han 2 productes");
        
        // Eliminar un dels productes de l'empresa
        long idAEliminar = arr[0].id;

        Peticio eliminar = new Peticio(Const.Peticio.PRODUCTE_DEL, token);
        eliminar.addData(String.valueOf(idAEliminar));

        RespostaPeticio rDel = resposta(eliminar);
        assertEquals(OK_RETURN_CODE, rDel.getCodi(), "L'eliminació hauria de ser correcta");

        // Comprovar que ara només queda 1 producte de l'empresa
        RespostaPeticio rList2 = resposta(llista);
        ProducteDto[] arr2 = rList2.getData(0, ProducteDto[].class);
        assertEquals(1, arr2.length);
    }
    
    @Test
    void packCrudComplet() throws GestorException {

        // Login empresa (crea empresa B-01287 y devuelve token)
        String token = loginDeProva(Rol.EMPRESA);

        Empresa e = empresaRepo.findAll().get(0);

        // Crear 2 productes de l'empresa
        long p1 = crearProducteProva(e, "Prod1");
        long p2 = crearProducteProva(e, "Prod2");

        // ---- PACK_ADD ----
        PackDto dtoAdd = new PackDto();
        dtoAdd.setNom("Pack Vermut");
        dtoAdd.setPreu(1990L);

        PackDto.PackItemDto i1 = new PackDto.PackItemDto();
        i1.setProducteId(p1);
        i1.setQuantitat(1);

        PackDto.PackItemDto i2 = new PackDto.PackItemDto();
        i2.setProducteId(p2);
        i2.setQuantitat(2);

        dtoAdd.setItems(java.util.List.of(i1, i2));

        Peticio add = new Peticio(Const.Peticio.PACK_ADD, token);
        add.addData(dtoAdd);

        RespostaPeticio rAdd = resposta(add);
        assertEquals(OK_RETURN_CODE, rAdd.getCodi());

        Long idPack = rAdd.getData(0, Long.class);
        assertNotNull(idPack);

        // ---- PACK_GET ----
        Peticio get = new Peticio(Const.Peticio.PACK_GET, token);
        get.addData(String.valueOf(idPack));

        RespostaPeticio rGet = resposta(get);
        assertEquals(OK_RETURN_CODE, rGet.getCodi());

        PackDto dtoGet = rGet.getData(0, PackDto.class);
        assertEquals("Pack Vermut", dtoGet.getNom());
        assertEquals(1990L, dtoGet.getPreu());
        assertNotNull(dtoGet.getItems());
        assertEquals(2, dtoGet.getItems().size());

        // ---- PACK_MOD (cambiar items) ----
        PackDto dtoMod = new PackDto();
        dtoMod.setId(String.valueOf(idPack));
        dtoMod.setNom("Pack Vermut Premium");
        dtoMod.setPreu(2490L);

        PackDto.PackItemDto i3 = new PackDto.PackItemDto();
        i3.setProducteId(p1);
        i3.setQuantitat(3); // cambiar quantitats

        dtoMod.setItems(java.util.List.of(i3)); // ara nomes 1 item

        Peticio mod = new Peticio(Const.Peticio.PACK_MOD, token);
        mod.addData(dtoMod);

        RespostaPeticio rMod = resposta(mod);
        System.out.println("PACK_MOD codi= " + rMod.getCodi() + " | msg= " + rMod.getMissatge());
        //System.out.println("PACK_MOD errors=" + rMod.); // si existe
        assertEquals(OK_RETURN_CODE, rMod.getCodi());

        // ---- PACK_GET (altre cop) ----
        RespostaPeticio rGet2 = resposta(get);
        assertEquals(OK_RETURN_CODE, rGet2.getCodi());

        PackDto dtoGet2 = rGet2.getData(0, PackDto.class);
        assertEquals("Pack Vermut Premium", dtoGet2.getNom());
        assertEquals(2490L, dtoGet2.getPreu());
        assertEquals(1, dtoGet2.getItems().size());
        assertEquals(p1, dtoGet2.getItems().get(0).getProducteId());
        assertEquals(3, dtoGet2.getItems().get(0).getQuantitat());

        // ---- PACK_DEL ----
        Peticio del = new Peticio(Const.Peticio.PACK_DEL, token);
        del.addData(String.valueOf(idPack));

        RespostaPeticio rDel = resposta(del);
        assertEquals(OK_RETURN_CODE, rDel.getCodi());

        // ---- PACK_GET ha de fallar ----
        RespostaPeticio rGetKo = resposta(get);
        assertEquals(ERROR_RETURN_CODE, rGetKo.getCodi());
    }

    
}
