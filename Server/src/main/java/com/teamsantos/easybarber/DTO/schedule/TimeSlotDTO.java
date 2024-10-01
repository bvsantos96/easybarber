package com.teamsantos.easybarber.DTO.schedule;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TimeSlotDTO {
    private String start;
    private String end;
    private List<Long> employeeIds;

    public TimeSlotDTO(LocalTime start, LocalTime end) {
        DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
        this.start = start.format(formatter24Hour);
        this.end = end.format(formatter24Hour);
    }

    public TimeSlotDTO(LocalTime start, LocalTime end, Long employeeId) {
        DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");
        this.start = start.format(formatter24Hour);
        this.end = end.format(formatter24Hour);
        setEmployeeId(employeeId);
    }

    public void setEmployeeId(Long employeeId) {
        if (employeeIds == null) {
            employeeIds = new ArrayList<>();
        }
        employeeIds.add(employeeId);
    }
}
