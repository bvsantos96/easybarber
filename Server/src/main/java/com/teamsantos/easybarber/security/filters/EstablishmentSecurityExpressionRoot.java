package com.teamsantos.easybarber.security.filters;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.services.EstablishmentService;

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
    public boolean hasAdminPermission(long establishmentId) {
        return _hasAdminPermission(establishmentStaffRepository, establishmentId);
    }

    public static boolean _hasAdminPermission(EstablishmentStaffRepository _establishmentStaffRepository,
            long establishmentID) {
        return _establishmentStaffRepository.isAdminOfEstablishment(UserContext.getEmployeeId(), establishmentID);
    }

    public static boolean _hasAdminPermission(EstablishmentService establishmentService,
            long establishmentID) {
        return establishmentService.isAdmin(establishmentID, UserContext.getEmployeeId());
    }

    public boolean hasEmployeePermission(long establishmentId) {
        return establishmentStaffRepository.isEmployeeOfEstablishment(UserContext.getEmployeeId(), establishmentId);
    }
}
