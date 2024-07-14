package com.teamsantos.easybarber.services;

import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.DTO.ScheduleDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.repositories.EmployeeScheduleRepository;
import com.teamsantos.easybarber.repositories.EstablishmentRepository;
import com.teamsantos.easybarber.repositories.ScheduleExceptionsRepository;

@Service
public class SchedulesService {
    private final EmployeeScheduleRepository employeeScheduleRepository;
    private final ScheduleExceptionsRepository scheduleExceptionsRepository;
    private final EstablishmentRepository establishmentRepository;

    public SchedulesService(EmployeeScheduleRepository employeeScheduleRepository,
            ScheduleExceptionsRepository scheduleExceptionsRepository,
            EstablishmentRepository establishmentRepository) {
        this.employeeScheduleRepository = employeeScheduleRepository;
        this.scheduleExceptionsRepository = scheduleExceptionsRepository;
        this.establishmentRepository = establishmentRepository;
    }

    public EmployeeSchedule create(ScheduleDTO schedule, Employee employee) {
        Establishment establishment = establishmentRepository.findById(schedule.getEstablishmentId())
                .orElseThrow(() -> new IllegalArgumentException("Establishment not found"));
        if (employee.getEstablishments().stream().noneMatch(e -> e.getId().equals(schedule.getEstablishmentId()))) {
            throw new IllegalArgumentException("Employee does not belong to this establishment");
        }
        employeeScheduleRepository
                .findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqual(employee.getId(),
                        schedule.getDay(), schedule.getStartHour(), schedule.getEndHour())
                .ifPresent(s -> {
                    throw new IllegalArgumentException("Employee already has a schedule for this day/hours");
                });
        scheduleExceptionsRepository
                .findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqual(employee.getId(),
                        schedule.getDay(), schedule.getStartHour(), schedule.getEndHour())
                .ifPresent(s -> {
                    throw new IllegalArgumentException("Employee has an exception for this day/hours");
                });
        return this.employeeScheduleRepository.save(schedule.toEntity(employee, establishment));
    }
}
