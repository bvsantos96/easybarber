package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.ServiceTypeDTO;
import com.teamsantos.easybarber.security.services.RolePermissionEvaluator;
import com.teamsantos.easybarber.services.ServiceService;

@Controller
@RequestMapping("/service")
public class ServiceController {
    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping
    @PreAuthorize(RolePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> createServiceType(@RequestBody ServiceTypeDTO serviceDTO) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            serviceService.createType(serviceDTO);
            response.setResponseMessage("Service type created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    @PreAuthorize(RolePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> updateServiceType(@RequestBody ServiceTypeDTO serviceDTO) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            serviceService.updateType(serviceDTO);
            response.setResponseMessage("Service type created successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
