package com.teamsantos.easybarber.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.services.EstablishmentService;
import com.teamsantos.easybarber.services.UserService;

@Controller
@RequestMapping("/establishment")
public class EstablishmentController {
    @Autowired
    private EstablishmentService establishmentService;

    @GetMapping("{id}")
    public ResponseEntity<BaseEstablishmentDTO> getEstablishment(@PathVariable Long id) {
        BaseEstablishmentDTO establishmentDTO = new BaseEstablishmentDTO();
        try {
            establishmentDTO = establishmentService.getEstablishment(id);
            return ResponseEntity.ok(establishmentDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(establishmentDTO);
        } catch (Exception e) {
            establishmentDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(establishmentDTO);
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
