package com.teamsantos.easybarber.entities;

import java.util.Objects;
import java.util.Set;

import org.hibernate.proxy.HibernateProxy;

import com.teamsantos.easybarber.DTO.ServiceDTO;
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
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(indexes = { @Index(columnList = "serviceTypeId") })
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "service_type_id")
    private ServiceType serviceType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "employee_id", referencedColumnName = "id")
    private Employee employee;
    @OneToMany(mappedBy = "service", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<EstablishmentService> establishments;

    @Override
    public final boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass)
            return false;
        Service service = (Service) o;
        return getId() != null && Objects.equals(getId(), service.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

    public void update(ServiceDTO serviceDTO) {
        this.name = Utils.setFieldIfNotNullOrEmpty(name, serviceDTO.getName());
        this.description = Utils.setFieldIfNotNullOrEmpty(description, serviceDTO.getDescription());
        this.imageUrl = Utils.setFieldIfNotNullOrEmpty(imageUrl, serviceDTO.getImageUrl());
    }
}
