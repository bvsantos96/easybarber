package com.teamsantos.easybarber.security.services;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.security.filters.EstablishmentSecurityExpressionRoot;

@Service
public class EstablishmentPermissionEvaluator implements PermissionEvaluator {
    private final UserRepository userRepository;
    private final EstablishmentStaffRepository establishmentStaffRepository;

    public static final String _ESTABLISHMENT_ADMIN = "ESTABLISHMENT-ADMIN";
    public static final String ESTABLISHMENT_ADMIN = "hasPermission(#establishmentId, " + _ESTABLISHMENT_ADMIN + ")";

    @Autowired
    public EstablishmentPermissionEvaluator(UserRepository userRepository,
            EstablishmentStaffRepository establishmentStaffRepository) {
        this.userRepository = userRepository;
        this.establishmentStaffRepository = establishmentStaffRepository;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        String strPermission = ((String) permission).toUpperCase();
        switch (strPermission) {
            case _ESTABLISHMENT_ADMIN:
                EstablishmentSecurityExpressionRoot root = new EstablishmentSecurityExpressionRoot(authentication,
                        userRepository, establishmentStaffRepository);
                return root.hasAdminPermission((Long) targetDomainObject);
            default:
                throw new UnsupportedOperationException(
                        "hasPermission is not supported for permission " + strPermission);
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
            Object permission) {
        if ((authentication == null) || (targetId == null) || !(permission instanceof String)) {
            return false;
        }
        String sPermission = ((String) permission).toUpperCase();
        switch (sPermission) {
            case "ESTABLISHMENT-ADMIN":
                EstablishmentSecurityExpressionRoot root = new EstablishmentSecurityExpressionRoot(authentication,
                        userRepository, establishmentStaffRepository);
                return root.hasAdminPermission(Long.parseLong(targetType));
            default:
                throw new UnsupportedOperationException("hasPermission is not supported for permission " + sPermission);
        }
    }
}
