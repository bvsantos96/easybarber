package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.DTO.UserDTO;
import com.teamsantos.easybarber.DTO.UsersDTO;
import com.teamsantos.easybarber.services.UserService;

@Controller
public class UserController {
    
    @Autowired 
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<UsersDTO> getAllUsers() {
        UsersDTO response = new UsersDTO();
        try {
            response.setUsers(userService.getAllUsers());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
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

    @PutMapping("/user")
    public ResponseEntity<String> updateUser(@RequestBody UserCreateDTO userDTO) {
        try {
            userService.updateUser(userDTO);
            return ResponseEntity.ok("User updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user: " + e.getMessage());
        }
    }

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@RequestBody UserCreateDTO userDTO) {
        try {
            userService.deleteUser(userDTO);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user: " + e.getMessage());
        }
    }

}
