package com.arad.care4pets;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * helper class for password hashing and validation
 *
 */
public class PasswordUtils {


    // hashes plain text passwiord using sha256

    public static String hash(String plainText){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(plainText.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes){
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("SHA-256 not available",e );
        }
    }

    // returns tryue if plain text matches the hash
    public static boolean verify(String plainText, String storedHash){
        return hash(plainText).equals(storedHash);
    }

    // validate password strength
    // min 8 characters, 1 uppercase, 1 digit, 1 spercail character
    public static String validate(String password){
        if(password == null || password.length() < 8){
            return "Password must be at least 8 characters";
        }
        if(!password.matches(".*[A-Z].*")){
            return "Password must contain at least one uppercase letter";
        }
        if(!password.matches(".*[0-9].*")){
            return "Password must contain at least one number";
        }
        if(!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")){
            return "Password must contain at least one special character";
        }
        return  null;
    }
}
