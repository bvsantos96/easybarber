package com.teamsantos.easybarber.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.repositories.establishmentServices.EstablishmentServiceEmployeeRepository;
import com.teamsantos.easybarber.repositories.services.ServiceDynamicPriceRepository;

@Service
public class ServiceDynamicPriceService {
    private final ServiceDynamicPriceRepository serviceDynamicPriceRepository;
    private final EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository;

    @Autowired
    public ServiceDynamicPriceService(ServiceDynamicPriceRepository serviceDynamicPriceRepository,
            EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository) {
        this.serviceDynamicPriceRepository = serviceDynamicPriceRepository;
        this.establishmentServiceEmployeeRepository = establishmentServiceEmployeeRepository;
    }

    public Double validate(long establishmentServiceId, Long establishmentStaffId,
            LocalDate date,
            LocalTime time) {
        Long establishmentServiceEmployeeId = establishmentStaffId == null ? null
                : establishmentServiceEmployeeRepository.getIdByEstablishmentServiceIdAndEstablishmentStaffId(
                        establishmentServiceId,
                        establishmentStaffId);
        return serviceDynamicPriceRepository.getDynamicPrice(establishmentServiceId,
                establishmentServiceEmployeeId,
                LocalDateTime.of(date, time));
    }
}
