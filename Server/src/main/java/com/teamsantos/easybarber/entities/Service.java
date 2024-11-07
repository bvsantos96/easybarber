package com.teamsantos.easybarber.entities;

import java.util.Set;

import com.teamsantos.easybarber.DTO.service.ServiceDTO;
import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.images.ServiceImage;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false, of = { "name", "description", "duration", "serviceType", "employee" })
@ToString
@Entity
@Table(indexes = { @Index(columnList = "service_type_id"), @Index(columnList = "employee_id") })
public class Service extends EntityWithImages<Service, ServiceImage> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column(nullable = false)
    private int duration;
    @ManyToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn(name = "service_type_id")
    private ServiceType serviceType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "employee_id", referencedColumnName = "id")
    private Employee employee;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service", cascade = CascadeType.ALL)
    private Set<EstablishmentService> establishments;

    public void update(ServiceDTO serviceDTO) {
        this.name = Utils.setFieldIfNotNullOrEmpty(name, serviceDTO.getName());
        this.description = Utils.setFieldIfNotNullOrEmpty(description, serviceDTO.getDescription());
        this.duration = Utils.setFieldIfNotNullOrEmpty(duration, serviceDTO.getDuration());
    }

    @Override
    public Service getEntity() {
        return this;
    }
}
