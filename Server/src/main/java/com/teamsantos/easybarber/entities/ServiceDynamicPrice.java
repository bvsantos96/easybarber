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

    @EmbeddedId
    private ServiceDynamicPriceId id;

    @AssertTrue(message = "Either establishmentServiceEmployee or establishmentService must be set")
    private boolean isValidReference() {
        return establishmentServiceEmployee != null || establishmentService != null;
    }

    @Embeddable
    @Data
    public static class ServiceDynamicPriceId implements Serializable {
        @Column(name = "establishment_service_employee_id", insertable = false, updatable = false)
        private Long establishmentServiceEmployeeId;

        @Column(name = "establishment_service_id", insertable = false, updatable = false)
        private Long establishmentServiceId;

        @Column(name = "valid_from")
        private LocalDateTime validFrom;

        @Column(name = "valid_to")
        private LocalDateTime validTo;
    }
}
