package com.teamsantos.easybarber.security.filters;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.security.utils.UserContext;

public class EstablishmentSecurityExpressionRoot extends SecurityExpressionRoot {
    private final EstablishmentStaffRepository establishmentStaffRepository;

    public EstablishmentSecurityExpressionRoot(Authentication authentication,
            EstablishmentStaffRepository establishmentStaffRepository) {
        super(authentication);
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
        return establishmentStaffRepository.isAdminOfEstablishment(UserContext.getEmployeeId(), establishmentId);
    }

    public boolean hasEmployeePermission(Long establishmentId) {
        return establishmentStaffRepository.isEmployeeOfEstablishment(UserContext.getEmployeeId(), establishmentId);
    }
}
