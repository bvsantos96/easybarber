package com.teamsantos.easybarber.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.EmployeeDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.EstablishmentServiceDTO;
import com.teamsantos.easybarber.exceptions.AlreadyExistsException;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.services.EstablishmentService;

import jakarta.websocket.server.PathParam;

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
        EstablishmentDTO establishments = new EstablishmentDTO();
        try {
            return ResponseEntity.ok(establishmentService.getEstablishment(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(establishments);
        } catch (Exception e) {
            establishments.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(establishments);
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

    // GET /establishments?page=0&size=15&sort=id,desc
    @GetMapping("/list")
    public ResponseEntity<BasePageDTO<EstablishmentDTO>> listEstablishments(
            @RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude, Pageable pageable) {
        BasePageDTO<EstablishmentDTO> listDTO = new BasePageDTO<>();
        try {
            listDTO.setItems(establishmentService.findByLocation(latitude, longitude, pageable));
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
            establishmentService.addEmployee(establishmentId, employeeId, principal);
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
    public ResponseEntity<BasePageDTO<com.teamsantos.easybarber.entities.EstablishmentService>> listServices(
            @PathVariable Long id, Pageable pageable) {
        BasePageDTO<com.teamsantos.easybarber.entities.EstablishmentService> listDTO = new BasePageDTO<>();
        try {
            listDTO.setItems(establishmentService.listServices(id, pageable));
            return ResponseEntity.ok(listDTO);
        } catch (Exception e) {
            listDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(listDTO);
        }
    }

    @PostMapping("/{establishmentId}/service/{serviceId}")
    @PreAuthorize(PrePermissionEvaluator.ESTABLISHMENT_ADMIN)
    public ResponseEntity<BaseResponseDTO> addService(@PathVariable Long establishmentId,
            @PathVariable Long serviceId) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            establishmentService.addService(establishmentId, serviceId);
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
            EstablishmentServiceDTO serviceDTO) {
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
}
