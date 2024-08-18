package com.teamsantos.easybarber.controllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.AppointmentDTO;
import com.teamsantos.easybarber.services.AppointmentService;

@Controller
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointment")
    public ResponseEntity<BaseResponseDTO> create(@RequestBody AppointmentDTO appointment, Principal principal) {
        try {
            return ResponseEntity.ok(new BaseResponseDTO(appointmentService.create(appointment, principal)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }
}
