package com.teamsantos.easybarber.DTO.schedule;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDayInfoDTO {
    private boolean isDisabled;
    private boolean hasSchedules;
    private long nAppointments;
    private LocalDate date;

    public CalendarDayInfoDTO(LocalDate date, long nAppointments) {
        this.date = date;
        this.nAppointments = nAppointments;
    }
}
