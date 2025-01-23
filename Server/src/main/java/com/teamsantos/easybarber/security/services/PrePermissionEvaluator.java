package com.teamsantos.easybarber.security.services;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.repositories.AppointmentRepository;
import com.teamsantos.easybarber.repositories.EmployeeScheduleRepository;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.establishmentServices.EstablishmentServiceEmployeeRepository;
import com.teamsantos.easybarber.repositories.establishmentServices.EstablishmentServiceRepository;
import com.teamsantos.easybarber.repositories.services.ServiceRepository;
import com.teamsantos.easybarber.security.filters.AppointmentSecurityExpressionRoot;
import com.teamsantos.easybarber.security.filters.EstablishmentSecurityExpressionRoot;
import com.teamsantos.easybarber.security.filters.ScheduleSecurityExpressionRoot;
import com.teamsantos.easybarber.security.filters.ServiceSecurityExpressionRoot;
import com.teamsantos.easybarber.utils.Pair;

@Service
public class PrePermissionEvaluator implements PermissionEvaluator {
    private final EstablishmentStaffRepository establishmentStaffRepository;
    private final ServiceRepository serviceRepository;
    private final EmployeeScheduleRepository scheduleRepository;
    private final AppointmentRepository appointmentRepository;
    private final EstablishmentServiceRepository establishmentServiceRepository;
    private final EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository;

    public static final String IS_EMPLOYEE = "hasRole('EMPLOYEE')";
    public static final String IS_SYSTEM_ADMIN = "hasRole('SYSTEM_ADMIN')";
    private static final String _SCHEDULE_OWNER = "SCHEDULE_OWNER";
    public static final String SCHEDULE_OWNER = "hasPermission(#id, '" + _SCHEDULE_OWNER + "')";
    private static final String _ESTABLISHMENT_EMPLOYEE = "ESTABLISHMENT_EMPLOYEE";
    public static final String ESTABLISHMENT_EMPLOYEE = "hasPermission(#establishmentId, '" + _ESTABLISHMENT_EMPLOYEE
            + "')";
    public static final String ESTABLISHMENT_EMPLOYEE_OBJECT = "hasPermission(#obj.getEstablishmentId(), '"
            + _ESTABLISHMENT_EMPLOYEE + "')";
    private static final String _ESTABLISHMENT_ADMIN = "ESTABLISHMENT_ADMIN";
    public static final String ESTABLISHMENT_ADMIN = "hasPermission(#establishmentId, '" + _ESTABLISHMENT_ADMIN + "')";
    private static final String _SERVICE_OWNER = "SERVICE_OWNER";
    public static final String SERVICE_OWNER_OBJECT = "hasPermission(#service.getId(), '" + _SERVICE_OWNER + "')";
    public static final String SERVICE_OWNER_OBJECT_SERVICE_ID = "hasPermission(#service.getServiceId(), '"
            + _SERVICE_OWNER + "')";
    public static final String SERVICE_OWNER = "hasPermission(#serviceId, '" + _SERVICE_OWNER + "')";
    private static final String _HAS_APPOINTMENT_CHANGE_PERMISSION = "HAS_APPOINTMENT_CHANGE_PERMISSION";
    public static final String HAS_APPOINTMENT_CHANGE_PERMISSION = "hasPermission(#id, '"
            + _HAS_APPOINTMENT_CHANGE_PERMISSION + "')";
    public static final String HAS_APPOINTMENT_CHANGE_PERMISSION_OBJECT = "hasPermission(#cancelAppointmentDTO.getId(), '"
            + _HAS_APPOINTMENT_CHANGE_PERMISSION + "')";
    private static final String _CAN_WRITE_SERVICE_DYNAMIC_PRICE = "CAN_WRITE_SERVICE_DYNAMIC_PRICE";
    public static final String CAN_WRITE_SERVICE_DYNAMIC_PRICE = "hasPermission(#establishmentServiceId, #establishmentServiceEmployeeId, '"
            + _CAN_WRITE_SERVICE_DYNAMIC_PRICE + "')";
    private static final String _CAN_READ_SERVICE_DYNAMIC_PRICE = "CAN_READ_SERVICE_DYNAMIC_PRICE";
    public static final String CAN_READ_SERVICE_DYNAMIC_PRICE = "hasPermission(#establishmentServiceId, #establishmentServiceEmployeeId, '"
            + _CAN_READ_SERVICE_DYNAMIC_PRICE + "')";

    @Autowired
    public PrePermissionEvaluator(EstablishmentStaffRepository establishmentStaffRepository,
            ServiceRepository serviceRepository,
            EmployeeScheduleRepository scheduleRepository,
            AppointmentRepository appointmentRepository,
            EstablishmentServiceRepository establishmentServiceRepository,
            EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository) {
        this.establishmentStaffRepository = establishmentStaffRepository;
        this.serviceRepository = serviceRepository;
        this.scheduleRepository = scheduleRepository;
        this.appointmentRepository = appointmentRepository;
        this.establishmentServiceRepository = establishmentServiceRepository;
        this.establishmentServiceEmployeeRepository = establishmentServiceEmployeeRepository;
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
                        establishmentStaffRepository);
                yield root.hasEmployeePermission((Long) targetDomainObject);
            }
            case _ESTABLISHMENT_ADMIN -> {
                EstablishmentSecurityExpressionRoot root = new EstablishmentSecurityExpressionRoot(authentication,
                        establishmentStaffRepository);
                yield root.hasAdminPermission((Long) targetDomainObject);
            }
            case _SERVICE_OWNER -> {
                ServiceSecurityExpressionRoot root = new ServiceSecurityExpressionRoot(authentication,
                        serviceRepository, establishmentServiceEmployeeRepository, establishmentServiceRepository);
                yield root.hasServiceOwnerPermission((Long) targetDomainObject);
            }
            case _SCHEDULE_OWNER -> {
                ScheduleSecurityExpressionRoot root = new ScheduleSecurityExpressionRoot(authentication,
                        scheduleRepository);
                yield root.hasScheduleOwnerPermission((Long) targetDomainObject);
            }
            case _HAS_APPOINTMENT_CHANGE_PERMISSION -> {
                AppointmentSecurityExpressionRoot root = new AppointmentSecurityExpressionRoot(authentication,
                        appointmentRepository);
                yield root.hasAppointmentChangePermission((Long) targetDomainObject);
            }
            case _CAN_WRITE_SERVICE_DYNAMIC_PRICE -> {
                ServiceSecurityExpressionRoot root = new ServiceSecurityExpressionRoot(authentication,
                        serviceRepository, establishmentServiceEmployeeRepository, establishmentServiceRepository);
                try {
                    @SuppressWarnings("unchecked")
                    Pair<Long, Long> pair = (Pair<Long, Long>) targetDomainObject;
                    yield root.hasServiceEstablishmentWritePermission(pair.getFirst(), pair.getSecond());
                } catch (Exception e) {
                    yield false;
                }
            }
            case _CAN_READ_SERVICE_DYNAMIC_PRICE -> {
                ServiceSecurityExpressionRoot root = new ServiceSecurityExpressionRoot(authentication,
                        serviceRepository, establishmentServiceEmployeeRepository, establishmentServiceRepository);
                @SuppressWarnings("unchecked")
                Pair<Long, Long> pair = (Pair<Long, Long>) targetDomainObject;
                yield root.hasServiceEstablishmentWritePermission(pair.getFirst(), pair.getSecond());
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
                        establishmentStaffRepository);
                yield root.hasEmployeePermission(Long.parseLong(targetType));
            }
            case _ESTABLISHMENT_ADMIN -> {
                EstablishmentSecurityExpressionRoot root = new EstablishmentSecurityExpressionRoot(authentication,
                        establishmentStaffRepository);
                yield root.hasAdminPermission(Long.parseLong(targetType));
            }
            case _SERVICE_OWNER -> {
                ServiceSecurityExpressionRoot root = new ServiceSecurityExpressionRoot(authentication,
                        serviceRepository, establishmentServiceEmployeeRepository, establishmentServiceRepository);
                yield root.hasServiceOwnerPermission(Long.parseLong(targetType));
            }

            case _SCHEDULE_OWNER -> {
                ScheduleSecurityExpressionRoot root = new ScheduleSecurityExpressionRoot(authentication,
                        scheduleRepository);
                yield root.hasScheduleOwnerPermission(Long.parseLong(targetType));
            }

            case _HAS_APPOINTMENT_CHANGE_PERMISSION -> {
                AppointmentSecurityExpressionRoot root = new AppointmentSecurityExpressionRoot(authentication,
                        appointmentRepository);
                yield root.hasAppointmentChangePermission(Long.parseLong(targetType));
            }
            case _CAN_WRITE_SERVICE_DYNAMIC_PRICE -> {
                ServiceSecurityExpressionRoot root = new ServiceSecurityExpressionRoot(authentication,
                        serviceRepository, establishmentServiceEmployeeRepository, establishmentServiceRepository);
                Long[] ids = (Long[]) targetId;
                yield root.hasServiceEstablishmentWritePermission(ids[0], ids[1]);
            }
            case _CAN_READ_SERVICE_DYNAMIC_PRICE -> {
                ServiceSecurityExpressionRoot root = new ServiceSecurityExpressionRoot(authentication,
                        serviceRepository, establishmentServiceEmployeeRepository, establishmentServiceRepository);
                Long[] ids = (Long[]) targetId;
                yield root.hasServiceEstablishmentReadPermission(ids[0], ids[1]);
            }
            default ->
                throw new UnsupportedOperationException("hasPermission is not supported for permission " + sPermission);
        };
    }
}
