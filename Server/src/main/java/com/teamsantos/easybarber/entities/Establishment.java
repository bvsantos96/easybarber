package com.teamsantos.easybarber.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Establishment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(mappedBy = "owned_establishments")
    private Set<User> owners;
    @Column
    private String name;
    @Column
    private String description;
}
