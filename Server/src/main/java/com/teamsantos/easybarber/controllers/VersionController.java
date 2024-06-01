package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/version")
public class VersionController {
    @Value("${app.data.version}")
    private String version;

    @GetMapping
    public ResponseEntity<String> getVersion() {
        return ResponseEntity.ok(version);
    }
}
