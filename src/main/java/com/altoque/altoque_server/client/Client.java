

package com.altoque.altoque_server.client;

import com.altoque.altoque_server.Const;
import com.altoque.altoque_server.Const.Rol;
import com.altoque.altoque_server.peticio.Peticio;
import com.altoque.altoque_server.peticio.RespostaPeticio;
import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import com.altoque.altoque_server.dto.ProducteDto;
import com.altoque.altoque_server.model.Empresa;


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
        System.out.println(" [2] Eliminar el meu USUARI");
        System.out.println(" [3] Veure empreses");
        System.out.println(" [9] Logout");
        System.out.println(" [0] Sortir");

        int op = askInt("Opcio: ", -1);
        switch (op) {
            case 1 -> actionWhoAmI();
            case 2 -> actionEliminarPropiCompte(); // esborra usuari + sessió
            case 3 -> actionEmpresaLlistar();
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
        System.out.println(" [1] Whoami / Veure token");
        System.out.println(" [2] Eliminar la meva EMPRESA");
        System.out.println(" [3] Afegir producte");
        System.out.println(" [4] Eliminar producte");
        System.out.println(" [5] Llistar productes (tots)");
        System.out.println(" [6] Llistar productes de la meva empresa");
        System.out.println(" [9] Logout");
        System.out.println(" [0] Sortir");

        int op = askInt("Opcio: ", -1);
        switch (op) {
            case 1 -> actionWhoAmI();
            case 2 -> actionEliminarPropiCompte(); // esborra empresa + sessió
            case 3 -> actionProducteAdd();
            case 4 -> actionProducteDel();
            case 5 -> actionProducteLlistar();
            case 6 -> actionProducteLlistarEmpresa();
            case 9 -> actionLogout();
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
        for (ProducteDto p : arr) {
            System.out.printf("  [%s] %s  --  %.2f €%n", p.id, p.nom, p.preu ,
                    p.empresaCif != null ? "(" + p.empresaCif + ")" : "");
        }
    }

    private void printEmpreses(RespostaPeticio r) {
        Empresa[] arr = r.getData(0, Empresa[].class);
        if (arr == null || arr.length == 0) { System.out.println("(sense empreses)"); return; }
        System.out.println("___ Empreses ___");
        for (Empresa e : arr) {
            System.out.printf("   %s  --  %s%n", e.getCif(), e.getNom());
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
            out.println(json);

            String respostaJson = br.readLine();
            if (respostaJson == null) return null;

            return gson.fromJson(respostaJson, RespostaPeticio.class);
        } catch (IOException e) {
            System.out.println("Error de connexio: " + e.getMessage());
            return null;
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

