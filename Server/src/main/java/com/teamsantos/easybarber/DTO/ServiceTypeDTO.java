package com.teamsantos.easybarber.DTO;

import com.teamsantos.easybarber.entities.ServiceType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTypeDTO extends BaseDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;

    public ServiceType getServiceType() {
        ServiceType service = new ServiceType();
        service.setId(getId());
        service.setName(getName());
        service.setDescription(getDescription());
        service.setImageUrl(getImageUrl());
        return service;
    }
}
