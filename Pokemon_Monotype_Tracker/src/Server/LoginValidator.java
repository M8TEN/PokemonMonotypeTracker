package Server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class LoginValidator {
    public static byte[] getPasswordHash(String password) {
     try {
         MessageDigest digest = MessageDigest.getInstance("SHA-256");
         return digest.digest(password.getBytes(StandardCharsets.UTF_8));
     }
     catch (NoSuchAlgorithmException e) {
         return new byte[0];
     }
    }
}
