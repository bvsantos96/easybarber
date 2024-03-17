package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.services.UserService;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponseDTO> createEmployee(@RequestBody UserCreateDTO user) {
        try {
            userService.createUser(user, true);
            return ResponseEntity.ok(new BaseResponseDTO("Employee created successfully"));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO("Employee already exists"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }
}
