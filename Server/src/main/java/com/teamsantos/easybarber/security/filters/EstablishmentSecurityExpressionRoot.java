package com.teamsantos.easybarber.security.filters;

import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

public class EstablishmentSecurityExpressionRoot extends SecurityExpressionRoot {
    private final UserRepository userRepository;
    private final EstablishmentStaffRepository establishmentStaffRepository;

    public EstablishmentSecurityExpressionRoot(Authentication authentication, UserRepository userRepository,
            EstablishmentStaffRepository establishmentStaffRepository) {
        super(authentication);
        this.userRepository = userRepository;
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
        Long userID = userRepository.findByMobileInformation(getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new)
                .getId();
        return establishmentStaffRepository.isUserAdminOfEstablishment(userID, establishmentId);
    }
}
