package com.teamsantos.easybarber.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EstablishmentServiceDTO extends BaseDTO {
    private EstablishmentDTO establishment;
    private ServiceDTO service;
    private Double price;
    private Boolean active;
}
