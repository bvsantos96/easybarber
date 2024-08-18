package com.teamsantos.easybarber.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class AppointmentDTO {
    private Long userID;
    private Long employeeID;
    private String description;
    // TODO: apagar dps de explicar
    // To let barbers schedule appointments for non-registered users
    // p.e. pessoas mais velhas que nao usam a internet
    // ou pessoas que passam la na loja so para agendar e nao querem usar a app
    // sem isto os barbers tinham de criar um user para cada pessoa 
    // ou criar uma exception para cada um desses casos e saber o que cada exception significa (que pessoa representa)
    private String nonRegisteredUser;
    private Date appointmentDateTime;
}
