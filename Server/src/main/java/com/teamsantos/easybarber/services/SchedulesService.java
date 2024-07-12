package com.teamsantos.easybarber.services;

import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.repositories.EmployeeScheduleRepository;
import com.teamsantos.easybarber.repositories.ScheduleExceptionsRepository;

@Service
public class SchedulesService {
    private final EmployeeScheduleRepository employeeScheduleRepository;
    private final ScheduleExceptionsRepository scheduleExceptionsRepository;

    public SchedulesService(EmployeeScheduleRepository employeeScheduleRepository, ScheduleExceptionsRepository scheduleExceptionsRepository) {
        this.employeeScheduleRepository = employeeScheduleRepository;
        this.scheduleExceptionsRepository = scheduleExceptionsRepository;
    }
}
