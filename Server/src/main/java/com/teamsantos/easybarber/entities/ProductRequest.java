package com.teamsantos.easybarber.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class ProductRequest {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "appointment_id", referencedColumnName = "id")
    private Appointment appointment;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "product_id", referencedColumnName = "id")
    private Product product;
}
