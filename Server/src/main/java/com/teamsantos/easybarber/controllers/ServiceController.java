package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.ServiceTypeDTO;
import com.teamsantos.easybarber.DTO.ServiceWithImagesDTO;
import com.teamsantos.easybarber.DTO.filters.ServiceWithEmployeeFilter;
import com.teamsantos.easybarber.entities.Service;
import com.teamsantos.easybarber.entities.images.ServiceImage;
import com.teamsantos.easybarber.security.filters.ServiceSecurityExpressionRoot;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.services.ServiceService;

@Controller
@RequestMapping("/service")
public class ServiceController extends ImageController<Service, ServiceImage> {
    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        super(serviceService);
        this.serviceService = serviceService;
    }

    // TODO: We should think what permissions are needed to create a service type
    // Admin ? Employee ?
    // This will probably be used by the application as a filter, so we should
    // think about it
    @PostMapping
    @PreAuthorize(PrePermissionEvaluator.IS_SYSTEM_ADMIN)
    public ResponseEntity<BaseResponseDTO> createServiceType(@RequestBody ServiceTypeDTO serviceDTO) {
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

    @PutMapping("/{serviceId}")
    @PreAuthorize(PrePermissionEvaluator.IS_SYSTEM_ADMIN)
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

    @GetMapping("/types")
    public ResponseEntity<BaseListDTO<ServiceTypeDTO>> listTypes() {
        BaseListDTO<ServiceTypeDTO> response = new BaseListDTO<>();
        try {
            response.setItems(serviceService.listTypes());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<BasePageDTO<ServiceWithImagesDTO>> list(
            @RequestParam(name = "serviceType", required = false) Long serviceType,
            Pageable pageable) {
        BasePageDTO<ServiceWithImagesDTO> response = new BasePageDTO<>();
        try {
            ServiceWithEmployeeFilter filter = new ServiceWithEmployeeFilter();
            filter.setServiceTypeId(serviceType);
            response.setItems(serviceService.listServicesWithEmployee(filter, pageable));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Override
    public boolean canEdit(long entityId) {
        return ServiceSecurityExpressionRoot._hasServiceOwnerPermission(serviceService, entityId);
    }
}
