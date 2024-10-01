package com.teamsantos.easybarber.DTO.schedule;

import java.time.LocalTime;

import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeScheduleDTO {
    private Long id;
    private Long employeeId;
    private Long establishmentId;
    private DAY_OF_WEEK day;
    private LocalTime startHour;
    private LocalTime endHour;

    public String getKey() {
        return String.format("%s-%s", startHour, endHour);
    }

}
