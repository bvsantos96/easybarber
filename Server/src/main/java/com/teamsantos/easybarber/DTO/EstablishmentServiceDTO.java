package com.teamsantos.easybarber.DTO;

import lombok.Data;

@Data
public class EstablishmentServiceDTO {
    private Long id;
    private Long establishmentId;
    private Long employeeId;
    private double price;
    private boolean active;
}
