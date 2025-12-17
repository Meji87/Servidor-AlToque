
package com.altoque.altoque_server.gestor;

import com.altoque.altoque_server.Const;
import com.altoque.altoque_server.servidor.GestorException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 *
 * @author marc mestres mejias
 */
public class GestorXifrat {
    // 16 bytes = 128 bits
    private static final String SECRET_KEY = "0123456789abcdef"; // 16 caracters = 16 bytes
    private static final String INIT_VECTOR = "fedcba9876543210";

    private GestorXifrat() {}

    public static String xifrar(String text) throws GestorException {
        try{
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes()); //StandardCharsets.UTF_8
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES"); //getBytes(StandardCharsets.UTF_8)

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encriptat = cipher.doFinal(text.getBytes()); //StandardCharsets.UTF_8
            return Base64.getEncoder().encodeToString(encriptat);
        } catch(Exception e){
            throw new GestorException(Const.Missatge.ERR_XIFRANT_DADES, e);
        }
    }

    public static String desxifrar(String textXifrat) throws GestorException {
        try{
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes()); //StandardCharsets.UTF_8
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES"); //StandardCharsets.UTF_8

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] desencriptat = cipher.doFinal(Base64.getDecoder().decode(textXifrat));
            return new String(desencriptat); //, StandardCharsets.UTF_8
        }catch(Exception e){
            throw new GestorException(Const.Missatge.ERR_DESXIFRANT_DADES, e);
        }
        
        
    }
}
