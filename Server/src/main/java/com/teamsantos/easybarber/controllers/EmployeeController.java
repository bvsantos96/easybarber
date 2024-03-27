package com.teamsantos.easybarber.controllers;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.services.ServiceService;
import com.teamsantos.easybarber.services.UserService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
    private final UserService userService;
    private final ServiceService serviceService;

    @Autowired
    public EmployeeController(UserService userService, ServiceService serviceService) {
        this.userService = userService;
        this.serviceService = serviceService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponseDTO> createEmployee(@RequestBody UserCreateDTO user, Principal principal) {
        try {
            if (principal != null && !userService.userChangePermissions(principal, user.getMobileInformation()))
                return ResponseEntity.badRequest().body(new BaseResponseDTO("You are not allowed to create this user"));
            userService.createUser(user, true);
            return ResponseEntity.ok(new BaseResponseDTO("Employee created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @PostMapping("/service")
    public ResponseEntity<BaseResponseDTO> createService(@RequestBody ServiceDTO service, Principal principal) {
        try {
            if (principal != null && userService.getUserId(principal).equals(service.getEmployeeId()))
                return ResponseEntity.badRequest()
                        .body(new BaseResponseDTO("You are not allowed to create this service"));
            serviceService.createService(service);
            return ResponseEntity.ok(new BaseResponseDTO("Service created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @PutMapping("/service")
    public ResponseEntity<BaseResponseDTO> updateService(@RequestBody ServiceDTO service, Principal principal) {
        try {
            if (principal != null && userService.getUserId(principal).equals(service.getEmployeeId()))
                return ResponseEntity.badRequest()
                        .body(new BaseResponseDTO("You are not allowed to update this service"));
            serviceService.updateService(service);
            return ResponseEntity.ok(new BaseResponseDTO("Service updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }
}
