package com.teamsantos.easybarber.controllers;

import com.teamsantos.easybarber.DTO.*;
import com.teamsantos.easybarber.exceptions.AlreadyExistsException;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.services.EstablishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/establishment")
public class EstablishmentController {
    private final EstablishmentService establishmentService;

    @Autowired
    public EstablishmentController(EstablishmentService establishmentService) {
        this.establishmentService = establishmentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstablishmentDTO> getEstablishment(@PathVariable Long id) {
        EstablishmentDTO establishment = new EstablishmentDTO();
        try {
            return ResponseEntity.ok(establishmentService.getEstablishment(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(establishment);
        } catch (Exception e) {
            establishment.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(establishment);
        }
    }

    @PostMapping
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseEstablishmentDTO> createEstablishment(@RequestBody BaseEstablishmentDTO establishmentDTO,
            Principal principal) {
        try {
            establishmentService.create(establishmentDTO, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body(establishmentDTO);
        } catch (AlreadyExistsException e) {
            establishmentDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FOUND).body(establishmentDTO);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            establishmentDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(establishmentDTO);
        }
    }

    // GET /establishments/list?page=0&size=15&sort=id,desc
    @GetMapping("/list")
    public ResponseEntity<BasePageDTO<EstablishmentDTO>> listEstablishments(
            @RequestParam(name = "latitude", required = false) Double latitude,
            @RequestParam(name = "longitude", required = false) Double longitude,
            @RequestParam(name = "serviceType", required = false) Long serviceType, Pageable pageable) {
        BasePageDTO<EstablishmentDTO> listDTO = new BasePageDTO<>();
        try {
            listDTO.setItems(establishmentService.findByLocation(latitude, longitude, serviceType, pageable));
            return ResponseEntity.ok(listDTO);
        } catch (Exception e) {
            listDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(listDTO);
        }
    }

    @PostMapping("/{establishmentId}/employee/{employeeId}")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> addEmployee(@PathVariable("establishmentId") Long establishmentId,
            @PathVariable Long employeeId,
            Principal principal) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            establishmentService.addEmployee(establishmentId, employeeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (UserAlreadyExistsException e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FOUND).body(responseDTO);
        } catch (Exception e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/{id}/services")
    public ResponseEntity<BasePageDTO<ServiceDTO>> listServices(
            @PathVariable Long id, Pageable pageable) {
        BasePageDTO<ServiceDTO> listDTO = new BasePageDTO<>();
        try {
            listDTO.setItems(establishmentService.listServices(id, pageable));
            return ResponseEntity.ok(listDTO);
        } catch (Exception e) {
            listDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(listDTO);
        }
    }

    @PostMapping("/{establishmentId}/service")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> addService(@PathVariable Long establishmentId,
            @RequestBody CreateEstablishmentServiceDTO serviceDTO) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            establishmentService.addService(establishmentId, serviceDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (AlreadyExistsException e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FOUND).body(responseDTO);
        } catch (Exception e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PutMapping("/{establishmentId}/service")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> updateService(@PathVariable Long establishmentId,
            CreateEstablishmentServiceDTO serviceDTO) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            establishmentService.updateService(establishmentId, serviceDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @DeleteMapping("/{establishmentId}/service/{serviceId}")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> removeService(@PathVariable Long establishmentId,
            @PathVariable Long serviceId) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            establishmentService.removeService(establishmentId, serviceId);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @GetMapping("/{establishmentId}/employees")
    public ResponseEntity<BaseListDTO<EmployeeDTO>> listEmployees(@PathVariable Long establishmentId,
            @RequestParam(defaultValue = "true") boolean onlyActive) {
        BaseListDTO<EmployeeDTO> response = new BaseListDTO<>();
        try {
            response.setItems(establishmentService.getEmployees(establishmentId, onlyActive));
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            response.setResponseMessage(String.format("Establishment with id %d not found", establishmentId));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception ex) {
            response.setResponseMessage(ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping(path = "/{establishmentId}/images", consumes = "application/json")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> addImages(@PathVariable("establishmentId") Long establishmentId,
            @RequestBody List<ImageDTO> images) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            if (images.isEmpty()) {
                response.setResponseMessage("Images list is empty");
                return ResponseEntity.badRequest().body(response);
            }
            establishmentService.saveImages(establishmentId, images);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NotFoundException e) {
            System.err.println(e.getMessage());
            response.setResponseMessage("Establishment not found");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
