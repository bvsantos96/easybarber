package com.teamsantos.easybarber.security.services;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.ServiceRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.security.filters.EstablishmentSecurityExpressionRoot;
import com.teamsantos.easybarber.security.filters.ServiceSecurityExpressionRoot;
import com.teamsantos.easybarber.security.filters.UserSecurityExpressionRoot;

@Service
public class PrePermissionEvaluator implements PermissionEvaluator {
    private final UserRepository userRepository;
    private final EstablishmentStaffRepository establishmentStaffRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;

    public static final String _ESTABLISHMENT_ADMIN = "ESTABLISHMENT_ADMIN";
    public static final String ESTABLISHMENT_ADMIN = "hasPermission(#establishmentId, " + _ESTABLISHMENT_ADMIN + ")";
    public static final String _IS_EMPLOYEE = "IS_EMPLOYEE";
    public static final String IS_EMPLOYEE = "hasPermission(#establishmentId, " + _IS_EMPLOYEE + ")";
    public static final String _SERVICE_OWNER = "SERVICE_OWNER";
    public static final String SERVICE_OWNER = "hasPermission(#serviceId, " + _SERVICE_OWNER + ")";
    public static final String _IS_USER = "IS_USER";
    public static final String IS_USER = "hasPermission(#userId, " + _IS_USER + ")";

    @Autowired
    public PrePermissionEvaluator(UserRepository userRepository,
            EstablishmentStaffRepository establishmentStaffRepository, EmployeeRepository employeeRepository,
            ServiceRepository serviceRepository) {
        this.userRepository = userRepository;
        this.establishmentStaffRepository = establishmentStaffRepository;
        this.employeeRepository = employeeRepository;
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
            case "IS_EMPLOYEE" -> {
                UserSecurityExpressionRoot userRoot = new UserSecurityExpressionRoot(authentication, userRepository,
                        employeeRepository);
                yield userRoot.isEmployee();
            }
            case _SERVICE_OWNER -> {
                ServiceSecurityExpressionRoot root = new ServiceSecurityExpressionRoot(authentication,
                        serviceRepository);
                yield root.hasServiceOwnerPermission((Long) targetDomainObject);
            }
            case _IS_USER -> {
                UserSecurityExpressionRoot userRoot = new UserSecurityExpressionRoot(authentication, userRepository,
                        employeeRepository);
                yield userRoot.isUser((Long) targetDomainObject);
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
                UserSecurityExpressionRoot userRoot = new UserSecurityExpressionRoot(authentication, userRepository,
                        employeeRepository);
                yield userRoot.isEmployee();
            }
            case _SERVICE_OWNER -> {
                ServiceSecurityExpressionRoot root = new ServiceSecurityExpressionRoot(authentication,
                        serviceRepository);
                yield root.hasServiceOwnerPermission(Long.parseLong(targetType));
            }
            case _IS_USER -> {
                UserSecurityExpressionRoot userRoot = new UserSecurityExpressionRoot(authentication, userRepository,
                        employeeRepository);
                yield userRoot.isUser(Long.parseLong(targetType));
            }
            default ->
                throw new UnsupportedOperationException("hasPermission is not supported for permission " + sPermission);
        };
    }
}
