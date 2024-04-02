package com.teamsantos.easybarber.security.services;

import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.ServiceRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.security.filters.EstablishmentSecurityExpressionRoot;
import com.teamsantos.easybarber.security.filters.ServiceSecurityExpressionRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class PrePermissionEvaluator implements PermissionEvaluator {
    private final UserRepository userRepository;
    private final EstablishmentStaffRepository establishmentStaffRepository;
    private final ServiceRepository serviceRepository;

    public static final String _ESTABLISHMENT_ADMIN = "ESTABLISHMENT_ADMIN";
    public static final String ESTABLISHMENT_ADMIN = "hasPermission(#establishmentId, " + _ESTABLISHMENT_ADMIN + ")";
    public static final String _SERVICE_OWNER = "SERVICE_OWNER";
    public static final String SERVICE_OWNER = "hasPermission(#serviceId, " + _SERVICE_OWNER + ")";
    public static final String IS_EMPLOYEE = "hasRole('EMPLOYEE')";

    @Autowired
    public PrePermissionEvaluator(UserRepository userRepository,
            EstablishmentStaffRepository establishmentStaffRepository,
            ServiceRepository serviceRepository) {
        this.userRepository = userRepository;
        this.establishmentStaffRepository = establishmentStaffRepository;
        this.serviceRepository = serviceRepository;
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
            case _SERVICE_OWNER -> {
                ServiceSecurityExpressionRoot root = new ServiceSecurityExpressionRoot(authentication,
                        serviceRepository);
                yield root.hasServiceOwnerPermission((Long) targetDomainObject);
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
            case _SERVICE_OWNER -> {
                ServiceSecurityExpressionRoot root = new ServiceSecurityExpressionRoot(authentication,
                        serviceRepository);
                yield root.hasServiceOwnerPermission(Long.parseLong(targetType));
            }
            default ->
                throw new UnsupportedOperationException("hasPermission is not supported for permission " + sPermission);
        };
    }
}
