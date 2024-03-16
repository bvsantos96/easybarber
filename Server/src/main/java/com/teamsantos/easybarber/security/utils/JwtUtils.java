package com.teamsantos.easybarber.security.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtils {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expirationTime}")
    private String expirationTime;

    private long EXPIRATION_TIME = 0L;
    private SecretKey SECRET_KEY;

    public JwtUtils() {
    }

    private long getExpirationTime() {
        if (EXPIRATION_TIME == 0L)
            EXPIRATION_TIME = Long.parseLong(expirationTime);
        return EXPIRATION_TIME;
    }

    private SecretKey getSecretKey() {
        if (SECRET_KEY == null) {
            SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes());
        }
        return SECRET_KEY;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    public String extractMobileNumber(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String generateToken(String mobileNumber) {
        return Jwts.builder()
                .subject(mobileNumber)
                .expiration(new Date(System.currentTimeMillis() + getExpirationTime()))
                .signWith(getSecretKey())
                .compact();
    }
}
