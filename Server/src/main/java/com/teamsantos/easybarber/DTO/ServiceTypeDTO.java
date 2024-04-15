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

    public ServiceTypeDTO(Long id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public ServiceTypeDTO(Long id, String name, String description, String imageUrl) {
        super(id);
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    @Override
    public ServiceTypeDTO addId(Long id) {
        super.addId(id);
        return this;
    }

    public ServiceTypeDTO addDescription(String description) {
        this.description = description;
        return this;
    }
}
