package com.teamsantos.easybarber.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;

@Entity
@Data
@Table(name = "service_dynamic_price", uniqueConstraints = @UniqueConstraint(columnNames = {
        "establishment_service_employee_id", "establishment_service_id", "valid_from", "valid_to" }))
public class ServiceDynamicPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "establishment_service_employee_id", referencedColumnName = "id", nullable = true)
    private EstablishmentServiceEmployee establishmentServiceEmployee;

    @ManyToOne
    @JoinColumn(name = "establishment_service_id", referencedColumnName = "id", nullable = false)
    private EstablishmentService establishmentService;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private LocalDateTime validTo;

    @Column(nullable = false)
    private double price;

    @AssertTrue(message = "Either establishmentServiceEmployee or establishmentService must be set")
    private boolean isValidReference() {
        return establishmentServiceEmployee != null || establishmentService != null;
    }
}
