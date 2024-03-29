package com.teamsantos.easybarber.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.EmployeeDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.services.ServiceService;
import com.teamsantos.easybarber.services.UserService;

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
    public ResponseEntity<BaseResponseDTO> createEmployee(@RequestBody EmployeeDTO employee, Principal principal) {
        try {
            if (principal != null && !userService.userChangePermissions(principal, employee.getMobileInformation()))
                return ResponseEntity.badRequest().body(new BaseResponseDTO("You are not allowed to create this user"));
            userService.createUser(employee, true);
            return ResponseEntity.ok(new BaseResponseDTO("Employee created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @PostMapping("/service")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> createService(@RequestBody ServiceDTO service, Principal principal) {
        try {
            serviceService.createService(service, principal);
            return ResponseEntity.ok(new BaseResponseDTO("Service created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @PutMapping("/service")
    @PreAuthorize(PrePermissionEvaluator.SERVICE_OWNER)
    public ResponseEntity<BaseResponseDTO> updateService(@RequestBody ServiceDTO service, Principal principal) {
        try {
            serviceService.updateService(service);
            return ResponseEntity.ok(new BaseResponseDTO("Service updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }
}
