package com.teamsantos.easybarber.testData;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.teamsantos.easybarber.DTO.AppointmentDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.CreateEstablishmentServiceDTO;

public class AppointmentData {
    public static final List<AppointmentDTO> appointments;
    public static final List<AppointmentDTO> appointmentsErrors;

    private static CreateEstablishmentServiceDTO getEstablishmentServiceOfEmployee(long id) {
        for (CreateEstablishmentServiceDTO establishmentService : EstablishmentData.establishmentServices) {
            if (getServiceFromEstablishmentService(establishmentService).getEmployeeId().equals(id)) {
                return establishmentService;
            }
        }
        return null;
    }

    private static ServiceDTO getServiceFromEstablishmentService(CreateEstablishmentServiceDTO establishmentService) {
        for (ServiceDTO service : ServiceData.services) {
            if (service.getId().equals(establishmentService.getServiceId())) {
                return service;
            }
        }
        return null;
    }

    static {
        appointments = new ArrayList<>() {
            {
                add(
                        new AppointmentDTO(
                                1L,
                                UsersData.usersDTO.get(0).getId(),
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(0))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(0).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(0).getServiceId(),
                                "First appointment",
                                null,
                                LocalDate.parse("2124-08-18"),
                                LocalTime.parse("10:00")));
                add(
                        new AppointmentDTO(
                                2L,
                                null,
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(1))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(1).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(1).getServiceId(),
                                "Second appointment",
                                "Pai",
                                LocalDate.parse("2124-08-17"),
                                LocalTime.parse("10:00")));
                add(
                        new AppointmentDTO(
                                3L,
                                null,
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(1))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(1).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(1).getServiceId(),
                                "Second appointment",
                                "Mae",
                                LocalDate.parse("2124-08-16"),
                                LocalTime.parse("10:00")));
            };
        };

        appointmentsErrors = new ArrayList<>() {
            {
                add(
                        new AppointmentDTO(
                                null,
                                UsersData.usersDTO.get(0).getId(),
                                ScheduleData.scheduleExceptions.get(0).getEmployeeId(),
                                getEstablishmentServiceOfEmployee(
                                        ScheduleData.scheduleExceptions.get(0).getEmployeeId()).getEstablishmentId(),
                                getEstablishmentServiceOfEmployee(
                                        ScheduleData.scheduleExceptions.get(0).getEmployeeId()).getServiceId(),
                                "This should fail",
                                "Pai",
                                ScheduleData.scheduleExceptions.get(0).getDateFrom(),
                                ScheduleData.scheduleExceptions.get(0).getStartHour().plusMinutes(1)));
            };
        };
    }
}
