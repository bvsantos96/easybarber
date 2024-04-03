package com.teamsantos.easybarber.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String description;
    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    private double rating;
    @Column(nullable = false, columnDefinition = "SMALLINT DEFAULT 0")
    private short nRating;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean enabled;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_mobileInformation", referencedColumnName = "mobileInformation")
    private User user;

    @OneToOne(mappedBy = "employee")
    private EstablishmentService establishmentService;
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
}
