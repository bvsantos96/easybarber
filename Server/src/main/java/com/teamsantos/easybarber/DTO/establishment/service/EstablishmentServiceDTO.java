package com.teamsantos.easybarber.DTO.establishment.service;

import com.teamsantos.easybarber.DTO.NameIdImageDTO;
import com.teamsantos.easybarber.DTO.service.ServiceWithImagesDTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstablishmentServiceDTO extends ServiceWithImagesDTO {
    private NameIdImageDTO establishment;
    private Double price;
    private Boolean active;
    private Long serviceId;

    public EstablishmentServiceDTO(Long id, Long serviceId, String name, String description, Integer duration,
            String image,
            Long serviceTypeId, String serviceTypeName, String serviceTypeDescription, String serviceTypeImageURL,
            Long employeeId, String employeeName, String employeeImage,
            Long establishmentId, String establishmentName, String establishmentImage,
            Double price, Boolean active) {
        super(id, name, description, duration, image, serviceTypeId, serviceTypeName, serviceTypeDescription,
                serviceTypeImageURL, employeeId, employeeName, employeeImage);
        this.establishment = new NameIdImageDTO(establishmentId, establishmentName, establishmentImage);
        this.price = price;
        this.active = active;
        this.serviceId = serviceId;
    }
}
