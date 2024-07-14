package com.teamsantos.easybarber.DTO;

import java.util.Date;

import lombok.Data;

@Data
public class AppointmentDTO {
    private Long userID;
    private Long employeeID;
    private String description;
    private Date appointmentDateTime;
}
