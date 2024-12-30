package com.teamsantos.easybarber.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Entity
@Data
@Table(name = "service_dynamic_price")
public class ServiceDynamicPrice {
    @ManyToOne
    @JoinColumn(name = "establishment_service_employee_id", referencedColumnName = "id")
    private EstablishmentServiceEmployee establishmentServiceEmployee;

    @ManyToOne
    @JoinColumn(name = "establishment_service_id", referencedColumnName = "id")
    private EstablishmentService establishmentService;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private LocalDateTime from;

    @Column(nullable = false)
    private LocalDateTime to;

    @AssertTrue(message = "Either establishmentServiceEmployee or establishmentService must be set")
    private boolean isValidReference() {
        return establishmentServiceEmployee != null || establishmentService != null;
    }

    @Embeddable
    @Data
    public static class ServiceDynamicPriceId implements Serializable {
        private Long establishmentServiceEmployeeId;
        private Long establishmentServiceId;
        private LocalDateTime validFrom;
        private LocalDateTime validTo;
    }

    @EmbeddedId
    private ServiceDynamicPriceId id;
}
