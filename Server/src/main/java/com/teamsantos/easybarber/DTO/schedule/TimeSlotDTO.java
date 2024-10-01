package com.teamsantos.easybarber.DTO.schedule;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TimeSlotDTO {
    private String start;
    private String end;

    public TimeSlotDTO(LocalTime start, LocalTime end) {
        DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
        this.start = start.format(formatter24Hour);
        this.end = end.format(formatter24Hour);
    }
}
