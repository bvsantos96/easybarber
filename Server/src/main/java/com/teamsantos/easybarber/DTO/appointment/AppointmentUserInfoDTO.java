package com.teamsantos.easybarber.DTO.appointment;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUserInfoDTO {
    private long appointmentID;
    private long userId;
    private String userName;
    private String mobileInformation;
    private String establishmentName;
    private LocalDateTime dateTime;
}
