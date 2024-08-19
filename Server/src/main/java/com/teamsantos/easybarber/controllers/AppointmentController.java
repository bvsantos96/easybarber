package com.teamsantos.easybarber.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.filters.AppointmentFilter;
import com.teamsantos.easybarber.DTO.AppointmentDTO;
import com.teamsantos.easybarber.services.AppointmentService;

@Controller
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointment")
    public ResponseEntity<BaseResponseDTO> create(@RequestBody AppointmentDTO appointment, Principal principal) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new BaseResponseDTO(appointmentService.create(appointment, principal)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @GetMapping("/appointments")
    public ResponseEntity<BasePageDTO<AppointmentDTO>> listSchedules(@ModelAttribute AppointmentFilter filter,
            Pageable pageable) {
        try {
            BasePageDTO<AppointmentDTO> appointments = appointmentService.listAppointment(filter, pageable);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }
}
