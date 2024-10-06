package com.teamsantos.easybarber.DTO.filters;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AvailabilityFilter {
    private List<Long> employeeIds;
    private long establishmentId;
    private long serviceId;
    private LocalDate date;
    private LocalTime startHour;
    private LocalTime endHour;
}
