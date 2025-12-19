
package com.altoque.altoque_server.gestor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Gestió de contrasenyes amb BCrypt
 * no s'utlitza algoritmes rapids com MD5 o SHA1 (vulnerables força bruta)
 * BCrypt es lent i utilitza salt a l'inici
 * 
 * @author marc mestres
 */
@Component
public class GestorContrasenyes {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String hash(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean comprovar(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}
