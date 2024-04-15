package com.teamsantos.easybarber.DTO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstablishmentCompleteDTO extends EstablishmentDTO {
    private List<UserDTO> admin;
    private List<EmployeeDTO> staff;
    private List<ServiceDTO> services;
}
