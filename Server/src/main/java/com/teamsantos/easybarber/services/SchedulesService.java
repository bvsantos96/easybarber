package com.teamsantos.easybarber.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.ScheduleDTO;
import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.repositories.EmployeeScheduleRepository;
import com.teamsantos.easybarber.repositories.EstablishmentRepository;

@Service
public class SchedulesService {
    private final EmployeeScheduleRepository employeeScheduleRepository;
    private final EstablishmentRepository establishmentRepository;

    public SchedulesService(EmployeeScheduleRepository employeeScheduleRepository,
            EstablishmentRepository establishmentRepository) {
        this.employeeScheduleRepository = employeeScheduleRepository;
        this.establishmentRepository = establishmentRepository;
    }

    public String[] create(ScheduleDTO schedule, Employee employee, Boolean forceSave) {
        String response = "";
        Establishment establishment = establishmentRepository.findById(schedule.getEstablishmentId())
                .orElseThrow(() -> new IllegalArgumentException("Establishment not found"));
        Optional<EmployeeSchedule> oSchedule = employeeScheduleRepository
                .findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndActive(employee.getId(),
                        schedule.getDay(), schedule.getStartHour(), schedule.getEndHour(), true);
        if (oSchedule.isPresent()) {
            if (!forceSave) {
                throw new IllegalArgumentException("Employee already has a schedule for this day/hours");
            } else {
                response = "Employee already has a schedule for this day/hours;";
                oSchedule.get().setActive(false);
                employeeScheduleRepository.save(oSchedule.get());
            }
        }
        // Optional<ScheduleExceptions> oExceptions = scheduleExceptionsRepository
        // .findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndDateAfter(employee.getId(),
        // schedule.getDay(), schedule.getStartHour(), schedule.getEndHour(), new
        // Date());
        // if (oExceptions.isPresent()) {
        // if (!forceSave) {
        // throw new IllegalArgumentException("Employee has an exception for this
        // day/hours");
        // } else {
        // response += "Employee has an exception for this day/hours";
        // oExceptions.get().setActive(false);
        // scheduleExceptionsRepository.save(oExceptions.get());
        // }
        // }
        this.employeeScheduleRepository.save(schedule.toEntity(employee, establishment));
        return response.split(";");
    }

    public BasePageDTO<ScheduleDTO> getSchedules(ScheduleFilter filter, Pageable pageable) {
        Page<EmployeeSchedule> schedules;
        schedules = employeeScheduleRepository.findAll(filter.getSpecification(), pageable);
        return new BasePageDTO<>(schedules.map(EmployeeSchedule::toDTO));
    }
}
