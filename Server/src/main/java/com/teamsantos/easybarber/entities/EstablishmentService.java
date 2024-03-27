package com.teamsantos.easybarber.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class EstablishmentService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "establishment_id")
    private Establishment establishment;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;
    @Column
    private double price;
    @Column
    private boolean active;
}
