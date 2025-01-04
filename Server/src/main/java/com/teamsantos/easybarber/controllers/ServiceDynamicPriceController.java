package com.teamsantos.easybarber.controllers;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.filters.ServiceDynamicFilter;
import com.teamsantos.easybarber.DTO.service.ServiceDynamicPriceDTO;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.services.ServiceDynamicPriceService;

@RestController
@RequestMapping("/dynamicprice")
public class ServiceDynamicPriceController {
    private final ServiceDynamicPriceService serviceDynamicPriceService;

    @Autowired
    public ServiceDynamicPriceController(ServiceDynamicPriceService serviceDynamicPriceService) {
        this.serviceDynamicPriceService = serviceDynamicPriceService;
    }

    @PostMapping
    @PreAuthorize(PrePermissionEvaluator.CAN_WRITE_SERVICE_DYNAMIC_PRICE)
    public ResponseEntity<Long> create(@RequestBody ServiceDynamicPriceDTO serviceDynamicPriceDTO) {
        try {
            return ResponseEntity.ok(serviceDynamicPriceService.create(serviceDynamicPriceDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping
    @PreAuthorize(PrePermissionEvaluator.CAN_WRITE_SERVICE_DYNAMIC_PRICE)
    public ResponseEntity<Long> update(@RequestBody ServiceDynamicPriceDTO serviceDynamicPriceDTO) {
        try {
            return ResponseEntity.ok(serviceDynamicPriceService.update(serviceDynamicPriceDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping
    @PreAuthorize(PrePermissionEvaluator.CAN_WRITE_SERVICE_DYNAMIC_PRICE)
    public ResponseEntity<BaseResponseDTO> delete(@RequestParam Long id) {
        try {
            serviceDynamicPriceService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    @PreAuthorize(PrePermissionEvaluator.CAN_READ_SERVICE_DYNAMIC_PRICE)
    public ResponseEntity<BasePageDTO<ServiceDynamicPriceDTO>> findAll(
            @ModelAttribute ServiceDynamicFilter filter,
            Pageable pageable) {
        try {
            return ResponseEntity.ok(serviceDynamicPriceService.findAll(filter, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<Double> validate(@RequestParam Long establishmentServiceId,
            @RequestParam(required = false) Long establishmentStaffId,
            @RequestParam LocalDate date, @RequestParam LocalTime time) {
        try {
            return ResponseEntity.ok(serviceDynamicPriceService.validate(establishmentServiceId,
                    establishmentStaffId, date, time));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

    }
}
