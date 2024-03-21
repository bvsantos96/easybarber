package com.teamsantos.easybarber.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class EstablishmentStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "establishment_id")
    private Establishment establishment;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean admin;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean approved;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean deleted;
    @OneToOne
    @JoinColumn(name = "invitor_id")
    private User invitor;

    public EstablishmentStaff(User user, Establishment establishment, boolean admin, boolean approved, User invitor) {
        this.user = user;
        this.establishment = establishment;
        this.admin = admin;
        this.approved = approved;
        this.invitor = invitor;
    }

    public EstablishmentStaff() {

    }
}
