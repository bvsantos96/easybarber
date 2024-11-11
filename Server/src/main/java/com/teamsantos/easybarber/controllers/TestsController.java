package com.teamsantos.easybarber.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentDTO;
import com.teamsantos.easybarber.DTO.employee.EmployeeCreateDTO;
import com.teamsantos.easybarber.DTO.establishment.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.establishment.service.CreateEstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.schedule.ScheduleDTO;
import com.teamsantos.easybarber.DTO.schedule.ScheduleExceptionDTO;
import com.teamsantos.easybarber.DTO.service.CreateServiceDTO;
import com.teamsantos.easybarber.DTO.service.ServiceDTO;
import com.teamsantos.easybarber.DTO.service.ServiceTypeDTO;
import com.teamsantos.easybarber.DTO.user.UserCreateDTO;
import com.teamsantos.easybarber.entities.Appointment;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.EstablishmentServiceEmployee;
import com.teamsantos.easybarber.repositories.AppointmentRepository;
import com.teamsantos.easybarber.repositories.ServiceTypeRepository;
import com.teamsantos.easybarber.repositories.establishmentServices.EstablishmentServiceEmployeeRepository;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.security.utils.UserPrincipal;
import com.teamsantos.easybarber.services.EstablishmentService;
import com.teamsantos.easybarber.services.SchedulesService;
import com.teamsantos.easybarber.services.ServiceService;
import com.teamsantos.easybarber.services.UserService;

import jakarta.persistence.EntityManager;

@RestController
@Profile("test")
public class TestsController {
    @Value("${teamsantos.istestheavy}")
    private boolean isTestContext;

    private final EstablishmentService establishmentService;
    private final UserService userService;
    private final SchedulesService schedulesService;
    private final ServiceService serviceService;
    private final EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository;
    private final AppointmentRepository appointmentRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final Random random = new Random();
    private final EntityManager entityManager;

    private List<Long> serviceTypes;

    private final int nUsers = 1000;
    private final int nServicesPerEstablishment = 10;
    private final int nEmployeesPerEstablishment = 5;
    private final int nEstablishments = 100;
    private final int nServiceTypes = 5;

    private ScheduleDTO morning = new ScheduleDTO(
            null,
            null,
            null,
            Set.of(
                    DAY_OF_WEEK.MONDAY,
                    DAY_OF_WEEK.TUESDAY,
                    DAY_OF_WEEK.WEDNESDAY,
                    DAY_OF_WEEK.THURSDAY,
                    DAY_OF_WEEK.FRIDAY),
            LocalTime.parse("08:00"),
            LocalTime.parse("12:00"));

    private final ScheduleDTO evening = new ScheduleDTO(
            null,
            null,
            null,
            Set.of(
                    DAY_OF_WEEK.MONDAY,
                    DAY_OF_WEEK.TUESDAY,
                    DAY_OF_WEEK.WEDNESDAY,
                    DAY_OF_WEEK.THURSDAY,
                    DAY_OF_WEEK.FRIDAY),
            LocalTime.parse("08:00"),
            LocalTime.parse("12:00"));

    private ScheduleExceptionDTO exception = new ScheduleExceptionDTO(
            null, // id?
            null, // employeeId
            null, // establishmentId?
            Set.of(DAY_OF_WEEK.FRIDAY),
            LocalTime.parse("10:30"),
            LocalTime.parse("12:00"),
            LocalDate.parse("2024-11-01"),
            LocalDate.parse("2026-11-30"),
            true); // ative

    @Autowired
    public TestsController(UserService userService,
            EstablishmentService establishmentService,
            SchedulesService schedulesService,
            ServiceService serviceService,
            EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository,
            AppointmentRepository appointmentRepository,
            ServiceTypeRepository serviceTypeRepository,
            EntityManager entityManager) {
        this.serviceTypes = new ArrayList<>();
        this.establishmentService = establishmentService;
        this.userService = userService;
        this.schedulesService = schedulesService;
        this.serviceService = serviceService;
        this.establishmentServiceEmployeeRepository = establishmentServiceEmployeeRepository;
        this.appointmentRepository = appointmentRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.entityManager = entityManager;
    }

    @GetMapping("/createheavydb")
    public ResponseEntity<BaseResponseDTO> createDummyEstablishments() {
        if (!isTestContext) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponseDTO("Test context is disabled"));
        }
        try {
            createServiceTypes();
            createEstablishmentInfo();
            createDummyAppointments();
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponseDTO("Dummy establishments info created"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponseDTO(e.getMessage()));
        }
    }

    private void createServiceTypes() {
        for (int i = 0; i < nServiceTypes; i++) {
            System.out.println("Creating service type " + i);
            serviceTypes.add(serviceTypeRepository.save(ServiceTypeDTO.createDummy(i).toEntity()).getId());
        }
    }

    private long createEmployee(int i) throws Exception {
        System.out.println("Creating employee " + i);
        return userService.createUser(
                new EmployeeCreateDTO("+351", fillPhoneNumber(i), "Test123*", "Employee " + i, "Employee " + i), true)
                .getId();
    }

    private long createEstablishment(int i, long ownerId) throws Exception {
        System.out.println("Creating establishment " + i);
        return establishmentService.create(BaseEstablishmentDTO.createDummy(i), ownerId);
    }

    private void createSchedule(long employeeId, long establishmentId) {
        System.out.println("Creating schedule for employee " + employeeId + " in establishment " + establishmentId);
        morning.setEmployeeId(employeeId);
        morning.setEstablishmentId(establishmentId);
        schedulesService.create(morning, employeeId, true, true);
        if (random.nextBoolean()) {
            evening.setEmployeeId(employeeId);
            evening.setEstablishmentId(establishmentId);
            schedulesService.create(evening, employeeId, true, true);
        }
    }

    private void createException(long employeeId, long establishmentId) throws Exception {
        System.out.println("Creating exception for employee " + employeeId + " in establishment " + establishmentId);
        exception.setEmployeeId(employeeId);
        exception.setEstablishmentId(establishmentId);
        if (random.nextBoolean()) {
            schedulesService.createException(exception, employeeId);
        }
    }

    private void createService(int index, long establishmentId, long ownerId, List<Long> employees) throws Exception {
        System.out.println("Creating service " + index + " in establishment " + establishmentId);
        UserContext.clear();
        UserContext.setCurrentUser(new UserPrincipal(null, ownerId, List.of("employee")));
        long employeeServiceId = serviceService.createService(
                CreateServiceDTO.createDummy(ServiceDTO.createDummy(index, serviceTypes.get(index % nServiceTypes))));
        long establishmentServiceId = establishmentService.addService(establishmentId,
                CreateEstablishmentServiceDTO.createDummy(establishmentId, employeeServiceId));
        Set<Long> employeesIds = new HashSet<>();
        for (Long employeeId : employees) {
            if (random.nextInt(1, 4) > 2) {
                continue;
            }
            employeesIds.add(employeeId);
        }
        establishmentService.addEmployeesToService(establishmentId, establishmentServiceId, employeesIds);
    }

    private void createEstablishmentInfo() throws Exception {
        for (int i = 0; i < nEstablishments; i++) {
            long ownerId = createEmployee(i * nEmployeesPerEstablishment);
            long establishment = createEstablishment(i, ownerId);
            List<Long> _employees = new ArrayList<>();
            for (int j = 1; j < nEmployeesPerEstablishment; j++) {
                long employee = createEmployee(i * nEmployeesPerEstablishment + j);
                _employees.add(employee);
                establishmentService.addEmployee(establishment, employee);
                createSchedule(employee, establishment);
                createException(employee, establishment);
            }
            for (int k = 0; k < nServicesPerEstablishment; k++) {
                createService(i * nServicesPerEstablishment + k, establishment, ownerId, _employees);
            }
        }
    }

    private void createDummyAppointments() {
        try {
            // Get all establishmentServiceEmployees
            List<EstablishmentServiceEmployee> establishmentServiceEmployees = establishmentServiceEmployeeRepository
                    .findAll();
            for (int i = 0; i < nUsers; i++) {
                // Create user
                System.out.println("Creating user " + i);
                long userId = userService.createUser(
                        new UserCreateDTO("+99", fillPhoneNumber(nEmployeesPerEstablishment * nEstablishments + i),
                                "Test123*", "User " + i))
                        .getId();
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
                    System.out.println("Creating appointment(" + j + ") for user " + i);
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
        } catch (Exception e) {
            e.printStackTrace();
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
