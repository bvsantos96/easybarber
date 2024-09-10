package com.teamsantos.easybarber.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestsState {
    public static final String SERVICE_TYPES_CREATE_SERVICE_TYPES = "serviceTypesCreateServiceTypes";
    public static final String SCHEDULE_CREATE_SCHEDULES = "scheduleCreateSchedules";
    public static final String SCHEDULE_CREATE_EXCEPTIONS = "scheduleCreateExceptions";
    public static final String SCHEDULE_DISABLE = "scheduleDisable";
    public static final String ESTABLISHMENT_CREATE_ESTABLISHMENTS = "establishmentCreateEstablishments";
    public static final String ESTABLISHMENT_TEST_EMPLOYEES = "establishmentTestEmployees";
    public static final String ESTABLISHMENT_TEST_SERVICE = "establishmentTestService";
    public static final String ESTABLISHMENT_ADD_IMAGES = "establishmentAddImages";
    public static final String ESTABLISHMENT_DELETE_IMAGES = "establishmentDeleteImages";
    public static final String EMPLOYEE_CREATE_EMPLOYEES = "employeeCreateEmployees";
    public static final String EMPLOYEE_TEST_DELETE = "employeeTestDelete";
    public static final String EMPLOYEE_CREATE_SERVICES = "employeeCreateServices";
    public static final String EMPLOYEE_UPDATE_SERVICE = "employeeUpdateService";
    public static final String EMPLOYEE_DELETE_IMAGES = "employeeDeleteImages";
    public static final String EMPLOYEE_ADD_SERVICE_IMAGES = "employeeAddServiceImages";
    public static final String EMPLOYEE_DELETE_SERVICE_IMAGES = "deleteServiceImages";
    public static final String USER_TEST = "userTest";
    public static final String APPOINTMENT_CREATE_APPOINTMENT = "appointmentCreateAppointment";
    public static final String APPOINTMENT_CANCEL_APPOINTMENT = "appointmentCancelAppointment";
    public static final String APPOINTMENT_CONFIRM_APPOINTMENT = "appointmentConfirmAppointment";
    public static final String AUTH_TEST = "authTest";
    public static final String AUTH_CREATE_SYSTEM_ADMIN = "authCreateSystemAdmin";

    public static String SYSTEM_ADMIN_JWT = null;

    private static Set<String> completedSetups = Collections.synchronizedSet(new HashSet<>());

    public static boolean ran(String setupName) {
        return completedSetups.contains(setupName);
    }

    public static void mark(String setupName) {
        completedSetups.add(setupName);
    }

}
