package com.teamsantos.easybarber.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class EstablishmentServiceDTO extends ServiceDTO {
    private EstablishmentDTO establishment;
    private EmployeeDTO employee;
    private Double price;
    private Boolean active;
}
