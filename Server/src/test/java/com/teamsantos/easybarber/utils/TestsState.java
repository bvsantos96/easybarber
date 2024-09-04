package com.teamsantos.easybarber.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestsState {
    private static Set<String> completedSetups = Collections.synchronizedSet(new HashSet<>());

    public static String SERVICE_TYPES_CREATE_SERVICE_TYPES = "serviceTypesCreateServiceTypes";
    public static String SCHEDULE_CREATE_SCHEDULES = "scheduleCreateSchedules";
    public static String SCHEDULE_CREATE_EXCEPTIONS = "scheduleCreateExceptions";
    public static String SCHEDULE_DISABLE = "scheduleDisable";
    public static String ESTABLISHMENT_CREATE_ESTABLISHMENTS = "establishmentCreateEstablishments";
    public static String ESTABLISHMENT_TEST_EMPLOYEES = "establishmentTestEmployees";
    public static String ESTABLISHMENT_TEST_SERVICE = "establishmentTestService";
    public static String ESTABLISHMENT_ADD_IMAGES = "establishmentAddImages";
    public static String ESTABLISHMENT_DELETE_IMAGES = "establishmentDeleteImages";
    public static String EMPLOYEE_CREATE_EMPLOYEES = "employeeCreateEmployees";
    public static String EMPLOYEE_TEST_DELETE = "employeeTestDelete";
    public static String EMPLOYEE_CREATE_SERVICES = "employeeCreateServices";
    public static String EMPLOYEE_UPDATE_SERVICE = "employeeUpdateService";
    public static String EMPLOYEE_DELETE_IMAGES = "employeeDeleteImages";
    public static String EMPLOYEE_ADD_SERVICE_IMAGES = "employeeAddServiceImages";
    public static String EMPLOYEE_DELETE_SERVICE_IMAGES = "deleteServiceImages";
    public static String USER_TEST = "userTest";
    public static String APPOINTMENT_CREATE_APPOINTMENT = "appointmentCreateAppointment";
    public static String APPOINTMENT_CANCEL_APPOINTMENT = "appointmentCancelAppointment";
    public static String APPOINTMENT_CONFIRM_APPOINTMENT = "appointmentConfirmAppointment";
    public static String AUTH_TEST = "authTest";

    public static synchronized boolean ran(String setupName) {
        return completedSetups.contains(setupName);
    }

    public static synchronized void mark(String setupName) {
        completedSetups.add(setupName);
    }
}
