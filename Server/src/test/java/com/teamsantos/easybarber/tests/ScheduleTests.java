package com.teamsantos.easybarber.tests;

import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.utils.JSONToDTO;
import com.teamsantos.easybarber.utils.PageDTO;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.testData.ScheduleData;
import com.teamsantos.easybarber.DTO.ScheduleDTO;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleTests {
    private final MockMvc mockMvc;

    @Autowired
    public ScheduleTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private void create(String path, String jwt, String item) throws Exception {
        CreateTest.createOrFound(mockMvc, path, jwt, item);
    }

    private void createForbiden(String path, String jwt, String item) throws Exception {
        CreateTest.createForbidden(mockMvc, path, jwt, item);
    }

    @Test
    public void createSchedules() {
        createSchedules(true);
    }

    public void createSchedules(boolean init) {
        try {
            new EstablishmentTests(mockMvc).testEmployees(init, init);
            ScheduleData.schedules.forEach(schedule -> {
                try {
                    String jwt = new EmployeeTests(mockMvc).loginById(schedule.getEmployeeId(), false);
                    create("/schedule", jwt, schedule.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    org.junit.jupiter.api.Assertions.fail(e.getMessage());
                }
            });

            ScheduleData.schedulesErrors.forEach(schedule -> {
                try {
                    String jwt = new EmployeeTests(mockMvc).loginById(schedule.getEmployeeId(), false);
                    createForbiden("/schedule", jwt, schedule.toString());
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
    public void listSchedules() {
        try {
            createSchedules(true);
            long employeeId = EmployeeData.employees.get(0).getId();
            String jwt = new EmployeeTests(mockMvc).login(false);
            ResultActions result = CreateTest.get(mockMvc, "/employee/schedule", jwt);
            result.andExpect(MockMvcResultMatchers.status().isOk());
            List<ScheduleDTO> schedules = JSONToDTO.fromPageDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), ScheduleDTO.class);
            assert schedules != null;
            List<ScheduleDTO> _schedules = getScheduleByEmployeeId(employeeId, schedules);
            for (int i = 0; i < schedules.size(); i++) {
                assert schedules.get(i).equals(_schedules.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    private List<ScheduleDTO> getScheduleByEmployeeId(long employeeId, List<ScheduleDTO> schedules) {
        return schedules.stream().filter(e -> e.getEmployeeId().equals(employeeId)).collect(Collectors.toList());
    }
}
