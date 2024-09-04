package com.teamsantos.easybarber.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.teamsantos.easybarber.DTO.AppointmentDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.testData.AppointmentData;
import com.teamsantos.easybarber.testData.UsersData;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.JSONToDTO;
import com.teamsantos.easybarber.utils.TestsState;

@SpringBootTest
@AutoConfigureMockMvc
public class AppointmentTests {
    private final MockMvc mockMvc;

    @Autowired
    public AppointmentTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private Long create(String path, String jwt, String item) throws Exception {
        ResultActions result = CreateTest.createOrFoundWithResult(mockMvc, path, jwt, item);
        BaseResponseDTO response = JSONToDTO.toDTO(
                new JSONObject(result.andReturn().getResponse().getContentAsString()),
                BaseResponseDTO.class);
        return response.getId();
    }

    private void createException(String path, String jwt, String item) throws Exception {
        CreateTest.createBadRequest(mockMvc, path, jwt, item);
    }

    @Test
    public void createAppointment() {
        createAppointment(true, true);
    }

    private void createAppointment(boolean initAuth, boolean initEmployee) {
        if (TestsState.ran(TestsState.APPOINTMENT_CREATE_APPOINTMENT)) {
            return;
        }
        TestsState.mark(TestsState.APPOINTMENT_CREATE_APPOINTMENT);
        try {
            new ScheduleTests(mockMvc).createExceptions(initAuth, initEmployee);
            new EstablishmentTests(mockMvc).testService(false, false);
            AppointmentData.appointments.forEach(appointment -> {
                try {
                    String jwt;
                    if (appointment.getUserId() != null) {
                        appointment.setUserId(UsersData.usersDTO.get(0).getId());
                        jwt = new AuthTests(mockMvc).loginById(appointment.getUserId(), false);
                    } else {
                        jwt = new EmployeeTests(mockMvc).loginById(appointment.getEmployeeId(), false);
                    }
                    create("/appointment", jwt, appointment.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    org.junit.jupiter.api.Assertions.fail(e.getMessage());
                }
            });
            AppointmentData.appointmentsErrors.forEach(appointment -> {
                try {
                    String jwt;
                    if (appointment.getUserId() != null) {
                        jwt = new AuthTests(mockMvc).loginById(appointment.getUserId(), false);
                    } else {
                        jwt = new EmployeeTests(mockMvc).loginById(appointment.getEmployeeId(), false);
                    }
                    createException("/appointment", jwt, appointment.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    org.junit.jupiter.api.Assertions.fail(e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listAppointments() {
        listAppointments(true, true);
    }

    private Map<Long, List<AppointmentDTO>> getAppointmentsByEstablishment() {
        Map<Long, List<AppointmentDTO>> appointments = new HashMap<>();
        for (int i = 0; i < AppointmentData.appointments.size(); i++) {
            if (appointments.containsKey(AppointmentData.appointments.get(i).getEstablishmentId())) {
                appointments.get(AppointmentData.appointments.get(i).getEstablishmentId())
                        .add(AppointmentData.appointments.get(i));
            } else {
                ArrayList<AppointmentDTO> list = new ArrayList<>();
                list.add(AppointmentData.appointments.get(i));
                appointments.put(AppointmentData.appointments.get(i).getEstablishmentId(), list);
            }
        }
        return appointments;
    }

    private Map<Long, List<AppointmentDTO>> getAppointmentsByEmployee() {
        Map<Long, List<AppointmentDTO>> appointments = new HashMap<>();
        for (int i = 0; i < AppointmentData.appointments.size(); i++) {
            if (appointments.containsKey(AppointmentData.appointments.get(i).getEmployeeId())) {
                appointments.get(AppointmentData.appointments.get(i).getEmployeeId())
                        .add(AppointmentData.appointments.get(i));
            } else {
                ArrayList<AppointmentDTO> list = new ArrayList<>();
                list.add(AppointmentData.appointments.get(i));
                appointments.put(AppointmentData.appointments.get(i).getEmployeeId(), list);
            }
        }
        return appointments;
    }

    private Map<Long, List<AppointmentDTO>> getAppointmentsByUser() {
        Map<Long, List<AppointmentDTO>> appointments = new HashMap<>();
        for (int i = 0; i < AppointmentData.appointments.size(); i++) {
            if (appointments.containsKey(AppointmentData.appointments.get(i).getUserId())) {
                appointments.get(AppointmentData.appointments.get(i).getUserId())
                        .add(AppointmentData.appointments.get(i));
            } else {
                ArrayList<AppointmentDTO> list = new ArrayList<>();
                list.add(AppointmentData.appointments.get(i));
                appointments.put(AppointmentData.appointments.get(i).getUserId(), list);
            }
        }
        return appointments;
    }

    private void validateAppointmentList(Map<Long, List<AppointmentDTO>> appointmentsMap, String fieldName)
            throws Exception {
        for (Long userId : appointmentsMap.keySet()) {
            if (userId == null) {
                continue;
            }
            String jwt = "";
            switch (fieldName) {
                case "clientId":
                    jwt = new AuthTests(mockMvc).loginById(userId, false);
                    break;
                case "employeeId":
                    jwt = new EmployeeTests(mockMvc).loginById(userId, false);
                    break;
                case "establishmentId":
                    jwt = new EstablishmentTests(mockMvc).loginAdminByEstablishmentId(userId);
                    break;
            }
            String url = String.format("/appointments?%s=%d", fieldName, userId);
            ResultActions result = CreateTest.get(mockMvc, url, jwt);
            List<AppointmentDTO> appointments = JSONToDTO.fromPageDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), AppointmentDTO.class);
            assert appointments != null;
            List<AppointmentDTO> _appointments = appointmentsMap.get(userId);
            for (int i = 0; i < _appointments.size(); i++) {
                assert _appointments.get(i).equals(appointments.get(i));
            }
        }

    }

    public void listAppointments(boolean initAuth, boolean initEmployee) {
        try {
            createAppointment(initAuth, initEmployee);
            Map<Long, List<AppointmentDTO>> appointmentsMap = getAppointmentsByUser();
            validateAppointmentList(appointmentsMap, "clientId");
            appointmentsMap = getAppointmentsByEmployee();
            validateAppointmentList(appointmentsMap, "employeeId");
            appointmentsMap = getAppointmentsByEstablishment();
            validateAppointmentList(appointmentsMap, "establishmentId");
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void cancelAppointment() {
        cancelAppointment(true);
    }

    public void cancelAppointment(boolean init) {
        if (TestsState.ran(TestsState.APPOINTMENT_CANCEL_APPOINTMENT)) {
            return;
        }
        TestsState.mark(TestsState.APPOINTMENT_CANCEL_APPOINTMENT);
        try {
            EmployeeTests employeeTests = new EmployeeTests(mockMvc);
            AuthTests authTests = new AuthTests(mockMvc);
            createAppointment(init, init);
            for (int i = 0; i < AppointmentData.appointments.size(); i++) {
                AppointmentDTO appointment = AppointmentData.appointments.get(i);
                String jwt = "";
                if (i % 2 == 0) {
                    if (appointment.getUserId() == null) {
                        jwt = employeeTests.loginById(appointment.getEmployeeId(), false);
                    } else {
                        jwt = authTests.loginById(appointment.getUserId(), false);
                    }
                    String url = String.format("/appointment/%d/cancel", appointment.getId());
                    CreateTest.putSuccessWJWT(mockMvc, url, jwt);
                } else {
                    jwt = employeeTests.loginById(
                            EmployeeTests.getDifferentEmployee(appointment.getEmployeeId()),
                            false);
                    String url = String.format("/appointment/%d/cancel", appointment.getId());
                    CreateTest.putForbiddenWJWT(mockMvc, url, jwt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void confirmAppointment() {
        confirmAppointment(true);
    }

    public void confirmAppointment(boolean init) {
        if (TestsState.ran(TestsState.APPOINTMENT_CONFIRM_APPOINTMENT)) {
            return;
        }
        TestsState.mark(TestsState.APPOINTMENT_CONFIRM_APPOINTMENT);
        try {
            EmployeeTests employeeTests = new EmployeeTests(mockMvc);
            createAppointment(init, init);
            String jwt;
            for (int i = 0; i < AppointmentData.appointments.size(); i++) {
                AppointmentDTO appointment = AppointmentData.appointments.get(i);
                if (i % 2 == 0) {
                    jwt = employeeTests.loginById(appointment.getEmployeeId(), false);
                    String url = String.format("/appointment/%d/confirm", appointment.getId());
                    CreateTest.putSuccessWJWT(mockMvc, url, jwt);
                } else {
                    jwt = employeeTests.loginById(
                            EmployeeTests.getDifferentEmployee(appointment.getEmployeeId()),
                            false);
                    String url = String.format("/appointment/%d/confirm", appointment.getId());
                    CreateTest.putBadRequestWJWT(mockMvc, url, jwt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }
}
