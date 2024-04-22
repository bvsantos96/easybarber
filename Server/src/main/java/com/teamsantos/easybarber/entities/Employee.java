package com.teamsantos.easybarber.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
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
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Service> services;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private Set<EstablishmentStaff> establishments;
}
