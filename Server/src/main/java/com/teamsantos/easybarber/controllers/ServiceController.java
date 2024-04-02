package com.teamsantos.easybarber.controllers;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.ServiceTypeDTO;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/service")
public class ServiceController {
    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    // TODO: We should thing what permissions are needed to create a service type
    // Admin ? Employee ?
    // This will probably be used by the application as a filter, so we should
    // think about it
    @PostMapping
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> createServiceType(@RequestBody ServiceTypeDTO serviceDTO,
            Principal principal) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            serviceService.createType(serviceDTO);
            response.setResponseMessage("Service type created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
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
