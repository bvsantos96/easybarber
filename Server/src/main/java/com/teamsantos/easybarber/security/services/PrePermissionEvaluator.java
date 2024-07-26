package com.teamsantos.easybarber.security.services;

import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.EmployeeScheduleRepository;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.ServiceRepository;
import com.teamsantos.easybarber.security.filters.EstablishmentSecurityExpressionRoot;
import com.teamsantos.easybarber.security.filters.ScheduleSecurityExpressionRoot;
import com.teamsantos.easybarber.security.filters.ServiceSecurityExpressionRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class PrePermissionEvaluator implements PermissionEvaluator {
    private final EstablishmentStaffRepository establishmentStaffRepository;
    private final ServiceRepository serviceRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeScheduleRepository scheduleRepository;

    public static final String _SCHEDULE_OWNER = "SCHEDULE_OWNER";
    public static final String SCHEDULE_OWNER = "hasPermission(#id, '" + _SCHEDULE_OWNER + "')";
    public static final String _ESTABLISHMENT_EMPLOYEE = "ESTABLISHMENT_EMPLOYEE";
    public static final String ESTABLISHMENT_EMPLOYEE = "hasPermission(#establishmentId, '" + _ESTABLISHMENT_EMPLOYEE
            + "')";
    public static final String ESTABLISHMENT_EMPLOYEE_OBJECT = "hasPermission(#obj.getEstablishmentId(), '"
            + _ESTABLISHMENT_EMPLOYEE + "')";
    public static final String _ESTABLISHMENT_ADMIN = "ESTABLISHMENT_ADMIN";
    public static final String ESTABLISHMENT_ADMIN = "hasPermission(#establishmentId, '" + _ESTABLISHMENT_ADMIN + "')";
    public static final String _SERVICE_OWNER = "SERVICE_OWNER";
    public static final String SERVICE_OWNER_OBJECT = "hasPermission(#service.getId(), '" + _SERVICE_OWNER + "')";
    public static final String SERVICE_OWNER_OBJECT_SERVICE_ID = "hasPermission(#service.getServiceId(), '"
            + _SERVICE_OWNER + "')";
    public static final String SERVICE_OWNER = "hasPermission(#serviceId, '" + _SERVICE_OWNER + "')";
    public static final String IS_EMPLOYEE = "hasRole('EMPLOYEE')";
    public static final String IS_SYSTEM_ADMIN = "hasRole('SYSTEM_ADMIN')";

    @Autowired
    public PrePermissionEvaluator(EstablishmentStaffRepository establishmentStaffRepository,
            ServiceRepository serviceRepository,
            EmployeeRepository employeeRepository,
            EmployeeScheduleRepository scheduleRepository) {
        this.establishmentStaffRepository = establishmentStaffRepository;
        this.serviceRepository = serviceRepository;
        this.employeeRepository = employeeRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        String strPermission = ((String) permission).toUpperCase();
        return switch (strPermission) {
            case _ESTABLISHMENT_EMPLOYEE -> {
                EstablishmentSecurityExpressionRoot root = new EstablishmentSecurityExpressionRoot(authentication,
                        employeeRepository, establishmentStaffRepository);
                yield root.hasEmployeePermission((Long) targetDomainObject);
            }
            case _ESTABLISHMENT_ADMIN -> {
                EstablishmentSecurityExpressionRoot root = new EstablishmentSecurityExpressionRoot(authentication,
                        employeeRepository, establishmentStaffRepository);
                yield root.hasAdminPermission((Long) targetDomainObject);
            }
            case _SERVICE_OWNER -> {
                ServiceSecurityExpressionRoot root = new ServiceSecurityExpressionRoot(authentication,
                        serviceRepository);
                yield root.hasServiceOwnerPermission((Long) targetDomainObject);
            }
            case _SCHEDULE_OWNER -> {
                ScheduleSecurityExpressionRoot root = new ScheduleSecurityExpressionRoot(authentication,
                        scheduleRepository);
                yield root.hasScheduleOwnerPermission((Long) targetDomainObject);
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
            case _ESTABLISHMENT_EMPLOYEE -> {
                EstablishmentSecurityExpressionRoot root = new EstablishmentSecurityExpressionRoot(authentication,
                        employeeRepository, establishmentStaffRepository);
                yield root.hasEmployeePermission(Long.parseLong(targetType));
            }
            case _ESTABLISHMENT_ADMIN -> {
                EstablishmentSecurityExpressionRoot root = new EstablishmentSecurityExpressionRoot(authentication,
                        employeeRepository, establishmentStaffRepository);
                yield root.hasAdminPermission(Long.parseLong(targetType));
            }
            case _SERVICE_OWNER -> {
                ServiceSecurityExpressionRoot root = new ServiceSecurityExpressionRoot(authentication,
                        serviceRepository);
                yield root.hasServiceOwnerPermission(Long.parseLong(targetType));
            }

            case _SCHEDULE_OWNER -> {
                ScheduleSecurityExpressionRoot root = new ScheduleSecurityExpressionRoot(authentication,
                        scheduleRepository);
                yield root.hasScheduleOwnerPermission(Long.parseLong(targetType));
            }
            default ->
                throw new UnsupportedOperationException("hasPermission is not supported for permission " + sPermission);
        };
    }
}
