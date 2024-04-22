package com.teamsantos.easybarber.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeCompleteDTO extends EmployeeDTO {
    private List<ServiceDTO> services;
    private List<EstablishmentDTO> establishments;

    public EmployeeCompleteDTO(String description) {
        super(description);
    }

    public EmployeeCompleteDTO(Long id, String mobileCode, String mobileNumber, String name, String description) {
        super(id, mobileCode, mobileNumber, name, description);
    }
}
