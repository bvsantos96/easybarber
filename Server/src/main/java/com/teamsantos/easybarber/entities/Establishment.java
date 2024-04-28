package com.teamsantos.easybarber.entities;

import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.images.EstablishmentImage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;
import org.locationtech.jts.geom.Point;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
public class Establishment extends EntityWithImages<Establishment, EstablishmentImage> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private String address;
    @Column
    private Point location;
    @Column
    private Long nVotes;
    @Column
    private Long sumVotes;
    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EstablishmentStaff> staff;
    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EstablishmentService> services;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        if (nVotes == null) {
            nVotes = 0L;
        }
        if (sumVotes == null) {
            sumVotes = 0L;
        }
        if (getServices() == null)
            setServices(new HashSet<>());
        if (getStaff() == null)
            setStaff(new HashSet<>());
    }

    public EstablishmentDTO convertToDto() {
        return convertToDto(this);
    }

    public static EstablishmentDTO convertToDto(Establishment establishment) {
        EstablishmentDTO dto = new EstablishmentDTO();
        dto.setId(establishment.getId());
        dto.setName(establishment.getName());
        dto.setDescription(establishment.getDescription());
        dto.setAddress(establishment.getAddress());
        dto.setLatitude(establishment.getLocation().getY());
        dto.setLongitude(establishment.getLocation().getX());
        return dto;
    }

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
        Establishment that = (Establishment) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

    @Override
    public Establishment getEntity() {
        return this;
    }
}
