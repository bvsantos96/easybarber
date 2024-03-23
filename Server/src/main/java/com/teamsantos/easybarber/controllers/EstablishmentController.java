package com.teamsantos.easybarber.controllers;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.services.EstablishmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/establishment")
public class EstablishmentController {
    private EstablishmentService establishmentService;

    @Autowired
    public EstablishmentController(EstablishmentService establishmentService) {
        this.establishmentService = establishmentService;
    }

    @GetMapping("{id}")
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
    public ResponseEntity<BaseEstablishmentDTO> createEstablishment(@RequestBody BaseEstablishmentDTO establishmentDTO,
            Principal principal) {
        try {
            establishmentService.create(establishmentDTO, principal);
            return ResponseEntity.ok(establishmentDTO);
        } catch (Exception e) {
            establishmentDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(establishmentDTO);
        }
    }

    // GET /establishments?page=0&size=15&sort=id,desc
    @GetMapping("/list")
    public ResponseEntity<BaseListDTO<BaseEstablishmentDTO>> listEstablishments(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude) {
        BaseListDTO<BaseEstablishmentDTO> listDTO = new BaseListDTO<BaseEstablishmentDTO>();
        try {
            listDTO.setItems(establishmentService.findByLocation(latitude, longitude, null));
            return ResponseEntity.ok(listDTO);
        } catch (Exception e) {
            listDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(listDTO);
        }
    }
    @PostMapping("/test")
    public ResponseEntity<BaseListDTO<BaseEstablishmentDTO>> listEstablishments(@RequestBody LocationDTO locationDTO) {
        double latitude = locationDTO.getLatitude();
        double longitude = locationDTO.getLongitude();

        BaseListDTO<BaseEstablishmentDTO> listDTO = new BaseListDTO<>();
        try {
            listDTO.setItems(establishmentService.findByLocation(latitude, longitude, null));
            return ResponseEntity.ok(listDTO);
        } catch (Exception e) {
            listDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(listDTO);
        }
    }

    @PostMapping("/{id}/employee")
    @PreAuthorize("hasPermission(#establishmentId, 'ESTABLISHMENT-ADMIN')")
    public ResponseEntity<BaseResponseDTO> addEmployee(@PathVariable("id") Long establishmentId,
            @RequestBody Long userId,
            Principal principal) {
        BaseResponseDTO responseDTO = new BaseResponseDTO();
        try {
            establishmentService.addEmployee(establishmentId, userId, principal);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            responseDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
    class LocationDTO {
        private double latitude;
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }
}
