package com.teamsantos.easybarber.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentCountDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentListDTO;
import com.teamsantos.easybarber.DTO.appointment.CancelAppointmentDTO;
import com.teamsantos.easybarber.DTO.filters.AppointmentFilter;
import com.teamsantos.easybarber.exceptions.ForbidenException;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.services.AppointmentService;

@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/appointment")
    public ResponseEntity<BaseResponseDTO> create(@RequestBody AppointmentDTO appointment) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new BaseResponseDTO(appointmentService.create(appointment)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @GetMapping("/appointments")
    public ResponseEntity<BasePageDTO<AppointmentDTO>> listAppointments(@ModelAttribute AppointmentFilter filter,
            Pageable pageable) {
        try {
            BasePageDTO<AppointmentDTO> appointments = appointmentService.listAppointment(filter, pageable);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @GetMapping("/appointment/count")
    public ResponseEntity<AppointmentCountDTO> countAppointments(@RequestParam(required = false) Boolean userView) {
        try {
            if (userView == null) {
                userView = true;
            }
            return ResponseEntity.ok(appointmentService.countAppointments(userView));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AppointmentCountDTO(e.getMessage()));
        }
    }

    @GetMapping("/appointment/list")
    public ResponseEntity<BasePageDTO<AppointmentListDTO>> listAppointmentsBase(
            @ModelAttribute AppointmentFilter filter,
            Pageable pageable) {
        try {
            if (filter.getUserView() == null) {
                filter.setUserView(true);
            }
            if (filter.getFuture() == null) {
                filter.setFuture(true);
            }
            if (filter.getUserView()) {
                if (filter.getClientId() != null && filter.getClientId() != 0
                        && filter.getClientId() != UserContext.getUserId()) {
                    throw new ForbidenException("User");
                }
                filter.setClientId(UserContext.getUserId());
            } else {
                if (filter.getEmployeeId() == null && filter.getEmployeeId() == 0
                        && filter.getEmployeeId() != UserContext.getEmployeeId()) {
                    throw new ForbidenException("Employee");
                }
                filter.setEmployeeId(UserContext.getEmployeeId());
            }
            BasePageDTO<AppointmentListDTO> appointments = appointmentService.listAppointmentBase(filter, pageable);
            return ResponseEntity.ok(appointments);
        } catch (ForbidenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BasePageDTO<>(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BasePageDTO<>(e.getMessage()));
        }
    }

    @PutMapping("/appointment/cancel")
    @PreAuthorize(PrePermissionEvaluator.HAS_APPOINTMENT_CHANGE_PERMISSION_OBJECT)
    public ResponseEntity<BaseResponseDTO> cancel(@RequestBody CancelAppointmentDTO cancelAppointmentDTO) {
        try {
            appointmentService.cancel(cancelAppointmentDTO);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Appointment canceled"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @PutMapping("/appointment/{id}/confirm")
    @PreAuthorize(PrePermissionEvaluator.IS_EMPLOYEE)
    public ResponseEntity<BaseResponseDTO> confirm(@PathVariable long id) {
        try {
            appointmentService.confirm(id);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Appointment canceled"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @GetMapping("/appointment/{id}/feedback/{feedback}")
    public ResponseEntity<BaseResponseDTO> feedback(@PathVariable long id, @PathVariable int feedback) {
        try {
            appointmentService.feedback(id, feedback);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Feedback sent"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }
}
