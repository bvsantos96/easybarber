package com.teamsantos.easybarber.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.ScheduleException;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.EntityManager;
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

    public ScheduleExceptionDTO(Long id, Long employeeId, Long establishmentId, Set<DAY_OF_WEEK> days,
            LocalTime startHour,
            LocalTime endHour, LocalDate dateFrom, LocalDate dateTo, Boolean active) {
        super(id, employeeId, establishmentId, days, startHour, endHour);
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.active = active;
    }

    public ScheduleExceptionDTO(Long id, Long employeeId, Long establishmentId, LocalDate date, LocalTime startHour,
            LocalTime endHour, Boolean active, DAY_OF_WEEK day) {
        super(id, employeeId, establishmentId, Set.of(day), startHour, endHour);
        this.dateFrom = date;
        this.dateTo = date;
        this.active = active;
    }

    public Set<ScheduleException> toEntitiesExceptions(EntityManager entityManager, Long employeeId,
            Long establishmentId) {
        Set<ScheduleException> schedules = new HashSet<ScheduleException>();
        for (; dateFrom.isBefore(dateTo) || dateFrom.isEqual(dateTo); dateFrom = dateFrom.plusDays(1)) {
            DAY_OF_WEEK day = Utils.getDayOfWeek(dateFrom);
            if (getDays().size() > 0) {
                if (!getDays().contains(day)) {
                    continue;
                }
            }
            ScheduleException schedule = new ScheduleException();
            if (employeeId != null) {
                schedule.setEmployee(entityManager.getReference(Employee.class, employeeId));
            }
            if (establishmentId != null) {
                schedule.setEstablishment(entityManager.getReference(Establishment.class, establishmentId));
            }
            schedule.setDate(dateFrom);
            schedule.setStartHour(getStartHour());
            schedule.setEndHour(getEndHour());
            schedule.setActive(active);
            schedule.setDay(day);
            schedules.add(schedule);
        }
        return schedules;
    }

    public List<ScheduleDTO> toDTOs(LocalDate from, LocalDate to) {
        List<ScheduleDTO> schedules = new ArrayList<ScheduleDTO>();
        for (; dateFrom.isBefore(dateTo) || dateFrom.isEqual(dateTo); dateFrom = dateFrom.plusDays(1)) {
            if (dateFrom.isBefore(from) || dateFrom.isAfter(to)) {
                continue;
            }
            DAY_OF_WEEK day = Utils.getDayOfWeek(dateFrom);
            if (getDays().size() > 0) {
                if (!getDays().contains(day)) {
                    continue;
                }
            }
            ScheduleDTO schedule = new ScheduleDTO();
            schedule.setId(getId());
            schedule.setEmployeeId(getEmployeeId());
            schedule.setEstablishmentId(getEstablishmentId());
            schedule.setDays(Set.of(day));
            schedule.setStartHour(getStartHour());
            schedule.setEndHour(getEndHour());
            schedules.add(schedule);
        }
        return schedules;
    }
}
