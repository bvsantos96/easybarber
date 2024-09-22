package com.teamsantos.easybarber.testDTOs;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.teamsantos.easybarber.DTO.ScheduleExceptionDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

import lombok.Getter;

@Getter
public class ScheduleExceptionTestDTO extends ScheduleExceptionDTO {
    private List<Long> ids;

    public ScheduleExceptionTestDTO() {
        super();
    }

    public ScheduleExceptionTestDTO(Long employeeId, Long establishmentId, Set<DAY_OF_WEEK> days,
            LocalTime startHour,
            LocalTime endHour, LocalDate dateFrom, LocalDate dateTo, Boolean active) {
        super(employeeId, establishmentId, days, startHour, endHour, dateFrom, dateTo, active);
    }

    public ScheduleExceptionTestDTO(Long id, Long employeeId, Long establishmentId, Set<DAY_OF_WEEK> days,
            LocalTime startHour,
            LocalTime endHour, LocalDate dateFrom, LocalDate dateTo, Boolean active) {
        super(id, employeeId, establishmentId, days, startHour, endHour, dateFrom, dateTo, active);
    }

    public void setIds(List<Long> ids) {
        if (ids.size() == 0) {
            return;
        }
        setId(ids.get(0));
        this.ids = ids;
    }

    public void _setId(Long id) {
        super.setId(id);
        if (ids == null) {
            ids = new ArrayList<>();
        }
        ids.add(id);
    }
}
