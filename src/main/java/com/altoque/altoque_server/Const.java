
package com.altoque.altoque_server;

/**
 *
 * @author mejia
 */
public final class Const {
    private Const() {} // evita instanciación

    // Tipos de petición que viajan por JSON
    public static final class Peticio {
        public static final String LOGIN_USUARI             = "LOGIN_USUARI";
        public static final String LOGIN_EMPRESA            = "LOGIN_EMPRESA";
        public static final String LOGOUT                   = "LOGOUT";
        public static final String USUARI_ADD               = "USUARI_ADD";
        public static final String USUARI_DEL               = "USUARI_DEL";
        public static final String EMPRESA_ADD              = "EMPRESA_ADD";
        public static final String EMPRESA_DEL              = "EMPRESA_DEL";
        public static final String PRODUCTE_ADD             = "PRODUCTE_ADD";
        public static final String PRODUCTE_DEL             = "PRODUCTE_DEL";
        public static final String PRODUCTE_LLISTAR         = "PRODUCTE_LLISTAR";
        public static final String PRODUCTE_EMPRESA_LLISTAR = "PRODUCTE_EMPRESA_LLISTAR";
        private Peticio() {}
    }

    // Códigos de respuesta
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
        public static final String ERR_PETICIO_INEXISTENT   = "ERROR: Tipus de petició no suportat";
        public static final String ERR_DESCONEGUT           = "ERROR: Desconegut: ";
        public static final String ERR_PARAMETRE_INEXISTENT = "ERROR: Falta el parametre: ";
        public static final String ERR_PARAMETRE_BUIT       = "ERROR: El pàrametre és buit: ";
        
        public static final String OK_SESSIO_TANCADA        = "OK: Sessió tancada correctament";
        public static final String ERR_SESSIO_INVALIDA      = "ERROR: Sessió no vàlida";
        
        public static final String OK_LOGIN_USUARI          = "OK: Usuari connectat correctament";
        public static final String OK_LOGIN_EMPRESA         = "OK: Empresa connectada correctament";
        public static final String ERR_LOGIN_USUARI         = "ERROR: Accés usuari denegat!";
        public static final String ERR_LOGIN_EMPRESA        = "ERROR: Accés empresa denegada!";
        
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
        
        
        
        private Missatge() {}
    }

    // Config de sessió u otras constantes transversales (opcional)
    public static final class Sessio {
        public static final int MINUTS_EXPIRACIO = 60;
        private Sessio() {}
    }
}

