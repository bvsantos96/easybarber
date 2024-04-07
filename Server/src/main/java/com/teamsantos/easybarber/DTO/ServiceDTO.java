package com.teamsantos.easybarber.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO extends BaseDTO {
    private Long id;
    private Long employeeId;
    private Long serviceTypeId;
    private String name;
    private String description;
    private String imageUrl;
    private double price;

    public ServiceDTO addId(Long id) {
        this.id = id;
        return this;
    }

    public ServiceDTO addDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ServiceDTO service)) {
            return false;
        }
        return id.equals(service.getId()) && employeeId.equals(service.getEmployeeId())
                && serviceTypeId.equals(service.getServiceTypeId()) && name.equals(service.getName())
                && description.equals(service.getDescription()) && imageUrl.equals(service.getImageUrl())
                && price == service.getPrice();
    }

    public boolean equalsWithoutPrice(Object obj) {
        if (!(obj instanceof ServiceDTO service)) {
            return false;
        }
        return id.equals(service.getId()) && employeeId.equals(service.getEmployeeId())
                && serviceTypeId.equals(service.getServiceTypeId()) && name.equals(service.getName())
                && description.equals(service.getDescription()) && imageUrl.equals(service.getImageUrl());
    }
}
