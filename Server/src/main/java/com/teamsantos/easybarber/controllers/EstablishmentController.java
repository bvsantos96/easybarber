package com.teamsantos.easybarber.controllers;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.services.EstablishmentService;
import com.teamsantos.easybarber.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/establishment")
public class EstablishmentController {
    @Autowired
    private EstablishmentService establishmentService;
    @Autowired
    private UserService userService;

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
    public ResponseEntity<BaseEstablishmentDTO> createEstablishment(@RequestBody BaseEstablishmentDTO establishmentDTO, Principal principal) {
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
    public ResponseEntity<BaseListDTO<BaseEstablishmentDTO>> listEstablishments(Pageable pageable) {
        BaseListDTO<BaseEstablishmentDTO> listDTO = new BaseListDTO<BaseEstablishmentDTO>();
        try {
            listDTO.setItems(establishmentService.findAllBase(pageable));
            return ResponseEntity.ok(listDTO);
        } catch (Exception e) {
            listDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(listDTO);
        }
    }
}
