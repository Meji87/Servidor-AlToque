

package com.altoque.altoque_server.client;

import com.altoque.altoque_server.Const;
import com.altoque.altoque_server.Const.Rol;
import com.altoque.altoque_server.dto.EmpresaDto;
import com.altoque.altoque_server.dto.PackDto;
import com.altoque.altoque_server.peticio.Peticio;
import com.altoque.altoque_server.peticio.RespostaPeticio;
import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import com.altoque.altoque_server.dto.ProducteDto;
import com.altoque.altoque_server.dto.UsuariDto;
import com.altoque.altoque_server.gestor.GestorXifrat;
import com.altoque.altoque_server.model.Empresa;
import java.util.ArrayList;
import java.util.List;


/**
 * Client de consola senzill per a AlToque.
 * - Un únic mètode send(...) per protocol
 * - Login únic; el servidor decideix el rol
 * - Alta usuari/empresa sense sessió
 * - Eliminar el propi usuari/empresa des de la sessió (esborra BBDD i sessió)
 * 
 * @author marc mestres
 */
public class Client {

    // === Config ===
    private static final String HOST = "localhost"; //10.2.94.42
    private static final int    PORT = 5050;

    // === Estat de la sessió local ===
    private String token = null;
    private Rol rol = null;
    private String nomUsuari = null; // pot ser nom d'usuari o CIF visible
    private String nomDesc = null;
    // === Infra ===
    private final Scanner in = new Scanner(System.in);
    private final Gson gson = new Gson();

    public static void main(String[] args) {
        new Client().run();
    }

    private void run() {
        System.out.println("-----------------------------------------");
        System.out.println("          AlToque – Client CLI           ");
        System.out.println("-----------------------------------------");

        while (true) {
            if (token == null) {
                if (!menuPrincipal()) break;
            } else if (rol == Rol.USUARI) {
                if (!menuUsuari()) break;
            } else { // EMPRESA
                if (!menuEmpresa()) break;
            }
        }
        System.out.println("Adeu! ");
    }

    // ============================
    // Menú principal (sense sessió)
    // ============================
    private boolean menuPrincipal() {
        System.out.println();
        System.out.println("***** Menu principal *****");
        System.out.println(" [1] Alta USUARI");
        System.out.println(" [2] Alta EMPRESA");
        System.out.println(" [3] Login");
        System.out.println(" [0] Sortir");

        int op = askInt("Opcio: ", -1);
        switch (op) {
            case 1 -> actionAltaUsuari();
            case 2 -> actionAltaEmpresa();
            case 3 -> actionLogin();
            case 0 -> { return false; }
            default -> System.out.println("Opcio no valida.");
        }
        return true;
    }

    // ============================
    // Menú USUARI
    // ============================
    private boolean menuUsuari() {
        headerSessio();
        System.out.println("***** Menu USUARI *****");
        System.out.println(" [1] Whoami / Veure token");
        System.out.println(" [2] Modificar les meves dades");
        System.out.println(" [3] Eliminar el meu USUARI");
        System.out.println(" [4] Veure empreses");
        System.out.println(" [9] Logout");
        System.out.println(" [0] Sortir");

        int op = askInt("Opcio: ", -1);
        switch (op) {
            case 1 -> actionWhoAmI();
            case 2 -> actionUsuariMod();
            case 3 -> actionEliminarPropiCompte(); // esborra usuari + sessió
            case 4 -> actionEmpresaLlistar();
            case 9 -> actionLogout();
            case 0 -> { return false; }
            default -> System.out.println("Opcio no valida.");
        }
        return true;
    }

    // ============================
    // Menú EMPRESA
    // ============================
    private boolean menuEmpresa() {
        headerSessio();
        System.out.println("***** Menu EMPRESA *****");
        System.out.println(" [1]  Whoami / Veure token");
        System.out.println(" [2]  Modificar les meves dades");
        System.out.println(" [3]  Eliminar la meva EMPRESA");
        System.out.println(" [4]  Afegir producte");
        System.out.println(" [5]  Modificar producte");
        System.out.println(" [6]  Eliminar producte");
        System.out.println(" [7]  Llistar productes (tots)");
        System.out.println(" [8]  Llistar productes (meva empresa)");
        System.out.println(" [9]  Logout");
        System.out.println(" [10] Afegir pack");
        System.out.println(" [11] Modificar pack");
        System.out.println(" [12] Eliminar pack");
        System.out.println(" [13] Llistar packs (meva empresa)");
        System.out.println(" [14] Veure un pack (GET per id)");
        System.out.println(" [0]  Sortir");

        int op = askInt("Opcio: ", -1);
        switch (op) {
            case 1 -> actionWhoAmI();
            case 2 -> actionEmpresaMod();
            case 3 -> actionEliminarPropiCompte(); // esborra empresa + sessió
            case 4 -> actionProducteAdd();
            case 5 -> actionProducteMod();
            case 6 -> actionProducteDel();
            case 7 -> actionProducteLlistar();
            case 8 -> actionProducteLlistarEmpresa();
            case 9 -> actionLogout();
            case 10 -> actionPackAdd();
            case 11 -> actionPackMod();
            case 12 -> actionPackDel();
            case 13 -> actionPackListEmpresa();
            case 14 -> actionPackGet();

            case 0 -> { return false; }
            default -> System.out.println("Opcio no valida.");
        }
        return true;
    }

    // ==========
    // ACCIONS
    // ==========

    // Alta usuari (des de pantalla principal)
    private void actionAltaUsuari() {
        System.out.println("\n***** Alta USUARI *****");
        String nomUsuari = askStr("Nom d'usuari: ", true);
        String nom       = askStr("Nom: ", true);
        String cognoms       = askStr("Cognoms: ", true);
        String pwd       = askStr("Contrasenya: ", true);

        Peticio p = new Peticio(Const.Peticio.USUARI_ADD);
        p.addData(nomUsuari);
        p.addData(pwd);
        p.addData(nom);
        p.addData(cognoms);

        printResposta(send(p));
    }
    
    // Modificar usuari
    private void actionUsuariMod(){
        System.out.println("\n***** Modificar dades USUARI *****");
        System.out.println("Deixa en blanc el camp que no vulguis modificar");
        String nom = askStr("Nou nom: ", false);          // Pot ser buit
        String cognoms = askStr("Nous cognoms: ", false); // Pot ser buit
        String pwd = askStr("Nova contrasenya: ", false); // Pot ser buit
        
        if (nom != null && nom.isBlank()) nom = null;
        if (cognoms != null && cognoms.isBlank()) cognoms = null;
        if (pwd != null && pwd.isBlank()) pwd = null;
        
        UsuariDto dto = new UsuariDto(nomUsuari, nom, cognoms, pwd);
        
        Peticio p = new Peticio(Const.Peticio.USUARI_MOD, token);
        p.addData(dto);

        printResposta(send(p));
    }

    // Alta empresa (des de pantalla principal)
    private void actionAltaEmpresa() {
        System.out.println("\n***** Alta EMPRESA *****");
        String cif = askStr("CIF: ", true);
        String nom = askStr("Nom empresa: ", true);
        String pwd = askStr("Contrasenya: ", true);

        Peticio p = new Peticio(Const.Peticio.EMPRESA_ADD);
        p.addData(cif);
        p.addData(pwd);
        p.addData(nom);

        printResposta(send(p));
    }
    
    // Modificar empresa
    private void actionEmpresaMod(){
        System.out.println("\n***** Modificar dades EMPRESA *****");
        System.out.println("Deixa en blanc el camp que no vulguis modificar");
        String nom = askStr("Nom empresa: ", false);
        String pwd = askStr("Contrasenya: ", false);
        
        if (nom != null && nom.isBlank()) nom = null;
        if (pwd != null && pwd.isBlank()) pwd = null;
        
        EmpresaDto dto = new EmpresaDto(nomUsuari, nom, pwd);

        Peticio p = new Peticio(Const.Peticio.EMPRESA_MOD, token);
        p.addData(dto);

        printResposta(send(p));
    }

    // Login únic (el servidor decideix rol)
    private void actionLogin() {
        System.out.println("\n***** Login *****");
        String usu = askStr("Usuari/CIF: ", true);
        String pwd = askStr("Contrasenya: ", true);

        Peticio p = new Peticio(Const.Peticio.LOGIN);
        p.addData(usu);
        p.addData(pwd);

        RespostaPeticio r = send(p);
        if (!printResposta(r)) return;

        // Esperem: [token, rol, nomVisible?]
        this.token = (String)r.getData(0, String.class);
        this.rol   = Rol.valueOf( r.getData(1, String.class));
        this.nomUsuari = (r.sizeData() >= 3) ? (String) r.getData(2, String.class) : usu;

        System.out.printf("Sessio iniciada com a %s%n", rol.name());
    }

    // Logout
    private void actionLogout() {
        if (token == null) { System.out.println("No hi ha sessio."); return; }

        Peticio p = new Peticio(Const.Peticio.LOGOUT, token);
        //p.addDataObject(token);

        if (printResposta(send(p))) {
            token = null; rol = null; nomUsuari = null;
        }
    }

    // Whoami (mostrar dades locals)
    private void actionWhoAmI() {
        if (token == null) { System.out.println("No hi ha sessio."); return; }
        System.out.println("\n— Sessio —");
        System.out.println(" Usuari visible: " + nomUsuari);
        System.out.println(" Rol: " + rol);
        System.out.println(" Token: " + token);
    }

    // Eliminar compte propi (USUARI o EMPRESA) + tancar sessió
    private void actionEliminarPropiCompte() {
        if (token == null) { System.out.println("No hi ha sessió."); return; }

        String miss = (rol == Rol.USUARI)
                ? "Segur que vols eliminar el teu USUARI? (s/n): "
                : "Segur que vols eliminar la teva EMPRESA? (s/n): ";
        if (!confirm(miss)) return;

        String ordre = (rol == Rol.USUARI) ? Const.Peticio.USUARI_DEL : Const.Peticio.EMPRESA_DEL;

        Peticio p = new Peticio(ordre, token);
        // Enviem només token; el servidor troba el subject i el rol a la sessió
        //p.addDataObject(token);

        RespostaPeticio r = send(p);
        if (printResposta(r)) {
            // Si s'ha esborrat, la sessió ja no té sentit
            token = null; rol = null; nomUsuari = null;
        }
    }

    // ——— PRODUCTES (només EMPRESA) ———

    private void actionProducteAdd() {
        ensureSessioEmpresa();

        System.out.println("\n***** Afegir producte *****");
        String nom  = askStr("Nom del producte: ", true);
        String descripcio = askStr("Descripcio: ", true);
        //Double pre = askDouble("Preu: ", 0.0);
        String preu = askStrDouble("Preu: ", "0");

        Peticio p = new Peticio(Const.Peticio.PRODUCTE_ADD, token);
        //p.addDataObject(token);
        p.addData(nom);
        p.addData(descripcio);
        p.addData(preu);

        printResposta(send(p));
    }
    
    private void actionProducteMod() {
        System.out.println("\n***** Modificar dades PRODUCTE *****");
        System.out.println("Deixa en blanc el camp que no vulguis modificar");
        Long id = askLong("Id del producte a modificar: ", 0L);
        String nom = askStr("nom producte: ", false);
        String desc = askStr("nova descripcio: ", false);
        Double preu = askDouble("nou preu", 0.0);
        
        if (id != null && id.equals(0L)) id = null;
        if (nom != null && nom.isBlank()) nom = null;
        if (desc != null && desc.isBlank()) desc = null;
        if (preu != null && preu == 0) preu = null;
        
        ProducteDto dto = new ProducteDto(id, nom, desc, preu);

        Peticio p = new Peticio(Const.Peticio.PRODUCTE_MOD, token);
        p.addData(dto);

        printResposta(send(p));
    }

    private void actionProducteDel() {
        ensureSessioEmpresa();

        String id = askStr("\nID producte a eliminar: ", true);

        Peticio p = new Peticio(Const.Peticio.PRODUCTE_DEL, token);
        //p.addDataObject(token);
        p.addData(id);

        printResposta(send(p));
    }

    private void actionProducteLlistar() {
        ensureSessioEmpresa();

        Peticio p = new Peticio(Const.Peticio.PRODUCTE_LIST, token);
        //p.addDataObject(token);

        RespostaPeticio r = send(p);
        if (!printResposta(r)) return;
        printProductes(r);
    }
    
    
    private void actionProducteLlistarEmpresa() {
        ensureSessioEmpresa();
        Peticio p = new Peticio(Const.Peticio.PRODUCTE_LIST, token);
        // El servidor espera un CIF opcional, no el token:
        p.addData(nomUsuari); // nomUsuari en sesión de EMPRESA es el CIF visible
        RespostaPeticio r = send(p);
        if (!printResposta(r)) return;
        printProductes(r);
    }
    
    private void actionEmpresaLlistar() {

        Peticio p = new Peticio(Const.Peticio.EMPRESA_LIST, token);
        //p.addDataObject(token);

        RespostaPeticio r = send(p);
        if (!printResposta(r)) return;
        printEmpreses(r);
    }
    
    //PACKS
    
    private void actionPackAdd() {
        ensureSessioEmpresa();
        System.out.println("\n***** Afegir PACK *****");

        String nom = askStr("Nom pack: ", true);
        Long preu = askLong("Preu (ex: 2490): ", null);

        int n = askInt("Quants items? ", 1);
        List<PackDto.PackItemDto> items = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("Item " + (i+1));
            Long producteId = askLong("  Producte ID: ", null);
            Integer q = askInt("  Quantitat: ", 1);

            PackDto.PackItemDto it = new PackDto.PackItemDto();
            it.setProducteId(producteId);
            it.setQuantitat(q);
            items.add(it);
        }

        PackDto dto = new PackDto();
        dto.setNom(nom);
        dto.setPreu(preu);
        dto.setItems(items);

        Peticio p = new Peticio(Const.Peticio.PACK_ADD, token);
        p.addData(dto);

        printResposta(send(p));
    }

    private void actionPackMod() {
        ensureSessioEmpresa();
        System.out.println("\n***** Modificar PACK *****");

        String id = askStr("ID pack: ", true);
        String nom = askStr("Nou nom (enter per no tocar): ", false);
        Long preu = askLong("Nou preu (enter per no tocar): ", null);

        int n = askInt("Quants items vols deixar (REEMPLAÇA)? ", 1);
        List<PackDto.PackItemDto> items = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("Item " + (i+1));
            Long producteId = askLong("  Producte ID: ", null);
            Integer q = askInt("  Quantitat: ", 1);

            PackDto.PackItemDto it = new PackDto.PackItemDto();
            it.setProducteId(producteId);
            it.setQuantitat(q);
            items.add(it);
        }

        PackDto dto = new PackDto();
        dto.setId(id);
        if (nom != null && !nom.isBlank()) dto.setNom(nom.trim());
        if (preu != null) dto.setPreu(preu);
        dto.setItems(items); // IMPORTANT: mod fa replace

        Peticio p = new Peticio(Const.Peticio.PACK_MOD, token);
        p.addData(dto);

        printResposta(send(p));
    }
    
    private void actionPackDel() {
        ensureSessioEmpresa();
        System.out.println("\n***** Eliminar PACK *****");

        String id = askStr("ID pack a eliminar: ", true);

        Peticio p = new Peticio(Const.Peticio.PACK_DEL, token);
        p.addData(id);

        printResposta(send(p));
    }

    private void actionPackGet() {
        ensureSessioEmpresa();
        System.out.println("\n***** GET PACK *****");

        String id = askStr("ID pack: ", true);

        Peticio p = new Peticio(Const.Peticio.PACK_GET, token);
        p.addData(id);

        RespostaPeticio r = send(p);
        if (!printResposta(r)) return;

        PackDto dto = r.getData(0, PackDto.class);
        printPack(dto);
    }
    
    private void actionPackListEmpresa() {
        ensureSessioEmpresa();

        Peticio p = new Peticio(Const.Peticio.PACK_LIST, token);
        // si el teu servidor espera CIF opcional:
        // p.addData(nomUsuari);

        RespostaPeticio r = send(p);
        if (!printResposta(r)) return;

        PackDto[] arr = r.getData(0, PackDto[].class);
        if (arr == null || arr.length == 0) {
            System.out.println("(sense packs)");
            return;
        }
        for (PackDto dto : arr) printPack(dto);
    }




    // ==========
    // Helpers
    // ==========

    private void headerSessio() {
        System.out.printf("%nSessio: %s (%s)%n", nomUsuari, rol);
    }

    private boolean printResposta(RespostaPeticio r) {
        if (r == null) { System.out.println("No hi ha resposta del servidor."); return false; }
        System.out.printf("%s (codi=%d)%n", r.getMissatge(), r.getCodi());
        return r.getCodi() == Const.Resposta.OK_RETURN_CODE;
    }
    
    private void printProductes(RespostaPeticio r) {
        ProducteDto[] arr = r.getData(0, ProducteDto[].class);  
        if (arr == null || arr.length == 0) { System.out.println("(sense productes)"); return; }
        System.out.println("___ Productes ___");
        System.out.println("  [ID] [NOM]  --  [PREU]  [EMPRESA ID]");
        for (ProducteDto p : arr) {
            System.out.printf("  [%s] %s  --  %.2f € %s%n", 
                    p.id, p.nom, p.preu, (p.empresaCif != null ? "(" + p.empresaCif + ")" : ""));

        }
    }

    private void printEmpreses(RespostaPeticio r) {
        Empresa[] arr = r.getData(0, Empresa[].class);
        if (arr == null || arr.length == 0) { System.out.println("(sense empreses)"); return; }
        System.out.println("___ Empreses ___");
        System.out.println("  [ID CIF] -- [NOM]");
        for (Empresa e : arr) {
            System.out.printf("   %s  --  %s%n", e.getCif(), e.getNom());
        }
    }
    
    private void printPack(PackDto p) {
        if (p == null) { System.out.println("(pack null)"); return; }
        System.out.printf("PACK [%s] %s - %d%n", p.getId(), p.getNom(), p.getPreu());
        if (p.getItems() != null) {
            for (PackDto.PackItemDto it : p.getItems()) {
                System.out.printf("   - producteId x quant. = %d x %d%n", it.getProducteId(), it.getQuantitat());
            }
        }
    }

    private void ensureSessioEmpresa() {
        if (token == null || rol != Rol.EMPRESA) {
            throw new IllegalStateException("Cal sessio d'EMPRESA per aquesta acció.");
        }
    }

    // ===========================
    // I/O protocol JSON per línia
    // ===========================
    private RespostaPeticio send(Peticio peticio) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String json = gson.toJson(peticio);
            String jsonEncriptat = GestorXifrat.xifrar(json); // nova
            out.println(jsonEncriptat); // modificada
            out.flush(); // nova

            String respostaJsonEncriptada = br.readLine(); // modificada
            String respostaJson = GestorXifrat.desxifrar(respostaJsonEncriptada); //nova
            if (respostaJson == null) return null;

            return gson.fromJson(respostaJson, RespostaPeticio.class);
        
        } catch (IOException e) {
            System.out.println("Error de connexio: " + e.getMessage());
            return new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, "Error de connexio: " + e.getMessage());
        } catch (Exception ex){
            System.out.println("Error de xifratge: " + ex.getMessage());
            return new RespostaPeticio(Const.Resposta.ERROR_RETURN_CODE, "Error de xifratge: " + ex.getMessage());
        }
    }

    // ===========================
    // Lectura de dades per consola
    // ===========================
    private String askStr(String prompt, boolean required) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine();
            if (!required || (s != null && !s.isBlank())) return s.trim();
            System.out.println("Valor obligatori.");
        }
    }

    private int askInt(String prompt, int defaultVal) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine();
            if (s == null || s.isBlank()) return defaultVal;
            try { return Integer.parseInt(s.trim()); }
            catch (NumberFormatException e) { System.out.println("Introdueix un numero."); }
        }
    }
    
    private Long askLong(String prompt, Long defaultVal) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine();
            if (s == null || s.isBlank()) return defaultVal;
            try { return Long.parseLong(s.trim()); }
            catch (NumberFormatException e) { System.out.println("Introdueix un numero."); }
        }
    }
    
    private Double askDouble(String prompt, Double defaultVal) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine();
            if (s == null || s.isBlank()) return defaultVal;
            try { return Double.parseDouble(s.trim()); }
            catch (NumberFormatException e) { System.out.println("Introdueix un numero (ex: 1.25)."); }
        }
    }

    private String askStrDouble(String prompt, String defaultVal) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine();
            if (s == null || s.isBlank()) return defaultVal;
            try { 
                Double d = Double.parseDouble(s.trim());
                return d.toString(); }
            catch (NumberFormatException e) { System.out.println("Introdueix un numero (ex: 1.25)."); }
        }
    }

    private boolean confirm(String prompt) {
        System.out.print(prompt);
        String s = in.nextLine();
        return s != null && s.trim().equalsIgnoreCase("s");
    }
}

