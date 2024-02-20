package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired 
    private UserService userService;

    @GetMapping()
    public String getAllUsers() {
        userService.getAllUsers();
        return "Hello, World!";
    }

    @PostMapping()
    public String createUser(@RequestBody UserCreateDTO userDTO) {
        userService.createUser();
        return "Hello, World!";
    }

    @PutMapping()
    public String updateUser(@RequestBody UserCreateDTO userDTO) {
        userService.updateUser();
        return "Hello, World!";
    }

    @DeleteMapping()
    public String deleteUser(@RequestBody UserCreateDTO userDTO) {
        userService.deleteUser();
        return "Hello, World!";
    }

}
