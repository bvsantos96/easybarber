package com.teamsantos.easybarber.security.filters;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

import com.teamsantos.easybarber.repositories.AppointmentRepository;
import com.teamsantos.easybarber.security.utils.UserContext;

public class AppointmentSecurityExpressionRoot extends SecurityExpressionRoot {
    private final AppointmentRepository appointmentRepository;

    public AppointmentSecurityExpressionRoot(Authentication authentication,
            AppointmentRepository appointmentRepository) {
        super(authentication);
        this.appointmentRepository = appointmentRepository;
    }

    public boolean hasAppointmentChangePermission(long appointmentId) {
        Long id = UserContext.getCurrentUser().getEmployeeId();
        if (id != null && appointmentRepository.existsByIdAndEmployeeId(appointmentId, id)) {
            return true;
        }
        if (appointmentRepository.existsByIdAndUserId(appointmentId, UserContext.getCurrentUser().getId())) {
            return true;
        }
        return false;
    }
}
