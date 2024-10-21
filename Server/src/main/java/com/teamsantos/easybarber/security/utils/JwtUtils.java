package com.teamsantos.easybarber.security.utils;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;

import com.teamsantos.easybarber.exceptions.InvalidTokenException;

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

    public Claims validateToken(String token) {
        try {
            return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            return null;
        }
    }

    public UserPrincipal parseToken(Claims claims) throws InvalidTokenException, IllegalArgumentException {
        String[] split = claims.getSubject().split(";");
        try {
            if (claims.containsKey("exp")) {
                long exp = (long) claims.get("exp");
                if (exp < System.currentTimeMillis())
                    throw new InvalidTokenException();
            } else {
                throw new InvalidTokenException();
            }
            if (claims.containsKey("roles"))
                return new UserPrincipal(Long.parseLong(split[0]), split.length > 1 ? Long.parseLong(split[1]) : null,
                        (List<String>) claims.get("roles", List.class));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new IllegalArgumentException("Token does not contain roles");
        }
        return null;
    }

    public String generateToken(long idUser, Long idEmployee, Set<String> roles) {
        String subject = idEmployee != null
                ? String.format("%d;%d", idUser, idEmployee)
                : String.format("%d", idUser);

        return Jwts.builder()
                .subject(subject)
                .claim("roles", roles)
                .expiration(new Date(System.currentTimeMillis() + getExpirationTime()))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateToken(UserPrincipal userDetails) {
        return generateToken(userDetails.getId(), userDetails.getEmployeeId(), Set.of(
                userDetails.getRoles().stream().map(a -> a.getAuthority().substring(5)).toArray(String[]::new)));
    }

    public boolean isTokenExpiringSoon(Claims claims) {
        Date expiration = claims.getExpiration();
        long timeToExpire = expiration.getTime() - System.currentTimeMillis();
        return timeToExpire < TimeUnit.MINUTES.toMillis(5);
    }
}
