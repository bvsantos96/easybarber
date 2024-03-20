package com.teamsantos.easybarber.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello, World!";
    }

    @GetMapping("/test/user")
    public String getMyInfo(Authentication auth, Principal principal) {
        return new StringBuilder()
                .append("MobileInformation: ")
                .append(principal.getName())
                .append("\n")
                .append("Authorities: ")
                .append(auth.getAuthorities())
                .toString();
    }
}
