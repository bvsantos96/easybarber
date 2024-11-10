package com.teamsantos.easybarber.DTO.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateServiceDTO {
    private Long serviceTypeId;
    private String name;
    private String description;
    private Double price;
    private int duration;

    public static CreateServiceDTO createDummy(ServiceDTO serviceDTO) {
        return new CreateServiceDTO(serviceDTO.getServiceTypeId(), serviceDTO.getName(), serviceDTO.getDescription(),
                serviceDTO.getPrice(), serviceDTO.getDuration());
    }
}
