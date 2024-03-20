package com.teamsantos.easybarber.security.utils;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoding {
    private static final PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    public static String encode(String cleanTxt) {
        return getPasswordEncoder().encode(cleanTxt);
    }

    public static PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
