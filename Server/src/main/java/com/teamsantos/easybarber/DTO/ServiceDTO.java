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
    private Long employeeId;
    private Long serviceTypeId;
    private String name;
    private String description;
    private String imageUrl;
    private Double price;

    public ServiceDTO(Long id, Long employeeId, Long serviceTypeId, String name, String description, String imageUrl,
            double price) {
        super(id);
        this.employeeId = employeeId;
        this.serviceTypeId = serviceTypeId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    @Override
    public ServiceDTO addId(Long id) {
        setId(id);
        return this;
    }

    public ServiceDTO addDescription(String description) {
        setDescription(description);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ServiceDTO service)) {
            return false;
        }
        return getId().equals(service.getId()) && employeeId.equals(service.getEmployeeId())
                && serviceTypeId.equals(service.getServiceTypeId()) && name.equals(service.getName())
                && description.equals(service.getDescription()) && imageUrl.equals(service.getImageUrl())
                && price == service.getPrice();
    }

    public boolean equalsWithoutPrice(Object obj) {
        if (!(obj instanceof ServiceDTO service)) {
            return false;
        }
        return getId().equals(service.getId()) && employeeId.equals(service.getEmployeeId())
                && serviceTypeId.equals(service.getServiceTypeId()) && name.equals(service.getName())
                && description.equals(service.getDescription()) && imageUrl.equals(service.getImageUrl());
    }
}
