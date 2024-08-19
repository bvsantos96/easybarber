package com.teamsantos.easybarber.security.filters;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;

public class EstablishmentSecurityExpressionRoot extends SecurityExpressionRoot {
    private final EmployeeRepository employeeRepository;
    private final EstablishmentStaffRepository establishmentStaffRepository;

    public EstablishmentSecurityExpressionRoot(Authentication authentication, EmployeeRepository employeeRepository,
            EstablishmentStaffRepository establishmentStaffRepository) {
        super(authentication);
        this.employeeRepository = employeeRepository;
        this.establishmentStaffRepository = establishmentStaffRepository;
    }

    /**
     * Checks if the logged user has permission to access the establishment
     * At the moment, only the admin of the establishment has permission to access
     * it, but in the future we can add more permissions that can be checked here
     * and passed as parameters
     *
     * @param establishmentId the id of the establishment
     * @return returns if the logged user has permission to access the
     *         establishment
     */
    public boolean hasAdminPermission(Long establishmentId) {
        Long employeeId = employeeRepository.findByMobileInformation(getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new)
                .getId();
        return establishmentStaffRepository.isAdminOfEstablishment(employeeId, establishmentId);
    }

    public boolean hasEmployeePermission(Long establishmentId) {
        Long employeeId = employeeRepository.findByMobileInformation(getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new)
                .getId();
        return establishmentStaffRepository.isEmployeeOfEstablishment(employeeId, establishmentId);
    }
}
