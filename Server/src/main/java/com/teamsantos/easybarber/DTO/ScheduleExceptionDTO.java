package com.teamsantos.easybarber.DTO;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.ScheduleException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleExceptionDTO extends ScheduleDTO {
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Boolean active;

    public ScheduleExceptionDTO() {
        super();
    }

    public Set<ScheduleException> toEntitiesExceptions(Employee employee, Establishment establishment) {
        Set<ScheduleException> schedules = new HashSet<ScheduleException>();
        for (; dateFrom.isBefore(dateTo) || dateFrom.isEqual(dateTo); dateFrom = dateFrom.plusDays(1)) {
            for (DAY_OF_WEEK day : getDays()) {
                ScheduleException schedule = new ScheduleException();
                schedule.setId(getId());
                if (employee != null) {
                    schedule.setEmployee(employee);
                }
                if (establishment != null) {
                    schedule.setEstablishment(establishment);
                }
                schedule.setDate(dateFrom);
                schedule.setStartHour(getStartHour());
                schedule.setEndHour(getEndHour());
                schedule.setActive(active);
                schedule.setDay(day);
                schedules.add(schedule);
            }
        }
        return schedules;
    }
}
