package com.teamsantos.easybarber.security.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtUtils {
    // TODO: This needs to be extracted from the code and stored in a secure location
    // TODO: Use a more secure key type RSA-4096 perhaps
    private static final SecretKey SECRET_KEY = new SecretKeySpec("m3Z9xfx_6Kyv2D0s7l79xgDKQyu-1zEA-wOEu3Us3_LqIIIYTEuM6q9Slhcmtc2oYChwdQC2ohcWck3KEtO2-Q".getBytes(), SignatureAlgorithm.HS512.getJcaName());
    private static final long EXPIRATION_TIME = 30 * 24 * 60 * 60 * 1000L; // 30 days

    public static boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    public static String extractMobileNumber(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build().parseSignedClaims(token).getBody().getSubject();
    }

    public static String generateToken(String mobileNumber) {
        return Jwts.builder()
                .setSubject(mobileNumber)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
