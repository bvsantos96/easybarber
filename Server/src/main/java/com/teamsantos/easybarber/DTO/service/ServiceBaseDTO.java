package com.teamsantos.easybarber.DTO.service;

import java.util.Objects;
import java.util.Set;

import com.teamsantos.easybarber.DTO.BaseDTO;
import com.teamsantos.easybarber.entities.images.ServiceImage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiceBaseDTO extends BaseDTO {
    private String name;
    private String description;
    private int duration;
    private ServiceTypeDTO serviceType;
    private Set<ServiceImage> images;
    private String image;
    private Double price;

    public ServiceBaseDTO(Long id, String name, String description, int duration, String image, Long serviceTypeId,
            String serviceTypeName, String serviceTypeDescription, String serviceTypeImage, Double price) {
        super(id);
        this.name = parseServiceString(name, serviceTypeName);
        this.description = parseServiceString(description, serviceTypeDescription);
        this.duration = duration;
        this.image = parseServiceString(image, serviceTypeImage);
        this.serviceType = new ServiceTypeDTO(serviceTypeId, serviceTypeName, serviceTypeDescription, serviceTypeImage);
        this.price = price;
    }

    public ServiceBaseDTO(Long id, String name, String description, int duration, String image, Long serviceTypeId,
            String serviceTypeName, String serviceTypeDescription, String serviceTypeImage) {
        super(id);
        this.name = parseServiceString(name, serviceTypeName);
        this.description = parseServiceString(description, serviceTypeDescription);
        this.duration = duration;
        this.image = parseServiceString(image, serviceTypeImage);
        this.serviceType = new ServiceTypeDTO(serviceTypeId, serviceTypeName, serviceTypeDescription, serviceTypeImage);
    }

    private String parseServiceString(String name, String fallback) {
        return name == null || name.length() == 0 ? fallback : name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ServiceBaseDTO service)) {
            return false;
        }
        return getId().equals(service.getId())
                && name.equals(service.getName())
                && description.equals(service.getDescription())
                && Objects.equals(duration, service.getDuration())
                && serviceType.equals(service.getServiceType())
                && Objects.equals(image, service.getImage());
    }

    public boolean equalsWithoutPrice(ServiceDTO service) {
        return getId().equals(service.getId())
                && name.equals(service.getName())
                && description.equals(service.getDescription())
                && Objects.equals(duration, service.getDuration())
                && serviceType.getId().equals(service.getServiceTypeId());
    }
}
