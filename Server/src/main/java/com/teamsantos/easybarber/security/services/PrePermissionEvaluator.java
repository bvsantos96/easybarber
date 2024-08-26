package com.teamsantos.easybarber.security.services;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.repositories.AppointmentRepository;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.EmployeeScheduleRepository;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.ServiceRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.security.filters.AppointmentSecurityExpressionRoot;
import com.teamsantos.easybarber.security.filters.EstablishmentSecurityExpressionRoot;
import com.teamsantos.easybarber.security.filters.ScheduleSecurityExpressionRoot;
import com.teamsantos.easybarber.security.filters.ServiceSecurityExpressionRoot;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.services.UserTypeService.UserTypes;

@Service
public class PrePermissionEvaluator implements PermissionEvaluator {
    private final EstablishmentStaffRepository establishmentStaffRepository;
    private final ServiceRepository serviceRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

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
    public static final String IS_EMPLOYEE = "hasPermission('EMPLOYEE')";
    public static final String IS_SYSTEM_ADMIN = "hasPermission('SYSTEM_ADMIN')";
    public static final String _HAS_APPOINTMENT_CHANGE_PERMISSION = "HAS_APPOINTMENT_CHANGE_PERMISSION";
    public static final String HAS_APPOINTMENT_CHANGE_PERMISSION = "hasPermission(#id, '"
            + _HAS_APPOINTMENT_CHANGE_PERMISSION + "')";

    @Autowired
    public PrePermissionEvaluator(EstablishmentStaffRepository establishmentStaffRepository,
            ServiceRepository serviceRepository,
            EmployeeRepository employeeRepository,
            EmployeeScheduleRepository scheduleRepository,
            UserRepository userRepository,
            AppointmentRepository appointmentRepository) {
        this.establishmentStaffRepository = establishmentStaffRepository;
        this.serviceRepository = serviceRepository;
        this.employeeRepository = employeeRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        String strPermission = ((String) permission).toUpperCase();
        return switch (strPermission) {
            case IS_EMPLOYEE -> {
                yield UserContext.getCurrentUser().hasPermission(UserTypes.EMPLOYEE);
            }
            case IS_SYSTEM_ADMIN -> {
                yield UserContext.getCurrentUser().hasPermission(UserTypes.ADMIN);
            }
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
            case _HAS_APPOINTMENT_CHANGE_PERMISSION -> {
                AppointmentSecurityExpressionRoot root = new AppointmentSecurityExpressionRoot(authentication,
                        appointmentRepository, userRepository, employeeRepository);
                yield root.hasAppointmentChangePermission((Long) targetDomainObject);
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
            case IS_EMPLOYEE -> {
                yield UserContext.getCurrentUser().hasPermission(UserTypes.EMPLOYEE);
            }
            case IS_SYSTEM_ADMIN -> {
                yield UserContext.getCurrentUser().hasPermission(UserTypes.ADMIN);
            }
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

            case _HAS_APPOINTMENT_CHANGE_PERMISSION -> {
                AppointmentSecurityExpressionRoot root = new AppointmentSecurityExpressionRoot(authentication,
                        appointmentRepository, userRepository, employeeRepository);
                yield root.hasAppointmentChangePermission(Long.parseLong(targetType));
            }
            default ->
                throw new UnsupportedOperationException("hasPermission is not supported for permission " + sPermission);
        };
    }
}
