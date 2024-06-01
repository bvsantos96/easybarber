package com.teamsantos.easybarber.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class TestController {
    @GetMapping("/test/user")
    public String getMyInfo(Authentication auth, Principal principal) {
        return "MobileInformation: " +
                principal.getName() +
                "\n" +
                "Authorities: " +
                auth.getAuthorities();
    }
}
