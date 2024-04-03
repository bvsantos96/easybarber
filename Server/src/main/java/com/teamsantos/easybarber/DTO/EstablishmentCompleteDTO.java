package com.teamsantos.easybarber.DTO;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class EstablishmentCompleteDTO extends EstablishmentDTO {
    private List<UserDTO> owner;
    private List<EmployeeDTO> staff;
    private List<ServiceDTO> services;

}
