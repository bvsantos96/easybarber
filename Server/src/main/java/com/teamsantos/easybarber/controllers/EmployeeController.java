package com.teamsantos.easybarber.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.EmployeeDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
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

    @GetMapping("/services")
    public ResponseEntity<BaseListDTO<ServiceDTO>> getServices(Principal principal) {
        try {
            return ResponseEntity.ok(new BaseListDTO<ServiceDTO>(serviceService.getServices(principal)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseListDTO<ServiceDTO>(e.getMessage()));
        }
    }

    @GetMapping("/{id}/services")
    public ResponseEntity<BaseListDTO<ServiceDTO>> getServices(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(new BaseListDTO<ServiceDTO>(serviceService.getServices(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseListDTO<ServiceDTO>(e.getMessage()));
        }
    }

    @GetMapping("/establishments")
    public ResponseEntity<BaseListDTO<EstablishmentDTO>> getEstablishments(Principal principal,
            @RequestParam(defaultValue = "false") boolean owned) {
        try {
            return ResponseEntity
                    .ok(new BaseListDTO<EstablishmentDTO>(userService.getEstablishments(principal, owned)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseListDTO<EstablishmentDTO>(e.getMessage()));
        }
    }

    @GetMapping("/{id}/establishments")
    public ResponseEntity<BaseListDTO<EstablishmentDTO>> getEstablishments(@PathVariable("id") Long id,
            @RequestParam(defaultValue = "false") boolean owned) {
        try {
            return ResponseEntity
                    .ok(new BaseListDTO<EstablishmentDTO>(userService.getEstablishments(id, owned)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseListDTO<EstablishmentDTO>(e.getMessage()));
        }
    }
}
