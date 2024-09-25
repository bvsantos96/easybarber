package com.teamsantos.easybarber.entities;

import java.util.List;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.images.EmployeeImage;

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
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Employee extends EntityWithImages<Employee, EmployeeImage> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String description;
    @Column(nullable = false)
    private Long sumVotes = 0L;
    @Column(nullable = false)
    private Long nVotes = 0L;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean enabled;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", referencedColumnName = "id")
    private User user;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @BatchSize(size = 10)
    private List<Service> services;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @BatchSize(size = 10)
    private Set<EstablishmentStaff> establishments;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @BatchSize(size = 10)
    private Set<ScheduleException> exceptions;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @BatchSize(size = 10)
    private Set<EmployeeSchedule> schedules;
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @BatchSize(size = 10)
    private Set<Appointment> appointment;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        if (nVotes == null) {
            nVotes = 0L;
        }
        if (sumVotes == null) {
            sumVotes = 0L;
        }
    }

    @Override
    public Employee getEntity() {
        return this;
    }
}
