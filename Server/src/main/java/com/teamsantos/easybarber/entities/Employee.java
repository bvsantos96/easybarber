package com.teamsantos.easybarber.entities;

import java.util.List;
import java.util.Set;

import com.teamsantos.easybarber.entities.base.EntityWithImages;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Employee extends EntityWithImages {
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
    @OneToMany(mappedBy = "employee", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Service> services;
    @OneToMany(mappedBy = "employee", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<EstablishmentStaff> establishments;

    @Override
    public String getEntityType() {
        return "employee";
    }
}
