package com.teamsantos.easybarber.security.filters;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

import com.teamsantos.easybarber.repositories.EmployeeScheduleRepository;

public class ScheduleSecurityExpressionRoot extends SecurityExpressionRoot {
    private final EmployeeScheduleRepository scheduleRepository;

    public ScheduleSecurityExpressionRoot(Authentication authentication,
            EmployeeScheduleRepository scheduleRepository) {
        super(authentication);
        this.scheduleRepository = scheduleRepository;
    }

    public boolean hasScheduleOwnerPermission(Long targetDomainObject) {
        if (scheduleRepository.checkIfEmployeeIsScheduleOwner(targetDomainObject, this.getAuthentication().getName())) {
            return true;
        }
        if (scheduleRepository.checkIfEmployeeIsEstablishmentOwner(targetDomainObject,
                this.getAuthentication().getName())) {
            return true;
        }
        return false;
    }
}
