package com.teamsantos.easybarber.DTO.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class AppointmentReminderDTO {
    private Long appointmentID;
    private String userName;
    private String mobileInformation;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String establishmentName;
    private String employeeName;

    public AppointmentReminderDTO(Long appointmentID, String userName, String mobileInformation, LocalDate appointmentDate, LocalTime appointmentTime, String establishmentName, String employeeName){
        this.appointmentID = appointmentID;
        this.userName = userName;
        this.mobileInformation = mobileInformation;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.establishmentName = establishmentName;
        this.employeeName = employeeName;
    }
}
