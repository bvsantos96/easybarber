package com.teamsantos.easybarber.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceWithImagesDTO extends ServiceBaseDTO {
    private EmployeeBaseDTO employee;

    public ServiceWithImagesDTO(Long id, String name, String description, Integer duration, String image,
            Long serviceTypeId, String serviceTypeName, String serviceTypeDescription, String serviceTypeImageURL,
            Long employeeId, String employeeName, String employeeImage) {
        super(id, name, description, duration, image, serviceTypeId, serviceTypeName, serviceTypeDescription,
                serviceTypeImageURL);
        this.employee = new EmployeeBaseDTO(employeeId, employeeName, employeeImage);
    }

    public boolean equalsWithoutPrice(ServiceDTO service) {
        return service.getEmployeeId() == employee.getId() && super.equalsWithoutPrice(service);
    }
}
