package com.teamsantos.easybarber.DTO.service;

import java.util.Set;

import com.teamsantos.easybarber.entities.images.ServiceImage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiceFullDTO extends ServiceWithImagesDTO {
    private Set<ServiceImage> images;

    public ServiceFullDTO(Long id, String name, String description, Integer duration, double price,
            Set<ServiceImage> images,
            Long serviceTypeId, String serviceTypeName, String serviceTypeDescription, String serviceTypeImageURL,
            Long employeeId, String employeeName) {
        super(id, name, description, duration, null, serviceTypeId, serviceTypeName, serviceTypeDescription,
                serviceTypeImageURL, employeeId, employeeName, null, price);
        this.images = images;
    }
}
