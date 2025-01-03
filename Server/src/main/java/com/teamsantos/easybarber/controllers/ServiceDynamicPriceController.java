package com.teamsantos.easybarber.controllers;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.services.ServiceDynamicPriceService;

@RestController
@RequestMapping("/dynamicprice")
public class ServiceDynamicPriceController {
    private final ServiceDynamicPriceService serviceDynamicPriceService;

    @Autowired
    public ServiceDynamicPriceController(ServiceDynamicPriceService serviceDynamicPriceService) {
        this.serviceDynamicPriceService = serviceDynamicPriceService;
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
