package com.teamsantos.easybarber.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long userTypeId;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String countryMobile;
    @Column
    private String mobile;
    @Column
    private String name;
    @Column
    private String mobileInformation;
    @Column
    private LocalDateTime tokenExpiration;
    @ManyToMany
    @JoinTable(name = "user_establishment", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "establishment_id"))
    private Set<Establishment> owned_establishments;
}
