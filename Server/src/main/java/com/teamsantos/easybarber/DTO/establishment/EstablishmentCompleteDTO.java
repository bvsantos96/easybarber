package com.teamsantos.easybarber.DTO.establishment;

import java.util.List;

import org.locationtech.jts.geom.Point;

import com.teamsantos.easybarber.DTO.employee.EmployeeDTO;
import com.teamsantos.easybarber.DTO.service.ServiceDTO;
import com.teamsantos.easybarber.DTO.user.UserDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstablishmentCompleteDTO extends BaseEstablishmentDTO {
    private List<UserDTO> admin;
    private List<EmployeeDTO> staff;
    private List<ServiceDTO> services;

    public EstablishmentCompleteDTO() {
    }

    public EstablishmentCompleteDTO(EstablishmentDTO establishment) {
        super(establishment.getId(), establishment.getName(), establishment.getDescription(),
                establishment.getAddress());
    }

    public EstablishmentCompleteDTO(Long id, String name, String description, String address, Point location,
            List<Service> services, List<Employee> employees) {
        super(id, name, description, address, location);
        try {
            setServices(services.stream().map(ServiceDTO::new).toList());
            setStaff(employees.stream().map(EmployeeDTO::new).toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
