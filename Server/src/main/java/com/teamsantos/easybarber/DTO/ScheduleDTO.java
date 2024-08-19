package com.teamsantos.easybarber.DTO;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.Establishment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScheduleDTO extends BaseDTO {
    private Long employeeId;
    private Long establishmentId;
    private Set<DAY_OF_WEEK> days;
    private LocalTime startHour;
    private LocalTime endHour;

    public ScheduleDTO() {
        super();
    }

    public ScheduleDTO(Long id, Long employeeId, Long establishmentId, DAY_OF_WEEK day, LocalTime startHour,
            LocalTime endHour) {
        this(id, employeeId, establishmentId, Set.of(day), startHour, endHour);
    }

    public ScheduleDTO(Long id, Long employeeId, Long establishmentId, Set<DAY_OF_WEEK> day, LocalTime startHour,
            LocalTime endHour) {
        super(id);
        this.employeeId = employeeId;
        this.establishmentId = establishmentId;
        this.days = day;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public EmployeeSchedule toEntity(Employee employee, Establishment establishment) {
        EmployeeSchedule schedule = new EmployeeSchedule();
        schedule.setEmployee(employee);
        schedule.setEstablishment(establishment);
        if (days.size() > 1) {
            throw new IllegalArgumentException("Only one day of week is allowed");
        }
        schedule.setDay(days.iterator().next());
        schedule.setStartHour(startHour);
        schedule.setEndHour(endHour);
        schedule.setActive(true);
        return schedule;
    }

    public List<EmployeeSchedule> toEntities(Employee employee, Establishment establishment) {
        List<EmployeeSchedule> schedules = new ArrayList<EmployeeSchedule>();
        for (DAY_OF_WEEK day : this.days) {
            EmployeeSchedule schedule = new EmployeeSchedule();
            schedule.setEmployee(employee);
            schedule.setEstablishment(establishment);
            schedule.setDay(day);
            schedule.setStartHour(startHour);
            schedule.setEndHour(endHour);
            schedule.setActive(true);
            schedules.add(schedule);
        }
        return schedules;
    }
}
