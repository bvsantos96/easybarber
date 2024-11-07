package com.teamsantos.easybarber.tests;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.teamsantos.easybarber.DTO.appointment.AppointmentDTO;
import com.teamsantos.easybarber.DTO.establishment.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.establishment.service.CreateEstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.schedule.ScheduleDTO;
import com.teamsantos.easybarber.DTO.service.ServiceDTO;
import com.teamsantos.easybarber.DTO.service.ServiceTypeDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.testDTOs.ScheduleExceptionTestDTO;
import com.teamsantos.easybarber.testDTOs.UserTestDTO;
import com.teamsantos.easybarber.testData.UsersData;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.Pair;

@SpringBootTest
@AutoConfigureMockMvc
public class HeavyDBTests {
    @Value("${teamsantos.istestheavy}")
    private boolean isTestContext;

    private final MockMvc mockMvc;

    private final int nServicesPerEstablishment = 10;
    private final int nEmployeesPerEstablishment = 5;
    private final int nEstablishments = 100;
    private final int nUsers = 1000;
    private final int nServiceTypes = 5;
    private final int nPastAppointmentsPerUser = 10;
    private final int nFutureAppointmentsPerUser = 2;

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

    private ScheduleExceptionTestDTO exception = new ScheduleExceptionTestDTO(
            null, // id?
            null, // employeeId
            null, // establishmentId?
            Set.of(DAY_OF_WEEK.FRIDAY),
            LocalTime.parse("10:30"),
            LocalTime.parse("12:00"),
            LocalDate.parse("2024-11-01"),
            LocalDate.parse("2026-11-30"),
            true); // ative

    private Map<Long, Long> establishments = new HashMap<>();
    private Map<Long, String> users = new HashMap<>();
    private Map<Long, String> employees = new HashMap<>();
    private ArrayList<Long> serviceTypes = new ArrayList<>();
    private Map<Long, Pair<Long, Long>> services = new HashMap<>();

    private final Random random = new Random();

    @Autowired
    private HeavyDBTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private Pair<Long, String> createEstablishment(int index) throws Exception {
        Pair<Long, String> employee = createEmployee(index * nEmployeesPerEstablishment);
        long establishmentId = CreateTest.createId(mockMvc, "/establishment",
                employee.getSecond(), BaseEstablishmentDTO.createDummy(index).toString());
        establishments.put(establishmentId, employee.getFirst());
        return new Pair<>(establishmentId, employee.getSecond());
    }

    private Pair<Long, String> createUser(int index) throws Exception {
        UserTestDTO user = UserTestDTO.createDummy(index);
        long userId = CreateTest.createId(mockMvc, "/register", user.toString());
        String jwt = CreateTest.login(mockMvc, user);
        users.put(userId, jwt);
        return new Pair<>(userId, jwt);
    }

    private Pair<Long, String> createEmployee(int index) throws Exception {
        UserTestDTO user = UserTestDTO.createDummy(index);
        long employeeId = CreateTest.createId(mockMvc, "/employee", user.toString());
        String jwt = CreateTest.login(mockMvc, user);
        employees.put(employeeId, jwt);
        return new Pair<>(employeeId, jwt);
    }

    private void createSchedule(String jwt, long establishmentId) throws Exception {
        morning.setEstablishmentId(establishmentId);
        CreateTest.createOrFound(mockMvc, "/schedule?replaceExisting=true", jwt, morning.toString());
        if (random.nextBoolean()) {
            evening.setEstablishmentId(establishmentId);
            CreateTest.createOrFound(mockMvc, "/schedule?replaceExisting=true", jwt, evening.toString());
        }
    }

    private void createException(String jwt, Long establishmentId, Long employeeId) throws Exception {
        exception.setEmployeeId(employeeId);
        exception.setEstablishmentId(establishmentId);
        if (random.nextBoolean()) {
            CreateTest.createOrFound(mockMvc, "/schedule/exception", jwt, exception.toString());
        }
    }

    private void createServiceTypes() throws Exception {
        CreateTest.createId(mockMvc, "/registerAdmin", UsersData.systemAdmin.toString());
        String jwt = CreateTest.login(mockMvc, UsersData.systemAdmin);
        for (int i = 0; i < nServiceTypes; i++) {
            serviceTypes.add(CreateTest.createId(mockMvc, "/service", jwt,
                    ServiceTypeDTO.createDummy(i).toString()));
        }
    }

    private void createService(int index, String jwt, long establishmentId, List<Long> employees) throws Exception {
        ServiceDTO service = ServiceDTO.createDummy(index, serviceTypes.get(index % nServiceTypes));
        long employeeServiceId = CreateTest.createId(mockMvc, "/employee/service", jwt, service.toString());
        System.out.println(String.format("Creating service %d for establishment %d, index = %d",
                employeeServiceId, establishmentId, index));
        CreateEstablishmentServiceDTO eService = CreateEstablishmentServiceDTO.createDummy(establishmentId,
                employeeServiceId);
        long serviceId = 0;
        serviceId = CreateTest.createId(mockMvc, String.format("/establishment/%d/service", establishmentId),
                jwt, eService.toString());
        List<Long> employeesIds = new ArrayList<>();
        for (Long employeeId : employees) {
            if (random.nextInt(1, 4) > 2) {
                continue;
            }
            employeesIds.add(employeeId);
            services.put(serviceId, new Pair<>(establishmentId, employeeId));
        }
        CreateTest.post(mockMvc, String.format("/establishment/%d/service/%d/employee",
                establishmentId, serviceId),
                jwt, String.format(employeesIds.toString()));
    }

    private void createAppointments(int index, String jwt, long establishmentId, long serviceId, long employeeId)
            throws Exception {
        CreateTest.createOrFound(mockMvc, "/appointment", jwt,
                AppointmentDTO.createDummy(index, establishmentId, serviceId, employeeId).toString());
    }

    @Test
    public void createEstablishmentRelatedInfo() throws Exception {
        if (!isTestContext) {
            return;
        }
        createServiceTypes();
        for (int i = 0; i < nEstablishments; i++) {
            Pair<Long, String> establishment = createEstablishment(i);
            List<Long> _employees = new ArrayList<>();
            for (int j = 1; j < nEmployeesPerEstablishment; j++) {
                Pair<Long, String> employee = createEmployee(i * nEmployeesPerEstablishment + j);
                _employees.add(employee.getFirst());
                CreateTest.createId(mockMvc,
                        String.format("/establishment/%d/employee/%d", establishment.getFirst(), employee.getFirst()),
                        establishment.getSecond(), "{}");
                createSchedule(employee.getSecond(), establishment.getFirst());
                createException(employee.getSecond(), establishment.getFirst(), employee.getFirst());
            }
            for (int k = 0; k < nServicesPerEstablishment; k++) {
                createService(i * nServicesPerEstablishment + k, establishment.getSecond(), establishment.getFirst(),
                        _employees);
            }
        }
    }

    @Test
    public void createUserRelatedInfo() throws Exception {
        if (!isTestContext) {
            return;
        }
        CreateTest.get(mockMvc, "/appointments/createDummy");
    }
}
