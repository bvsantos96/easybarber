package com.teamsantos.easybarber.testData;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.teamsantos.easybarber.DTO.appointment.AppointmentDTO;
import com.teamsantos.easybarber.DTO.establishment.service.CreateEstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.service.ServiceDTO;

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
                                UsersData.usersDTO.get(0).getId(),
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(0))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(0).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(0).getServiceId(),
                                "First appointment",
                                new String(),
                                LocalDate.parse("2124-08-18"),
                                LocalTime.parse("10:00")));
                add(
                        new AppointmentDTO(
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
                                null,
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(1))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(1).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(1).getServiceId(),
                                "Second appointment",
                                "Mae",
                                LocalDate.parse("2124-08-16"),
                                LocalTime.parse("10:00")));
                add(
                        new AppointmentDTO(
                                UsersData.usersDTO.get(1).getId(),
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(1))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(1).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(1).getServiceId(),
                                "Corte de cabelo",
                                "",
                                LocalDate.parse("2124-08-15"),
                                LocalTime.parse("10:00")));
                add(
                        new AppointmentDTO(
                                UsersData.usersDTO.get(1).getId(),
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(2))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(2).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(2).getServiceId(),
                                "Barba",
                                "",
                                LocalDate.parse("2124-08-14"),
                                LocalTime.parse("10:00")));

                add(
                        new AppointmentDTO(
                                UsersData.usersDTO.get(1).getId(),
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(1))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(1).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(1).getServiceId(),
                                "Corte de cabelo",
                                "",
                                LocalDate.parse("2124-08-11"),
                                LocalTime.parse("10:00")));
                add(
                        new AppointmentDTO(
                                UsersData.usersDTO.get(1).getId(),
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(2))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(2).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(2).getServiceId(),
                                "Barba",
                                "",
                                LocalDate.parse("2124-08-10"),
                                LocalTime.parse("10:00")));
                add(
                        new AppointmentDTO(
                                UsersData.usersDTO.get(1).getId(),
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(1))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(1).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(1).getServiceId(),
                                "Corte de cabelo",
                                "",
                                LocalDate.parse("2124-08-09"),
                                LocalTime.parse("10:00")));
                add(
                        new AppointmentDTO(
                                UsersData.usersDTO.get(1).getId(),
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(2))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(2).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(2).getServiceId(),
                                "Barba",
                                "",
                                LocalDate.parse("2124-08-08"),
                                LocalTime.parse("10:00")));
                add(
                        new AppointmentDTO(
                                UsersData.usersDTO.get(1).getId(),
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(1))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(1).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(1).getServiceId(),
                                "Corte de cabelo",
                                "",
                                LocalDate.parse("2124-08-07"),
                                LocalTime.parse("10:00")));
                add(
                        new AppointmentDTO(
                                UsersData.usersDTO.get(1).getId(),
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(2))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(2).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(2).getServiceId(),
                                "Barba",
                                "",
                                LocalDate.parse("2124-08-04"),
                                LocalTime.parse("10:00")));
                add(
                        new AppointmentDTO(
                                UsersData.usersDTO.get(1).getId(),
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(1))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(1).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(1).getServiceId(),
                                "Corte de cabelo",
                                "",
                                LocalDate.parse("2124-08-03"),
                                LocalTime.parse("10:00")));
                add(
                        new AppointmentDTO(
                                UsersData.usersDTO.get(1).getId(),
                                getServiceFromEstablishmentService(EstablishmentData.establishmentServices.get(2))
                                        .getEmployeeId(),
                                EstablishmentData.establishmentServices.get(2).getEstablishmentId(),
                                EstablishmentData.establishmentServices.get(2).getServiceId(),
                                "Barba",
                                "",
                                LocalDate.parse("2124-08-02"),
                                LocalTime.parse("10:00")));
            };
        };

        appointmentsErrors = new ArrayList<>() {
            {
                add(
                        new AppointmentDTO(
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
