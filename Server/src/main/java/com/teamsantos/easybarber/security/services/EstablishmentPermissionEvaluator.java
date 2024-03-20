package com.teamsantos.easybarber.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.UserRepository;

@Service
public class EstablishmentPermissionEvaluator {
    private final UserRepository userRepository;
    private final EstablishmentStaffRepository establishmentStaffRepository;

    public boolean hasAdminPermission(Authentication auth, Long establishmentId) {
        if(!userRepository.existsByMobileInformation(auth.getName())) {
            throw new UserNotFoundException();
        }
        establishmentStaff
        return true;

    }
}
