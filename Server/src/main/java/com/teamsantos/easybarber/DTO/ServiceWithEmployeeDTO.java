package com.teamsantos.easybarber.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceWithEmployeeDTO extends ServiceBaseDTO {
    private EmployeeBaseDTO employee;

    public ServiceWithEmployeeDTO(Long id, String name, String description, int duration, long serviceTypeId,
            EmployeeBaseDTO employee) {
        super(id, name, description, duration, serviceTypeId);
        this.employee = employee;
    }

    public ServiceWithEmployeeDTO(Long id, String name, String description, int duration, long serviceTypeId,
            String image,
            EmployeeBaseDTO employee) {
        super(id, name, description, duration, serviceTypeId, image);
        this.employee = employee;
    }

    public ServiceWithEmployeeDTO(Long id, String name, String description, int duration, long serviceTypeId,
            String image,
            Long employeeId, String employeeName) {
        super(id, name, description, duration, serviceTypeId, image);
        this.employee = new EmployeeBaseDTO(employeeId, employeeName);
    }

    public ServiceWithEmployeeDTO(Long id, String name, String description, int duration, long serviceTypeId,
            String image,
            Long employeeId, String employeeName, String employeeImage) {
        super(id, name, description, duration, serviceTypeId, image);
        this.employee = new EmployeeBaseDTO(employeeId, employeeName, employeeImage);
    }

    public ServiceWithEmployeeDTO(Long id, String name, String description, int duration, long serviceTypeId,
            Long employeeId, String employeeName, String employeeImage) {
        super(id, name, description, duration, serviceTypeId);
        this.employee = new EmployeeBaseDTO(employeeId, employeeName, employeeImage);
    }

    public ServiceWithEmployeeDTO(Long id, String name, String description, int duration, long serviceTypeId,
            Long employeeId, String employeeName) {
        super(id, name, description, duration, serviceTypeId);
        this.employee = new EmployeeBaseDTO(employeeId, employeeName);
    }
}
