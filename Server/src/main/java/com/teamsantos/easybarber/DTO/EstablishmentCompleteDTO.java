package com.teamsantos.easybarber.DTO;

import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Service;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.util.List;

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
