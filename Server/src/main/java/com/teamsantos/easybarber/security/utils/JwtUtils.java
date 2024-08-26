package com.teamsantos.easybarber.security.utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

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

    public UserPrincipal parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String[] split = claims.getSubject().split(";");
        return new UserPrincipal(Long.parseLong(split[0]), split.length > 1 ? Long.parseLong(split[1]) : null,
                (boolean[]) claims.getOrDefault("roles", new boolean[0]));
    }

    public String generateToken(long idUser, Long idEmployee, boolean[] roles) {
        return Jwts.builder()
                .subject(String.format("%d;%d", idUser, idEmployee == null ? -1 : idEmployee))
                .claim("roles", roles)
                .expiration(new Date(System.currentTimeMillis() + getExpirationTime()))
                .signWith(getSecretKey())
                .compact();
    }
}
