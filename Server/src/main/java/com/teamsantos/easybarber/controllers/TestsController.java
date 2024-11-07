package com.teamsantos.easybarber.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentDTO;
import com.teamsantos.easybarber.DTO.user.UserCreateDTO;
import com.teamsantos.easybarber.entities.Appointment;
import com.teamsantos.easybarber.entities.EstablishmentServiceEmployee;
import com.teamsantos.easybarber.repositories.AppointmentRepository;
import com.teamsantos.easybarber.repositories.establishmentServices.EstablishmentServiceEmployeeRepository;
import com.teamsantos.easybarber.services.UserService;

import jakarta.persistence.EntityManager;

@RestController
@Profile("test")
public class TestsController {
    private final EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository;
    private final UserService userService;
    private final AppointmentRepository appointmentRepository;
    private final Random random = new Random();
    private final EntityManager entityManager;

    @Autowired

    public TestsController(EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository,
            UserService userService, AppointmentRepository appointmentRepository,
            EntityManager entityManager) {
        this.establishmentServiceEmployeeRepository = establishmentServiceEmployeeRepository;
        this.userService = userService;
        this.appointmentRepository = appointmentRepository;
        this.entityManager = entityManager;
    }

    @GetMapping("/appointments/createDummy")
    public ResponseEntity<BaseResponseDTO> createDummyAppointments() {
        try {
            // Get all establishmentServiceEmployees
            List<EstablishmentServiceEmployee> establishmentServiceEmployees = establishmentServiceEmployeeRepository
                    .findAll();
            final int nUsers = 1000;
            for (int i = 90; i < nUsers; i++) {
                // Create user
                System.out.println("Creating user " + i);
                long userId = userService.createUser(
                        new UserCreateDTO("+99", fillPhoneNumber(i), "Test123*", "User " + i)).getId();
                // Create appointments in the past
                int nAppointments = random.nextInt(0, 5);
                for (int j = 0; j < nAppointments; j++) {
                    EstablishmentServiceEmployee establishmentServiceEmployee = establishmentServiceEmployees
                            .get(random.nextInt(0, establishmentServiceEmployees.size()));
                    // Create appointment in the past
                    AppointmentDTO appointmentDTO = new AppointmentDTO();

                    appointmentDTO.setDate(LocalDate.now().minusDays(random.nextInt(1, 30)));
                    appointmentDTO.setTime(LocalTime.now().minusHours(random.nextInt(1, 5)));
                    appointmentDTO.setUserId(userId);
                    appointmentDTO.setEstablishmentId(establishmentServiceEmployee.getEstablishment().getId());
                    appointmentDTO.setServiceId(establishmentServiceEmployee.getService().getId());
                    appointmentDTO.setEmployeeId(establishmentServiceEmployee.getEmployee().getId());
                    appointmentDTO.setDescription("Test appointment " + i * nUsers + j);

                    Appointment appointment = appointmentDTO.toEntity(entityManager);
                    appointment.setActive(true);
                    appointment.setReminded(true);
                    appointment.setConfirmed(true);
                    appointment.setFeedbackAsked(true);
                    appointmentRepository.save(appointment);
                }
                // Create appointments in the future
                nAppointments = random.nextInt(0, 5);
                for (int j = 0; j < nAppointments; j++) {
                    EstablishmentServiceEmployee establishmentServiceEmployee = establishmentServiceEmployees
                            .get(random.nextInt(0, establishmentServiceEmployees.size()));
                    // Create appointment in the future
                    AppointmentDTO appointmentDTO = new AppointmentDTO();

                    appointmentDTO.setDate(LocalDate.now().plusDays(random.nextInt(1, 30)));
                    appointmentDTO.setTime(LocalTime.now().plusHours(random.nextInt(1, 5)));
                    appointmentDTO.setUserId(userId);
                    appointmentDTO.setEstablishmentId(establishmentServiceEmployee.getEstablishment().getId());
                    appointmentDTO.setServiceId(establishmentServiceEmployee.getService().getId());
                    appointmentDTO.setEmployeeId(establishmentServiceEmployee.getEmployee().getId());
                    appointmentDTO.setDescription("Test appointment " + i * nUsers + j);

                    Appointment appointment = appointmentDTO.toEntity(entityManager);
                    appointment.setActive(true);
                    appointment.setReminded(true);
                    appointment.setConfirmed(true);
                    appointment.setFeedbackAsked(true);
                    appointmentRepository.save(appointment);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Dummy appointments created"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    private String fillPhoneNumber(int index) {
        String phoneNumber = "" + index;
        while (phoneNumber.length() < 9) {
            phoneNumber = "0" + phoneNumber;
        }
        return phoneNumber;
    }
}
