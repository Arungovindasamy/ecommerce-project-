package com.example.project001a.security;

import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class KeyGen {
    public static void main(String[] args) {
        byte[] key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(key);
        System.out.println("JWT Secret: " + base64Key);
    }
}