package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.services.UserService;

@Controller
public class EmployeeController {

    @Autowired
    private UserService UserService;

    public ResponseEntity<BaseResponseDTO> createEmployee(@RequestBody UserCreateDTO user) {
        try {
            UserService.createUser(user, true);
            return ResponseEntity.ok(new BaseResponseDTO("Employee created successfully"));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO("Employee already exists"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO("Error creating employee"));
        }
    }
}
