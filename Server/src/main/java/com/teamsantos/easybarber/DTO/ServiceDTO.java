package com.teamsantos.easybarber.DTO;

import java.util.Objects;

import com.teamsantos.easybarber.entities.Service;

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
    private Double price;
    private int duration;

    public ServiceDTO(Service service) {
        super(service.getId());
        this.employeeId = service.getEmployee().getId();
        this.serviceTypeId = service.getServiceType().getId();
        this.name = service.getName();
        this.description = service.getDescription();
        this.duration = service.getDuration();
    }

    public ServiceDTO(Long id, Long employeeId, Long serviceTypeId, String name, String description,
            double price, int duration) {
        super(id);
        this.employeeId = employeeId;
        this.serviceTypeId = serviceTypeId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }

    public ServiceDTO(Long id, Long employeeId, Long serviceTypeId, String name, String description,
            double price) {
        super(id);
        this.employeeId = employeeId;
        this.serviceTypeId = serviceTypeId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public ServiceDTO(Long id, Long employeeId, Long serviceTypeId, String name, String description, String imageUrl,
            double price) {
        super(id);
        this.employeeId = employeeId;
        this.serviceTypeId = serviceTypeId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public ServiceDTO(Long id, Long employeeId, Long serviceTypeId, String name, String description, String imageUrl,
            double price, int duration) {
        super(id);
        this.employeeId = employeeId;
        this.serviceTypeId = serviceTypeId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
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
                && description.equals(service.getDescription())
                && Objects.equals(price, service.getPrice())
                && Objects.equals(duration, service.getDuration());
    }

    public boolean equalsWithoutPrice(Object obj) {
        if (!(obj instanceof ServiceDTO service)) {
            return false;
        }
        return getId().equals(service.getId())
                && employeeId.equals(service.getEmployeeId())
                && serviceTypeId.equals(service.getServiceTypeId())
                && name.equals(service.getName())
                && description.equals(service.getDescription())
                && Objects.equals(duration, service.getDuration());
    }
}
