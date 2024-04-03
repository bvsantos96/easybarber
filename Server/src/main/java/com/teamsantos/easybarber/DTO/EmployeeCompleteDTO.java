package com.teamsantos.easybarber.DTO;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class EmployeeCompleteDTO extends EmployeeDTO {
    private List<ServiceDTO> services;
    private List<EstablishmentDTO> establishments;
}
