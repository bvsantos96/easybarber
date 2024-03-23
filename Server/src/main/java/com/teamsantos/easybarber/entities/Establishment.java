package com.teamsantos.easybarber.entities;

import lombok.Data;
import java.util.Set;

import org.locationtech.jts.geom.Point;

import com.teamsantos.easybarber.DTO.EstablishmentDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Data
@Entity
public class Establishment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String description;
    @OneToMany(mappedBy = "establishment")
    private Set<EstablishmentStaff> staff;
    @Column
    private String address;
    @Column
    private Point location;

    public EstablishmentDTO convertToDto(Establishment establishment) {
        EstablishmentDTO dto = new EstablishmentDTO();
        dto.setId(establishment.getId());
        dto.setName(establishment.getName());
        dto.setDescription(establishment.getDescription());
        dto.setAddress(establishment.getAddress());
        return dto;
    }
}
