package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.services.EstablishmentService;

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
    public ResponseEntity<BaseEstablishmentDTO> createEstablishment(@RequestBody BaseEstablishmentDTO establishmentDTO) {
        try {
            establishmentDTO = establishmentService.createEstablishment(establishmentDTO);
            return ResponseEntity.ok(establishmentDTO);
        } catch (Exception e) {
            establishmentDTO.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(establishmentDTO);
        }
    }
}
