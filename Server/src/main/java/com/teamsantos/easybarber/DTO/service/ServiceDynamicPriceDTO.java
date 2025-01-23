package com.teamsantos.easybarber.DTO.service;

import java.time.LocalDateTime;

import com.teamsantos.easybarber.entities.EstablishmentService;
import com.teamsantos.easybarber.entities.EstablishmentServiceEmployee;
import com.teamsantos.easybarber.entities.ServiceDynamicPrice;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDynamicPriceDTO {
    private Long id;
    private Integer duration;
    private Double price;
    private Boolean usingDynamicPrice;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private Long establishmentServiceEmployeeId;
    private Long establishmentServiceId;

    public ServiceDynamicPriceDTO(Long id, Integer duration, Double price, Boolean usingDynamicPrice) {
        this.id = id;
        this.duration = duration;
        this.price = price;
        this.usingDynamicPrice = usingDynamicPrice || false;
    }

    public ServiceDynamicPrice toEntity(EntityManager entityManager) {
        return new ServiceDynamicPrice(id,
                entityManager.getReference(EstablishmentServiceEmployee.class, establishmentServiceEmployeeId),
                entityManager.getReference(EstablishmentService.class, establishmentServiceId),
                validFrom, validTo,
                price);
    }
}
