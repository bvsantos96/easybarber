package com.teamsantos.easybarber.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

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

    public EstablishmentStaff(User user, Establishment establishment, boolean admin, User invitor) {
        this.user = user;
        this.establishment = establishment;
        this.admin = admin;
        this.invitor = invitor;
    }
}
