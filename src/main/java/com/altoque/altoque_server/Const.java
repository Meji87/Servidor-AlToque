
package com.altoque.altoque_server;

/**
 *
 * @author marc mestres
 */
public final class Const {
    private Const() {} // Per evitar que no s'instanciï
    
    // Tipus de rol
    public static enum Rol{
        USUARI, EMPRESA;
        
        public boolean esUsuari()  { return this == USUARI; }
        public boolean esEmpresa() { return this == EMPRESA; }
    }

    // Tipus de petició 
    public static final class Peticio {
        public static final String LOGIN                    = "LOGIN";
        public static final String LOGOUT                   = "LOGOUT";
        public static final String USUARI_ADD               = "USUARI_ADD";
        public static final String USUARI_DEL               = "USUARI_DEL";
        public static final String EMPRESA_ADD              = "EMPRESA_ADD";
        public static final String EMPRESA_DEL              = "EMPRESA_DEL";
        public static final String EMPRESA_LLISTAR          = "EMPRESA_LLISTAR";
        public static final String PRODUCTE_ADD             = "PRODUCTE_ADD";
        public static final String PRODUCTE_DEL             = "PRODUCTE_DEL";
        public static final String PRODUCTE_LLISTAR         = "PRODUCTE_LLISTAR";
        
        private Peticio() {}
    }

    // Còdis de resposta
    public static final class Resposta {
        public static final int OK_RETURN_CODE    = 0;
        public static final int ERROR_RETURN_CODE = 1;
        private Resposta() {}
    }

    // Claves para datos/params en DadesPeticio / RespostaPeticio
    public static final class Dades {
        public static final String TOKEN        = "token";
        public static final String NOM_USUARI   = "nomusuari";
        public static final String CONTRASENYA  = "contrasenya";
        public static final String NOM          = "nom";
        public static final String COGNOMS      = "cognoms";
        public static final String CIF          = "cif";
        public static final String DESCRIPCIO   = "descripcio";
        public static final String PREU         = "preu";
        private Dades() {}
    }

    // Mensajes estándar (opcional, si quieres unificarlos)
    public static final class Missatge {
        public static final String ERR_PETICIO_DESCONEGUT   = "ERROR: Tipus de petició no suportat: ";
        public static final String ERR_PETICIO_INEXISTENT   = "ERROR: Petició buida";
        public static final String ERR_DESCONEGUT           = "ERROR: Desconegut: ";
        public static final String ERR_PARAMETRE_INEXISTENT = "ERROR: Faltan paràmetres";
        public static final String ERR_PARAMETRE_BUIT       = "ERROR: El pàrametre és buit: ";
        public static final String ERR_PARAMETRE_TIPUS      = "ERROR: Tipus de pàrametre incorrecte: ";
        public static final String ERR_TOKEN_INEXISTENT     = "ERROR: El token no existeix o és invalid";
        
        public static final String OK_SESSIO_TANCADA        = "OK: Sessió tancada correctament";
        public static final String ERR_SESSIO_INVALIDA      = "ERROR: Sessió no vàlida";
        
        public static final String OK_LOGIN_USUARI          = "OK: Usuari connectat correctament";
        public static final String OK_LOGIN_EMPRESA         = "OK: Empresa connectada correctament";
        public static final String ERR_LOGIN_USUARI         = "ERROR: Accés usuari denegat!";
        public static final String ERR_LOGIN_EMPRESA        = "ERROR: Accés empresa denegada!";
        public static final String ERR_BAD_PASS             = "ERROR: Contrasenya incorrecte";
        
        public static final String OK_USUARI_ADD            = "OK: Usuari afegit correctament";
        public static final String ERR_USUARI_ADD           = "ERROR: No s'ha pogut afegir l'usuari: ";
        public static final String OK_USUARI_DEL            = "OK: Usuari eliminat correctament";
        public static final String ERR_USUARI_DEL           = "ERROR: No s'ha pogut eliminar l'usuari: "; 
        public static final String ERR_USUARI_EXISTENT      = "ERROR: Usuari ja existent";
        public static final String ERR_USUARI_INEXISTENT    = "ERROR: L'usuari no existeix";
       
        
        public static final String OK_EMPRESA_ADD           = "OK: Empresa afegida correctament";
        public static final String ERR_EMPRESA_ADD          = "ERROR: No s'ha pogut afegir l'empresa: ";
        public static final String OK_EMPRESA_DEL           = "OK: Empresa eliminada correctament";
        public static final String ERR_EMPRESA_DEL          = "ERROR: No s'ha pogut eliminar l'empresa: "; 
        public static final String ERR_EMPRESA_EXISTENT     = "ERROR: Empresa ja existent";
        public static final String ERR_EMPRESA_INEXISTENT   = "ERROR: L'empresa no existeix";
        
        public static final String OK_PRODUCTE_ADD          = "OK: Producte afegit correctament";
        public static final String ERR_PRODUCTE_ADD         = "ERROR: No s'ha pogut afegir el producte";
        public static final String OK_PRODUCTE_DEL          = "OK: Producte eliminat correctament";
        public static final String OK_PRODUCTE_LIST         = "OK: Llistat de productes";
        public static final String ERR_PRODUCTE_INEXISTENT  = "ERROR: El producte no existeix";
        public static final String ERR_PRODUCTE_NO_TROBAT_PER_NOM = "ERROR: No s'ha trobat cap producte amb aquest NOM";
        public static final String ERR_PRODUCTE_PREU_INVALID= "ERROR: El preu del producte no es valid";
        
        private Missatge() {}
    }
    // Config de sessió u otras constantes transversales (opcional)
    public static final class Servidor {
        public static final int PORT_DEFECTE = 5050;
        private Servidor() {}
    }
    
    // Config de sessió u otras constantes transversales (opcional)
    public static final class Sessio {
        public static final int MINUTS_EXPIRACIO = 60;
        private Sessio() {}
    }
}

