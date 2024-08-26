package com.teamsantos.easybarber.security.utils;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;

public class PasswordEncoding {
    @Getter
    private static final PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    public static String encode(String cleanTxt) {
        return getPasswordEncoder().encode(cleanTxt);
    }

}
