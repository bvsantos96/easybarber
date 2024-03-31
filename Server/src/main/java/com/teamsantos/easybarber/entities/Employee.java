package com.teamsantos.easybarber.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "employee")
    private User user;
    @Column
    private String description;
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    private double rating;
    @Column(nullable = false, columnDefinition = "SMALLINT DEFAULT 0")
    private short nRating;
    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Appointment> appointments;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<EstablishmentStaff> establishments;
    @OneToMany(mappedBy = "invitor", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<EstablishmentStaff> invitations;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Service> services;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean enabled;
}
