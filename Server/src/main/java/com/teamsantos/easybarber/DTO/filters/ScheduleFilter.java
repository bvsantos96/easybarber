package com.teamsantos.easybarber.DTO.filters;

import java.util.Date;

import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleFilter {
    private long employeeId;
    private Long establishmentId;
    private DAY_OF_WEEK[] dayOfWeek;
    private Date from;
    private Date to;
    private String startHour;
    private String endHour;
    private Boolean active;
}
