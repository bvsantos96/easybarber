package com.teamsantos.easybarber.entities;

import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.utils.Utils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
@Table(indexes = { @Index(columnList = "service_type_id") })
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "service_type_id")
    private ServiceType serviceType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "employee_id", referencedColumnName = "id")
    private Employee employee;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "service", cascade = CascadeType.ALL)
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
    }
}
