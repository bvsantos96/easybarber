package com.teamsantos.easybarber.controllers;

import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.DTO.UsersDTO;
import com.teamsantos.easybarber.services.EstablishmentService;
import com.teamsantos.easybarber.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
public class UserController {
    private EstablishmentService establishmentService;
    private UserService userService;

    @Autowired
    public UserController(EstablishmentService establishmentService, UserService userService) {
        this.establishmentService = establishmentService;
        this.userService = userService;
    }
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

    @GetMapping("/user/establishment/list")
    public ResponseEntity<BaseListDTO<EstablishmentDTO>> getEstablishment(Principal principal) {
        BaseListDTO<EstablishmentDTO> establishments = new BaseListDTO<>();
        try {
            establishments.setItems(establishmentService.listUserEstablishments(userService.getUserId(principal)));
            return ResponseEntity.ok(establishments);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(establishments);
        } catch (Exception e) {
            establishments.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(establishments);
        }
    }
}
