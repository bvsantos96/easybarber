package com.teamsantos.easybarber.controllers;

import java.util.Set;

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

import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentCountDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentListDTO;
import com.teamsantos.easybarber.DTO.appointment.CancelAppointmentDTO;
import com.teamsantos.easybarber.DTO.appointment.FeedbackDTO;
import com.teamsantos.easybarber.DTO.filters.AppointmentFilter;
import com.teamsantos.easybarber.DTO.filters.ProductRequestFilter;
import com.teamsantos.easybarber.DTO.product.ProductRequestsDTO;
import com.teamsantos.easybarber.exceptions.ForbidenException;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.services.AppointmentService;
import com.teamsantos.easybarber.services.ProductService;
import com.teamsantos.easybarber.utils.Pair;

@RestController
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final ProductService productService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, ProductService productService) {
        this.appointmentService = appointmentService;
        this.productService = productService;
    }

    @PostMapping("/appointment")
    public ResponseEntity<BaseResponseDTO> create(@RequestBody AppointmentDTO appointment) {
        try {
            Pair<Long, Double> appointmentIdAndPrice = appointmentService.create(appointment);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new BaseResponseDTO(appointmentIdAndPrice.getFirst(),
                            appointmentIdAndPrice.getSecond() != null
                                    ? String.format(
                                            "Please note that due to a high volume of appointments on the selected day, the price for this service has been adjusted to %.2f€. Would you like to proceed with scheduling this appointment?",
                                            appointmentIdAndPrice.getSecond())
                                    : null));
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
    public ResponseEntity<AppointmentCountDTO> countAppointments(@RequestParam(required = false) Boolean userView,
            @RequestParam(required = false) Long establishmentId) {
        try {
            if (userView == null) {
                userView = true;
            }
            return ResponseEntity.ok(appointmentService.countAppointments(userView, establishmentId));
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
            if (filter.getUserView()) {
                if (filter.getFuture() == null) {
                    filter.setFuture(true);
                }
                if (filter.getClientId() != null && filter.getClientId() != 0
                        && filter.getClientId() != UserContext.getUserId()) {
                    throw new ForbidenException("User");
                }
                filter.setClientId(UserContext.getUserId());
            } else {
                if (filter.getEmployeeId() != null && filter.getEmployeeId() != 0
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

    @PostMapping("/appointment/{id}/feedback/{feedback}")
    public ResponseEntity<BaseResponseDTO> feedback(@PathVariable long id, @PathVariable int feedback) {
        try {
            appointmentService.feedback(id, feedback);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Feedback sent"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @GetMapping("/appointments/feedback")
    public ResponseEntity<FeedbackDTO> feedbackAsked() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(appointmentService.feedbackAsked());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new FeedbackDTO());
        }
    }

    @GetMapping("/appointments/validate")
    public ResponseEntity<String> validateAppointments(@RequestParam(required = false) Boolean userView) {
        try {
            if (userView == null) {
                userView = true;
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .body(appointmentService.validateAppointments(userView));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("");
        }
    }

    @PostMapping("/{appointmentId}/suggest/product")
    @PreAuthorize(PrePermissionEvaluator.HAS_APPOINTMENT_CHANGE_PERMISSION)
    public ResponseEntity<BaseResponseDTO> suggestProducts(@RequestBody Set<Long> productIds,
            @PathVariable("appointmentId") Long appointmentId) {
        try {
            Long userId = appointmentService.getUserIdByAppointmentId(appointmentId);
            if (userId == null) {
                return ResponseEntity.badRequest().body(new BaseResponseDTO("User of appointment not found"));
            }
            productService.addSuggestionToClient(productIds, userId);
            return ResponseEntity.ok(new BaseResponseDTO("Products suggested successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @PostMapping("/{appointmentId}/request/product")
    @PreAuthorize(PrePermissionEvaluator.HAS_APPOINTMENT_CHANGE_PERMISSION)
    public ResponseEntity<BaseResponseDTO> requestProducts(@RequestBody Set<Long> productIds,
            @PathVariable("appointmentId") Long appointmentId) {
        try {
            productService.requestProduct(productIds, appointmentId);
            return ResponseEntity.ok(new BaseResponseDTO("Products requested successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    @PostMapping("/{appointmentId}/products")
    @PreAuthorize(PrePermissionEvaluator.HAS_APPOINTMENT_CHANGE_PERMISSION)
    public ResponseEntity<BaseListDTO<ProductRequestsDTO>> getProducts(
            @PathVariable("appointmentId") Long appointmentId) {
        try {
            ProductRequestFilter filter = new ProductRequestFilter();
            filter.setAppointmentId(appointmentId);
            return ResponseEntity.ok(new BaseListDTO<>(appointmentService.getProductRequests(filter)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseListDTO<>(e.getMessage()));
        }
    }
}
