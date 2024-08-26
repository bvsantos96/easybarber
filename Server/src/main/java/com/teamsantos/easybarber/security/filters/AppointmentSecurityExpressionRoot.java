package com.teamsantos.easybarber.security.filters;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

import com.teamsantos.easybarber.repositories.AppointmentRepository;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.UserRepository;

public class AppointmentSecurityExpressionRoot extends SecurityExpressionRoot {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    public AppointmentSecurityExpressionRoot(Authentication authentication,
            AppointmentRepository appointmentRepository,
            UserRepository userRepository,
            EmployeeRepository employeeRepository) {
        super(authentication);
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.employeeRepository = employeeRepository;
    }

    public boolean hasAppointmentChangePermission(Long appointmentId) {
        Long id = employeeRepository.getIdByMobileInformation(getAuthentication().getName());
        if (id != null && appointmentRepository.existsByIdAndEmployeeId(appointmentId, id)) {
            return true;
        }
        id = userRepository.getIdByMobileInformation(getAuthentication().getName());
        if (id != null && appointmentRepository.existsByIdAndUserId(appointmentId, id)) {
            return true;
        }
        return false;
    }
}
