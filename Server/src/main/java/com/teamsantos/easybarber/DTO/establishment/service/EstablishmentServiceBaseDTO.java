package com.teamsantos.easybarber.DTO.establishment.service;

import com.teamsantos.easybarber.DTO.service.ServiceWithImagesDTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstablishmentServiceBaseDTO extends ServiceWithImagesDTO {
    private long establishmentId;
    private Double price;
    private Boolean active;
    private Long serviceId;

    public EstablishmentServiceBaseDTO(Long id,
            Long serviceId, String name, String description, Integer duration, String image,
            Long serviceTypeId, String serviceTypeName, String serviceTypeDescription, String serviceTypeImageURL,
            Long employeeId, String employeeName, String employeeImage,
            Long establishmentId,
            Double price, Boolean active) {
        super(id, name, description, duration, image, serviceTypeId, serviceTypeName, serviceTypeDescription,
                serviceTypeImageURL, employeeId, employeeName, employeeImage);
        this.establishmentId = establishmentId;
        this.price = price;
        this.active = active;
        this.serviceId = serviceId;
    }
}
