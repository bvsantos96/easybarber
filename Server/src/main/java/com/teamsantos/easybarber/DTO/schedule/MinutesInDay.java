package com.teamsantos.easybarber.DTO.schedule;

import java.time.LocalDate;

import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MinutesInDay {
    private LocalDate date;
    private DAY_OF_WEEK dayOfWeek;
    private Long minutesInDay;

    public MinutesInDay(LocalDate date, Long minutesInDay) {
        this.date = date;
        this.minutesInDay = minutesInDay;
    }

    public MinutesInDay(DAY_OF_WEEK dayOfWeek, Long minutesInDay) {
        this.dayOfWeek = dayOfWeek;
        this.minutesInDay = minutesInDay;
    }
}
