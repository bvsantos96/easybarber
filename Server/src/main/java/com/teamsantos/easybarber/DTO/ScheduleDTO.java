package com.teamsantos.easybarber.DTO;

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
    private DAY_OF_WEEK day;
    private String startHour;
    private String endHour;

    public ScheduleDTO() {
        super();
    }

	public EmployeeSchedule toEntity(Employee employee, Establishment establishment) {
        EmployeeSchedule schedule = new EmployeeSchedule();
        schedule.setEmployee(employee);
        schedule.setEstablishment(establishment);
        schedule.setDay(day);
        schedule.setStartHour(startHour);
        schedule.setEndHour(endHour);
        schedule.setActive(true);
        return schedule;
	}
}
