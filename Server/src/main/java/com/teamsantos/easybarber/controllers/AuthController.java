package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.DTO.UserDTO;
import com.teamsantos.easybarber.DTO.UsersDTO;
import com.teamsantos.easybarber.services.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserCreateDTO userDTO) {
        try {
            return ResponseEntity.ok(userService.loginUser(userDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO userDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userDTO));
        } catch (Exception e) {
            UserDTO response = new UserDTO();
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
