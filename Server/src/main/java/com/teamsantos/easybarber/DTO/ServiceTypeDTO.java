package com.teamsantos.easybarber.DTO;

import com.teamsantos.easybarber.entities.ServiceType;

import lombok.Data;

@Data
public class ServiceTypeDTO {
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
