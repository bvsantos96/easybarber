package com.teamsantos.easybarber.security.services;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.security.filters.EstablishmentSecurityExpressionRoot;
import com.teamsantos.easybarber.security.filters.UserSecurityExpressionRoot;

@Service
public class RolePermissionEvaluator implements PermissionEvaluator {
    private final UserRepository userRepository;
    private final EstablishmentStaffRepository establishmentStaffRepository;

    public static final String _ESTABLISHMENT_ADMIN = "ESTABLISHMENT-ADMIN";
    public static final String ESTABLISHMENT_ADMIN = "hasPermission(#establishmentId, " + _ESTABLISHMENT_ADMIN + ")";
    public static final String _IS_EMPLOYEE = "IS-EMPLOYEE";
    public static final String IS_EMPLOYEE = "hasPermission(" + _IS_EMPLOYEE + ")";

    @Autowired
    public RolePermissionEvaluator(UserRepository userRepository,
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
        return switch (strPermission) {
            case _ESTABLISHMENT_ADMIN -> {
                EstablishmentSecurityExpressionRoot root = new EstablishmentSecurityExpressionRoot(authentication,
                        userRepository, establishmentStaffRepository);
                yield root.hasAdminPermission((Long) targetDomainObject);
            }
            case _IS_EMPLOYEE -> {
                UserSecurityExpressionRoot userRoot = new UserSecurityExpressionRoot(authentication, userRepository);
                yield userRoot.isEmployee();
            }
            default -> throw new UnsupportedOperationException(
                    "hasPermission is not supported for permission " + strPermission);
        };
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
            Object permission) {
        if ((authentication == null) || (targetId == null) || !(permission instanceof String)) {
            return false;
        }
        String sPermission = ((String) permission).toUpperCase();
        return switch (sPermission) {
            case _ESTABLISHMENT_ADMIN -> {
                EstablishmentSecurityExpressionRoot root = new EstablishmentSecurityExpressionRoot(authentication,
                        userRepository, establishmentStaffRepository);
                yield root.hasAdminPermission(Long.parseLong(targetType));
            }
            case _IS_EMPLOYEE -> {
                UserSecurityExpressionRoot userRoot = new UserSecurityExpressionRoot(authentication, userRepository);
                yield userRoot.isEmployee();
            }
            default ->
                throw new UnsupportedOperationException("hasPermission is not supported for permission " + sPermission);
        };
    }
}
