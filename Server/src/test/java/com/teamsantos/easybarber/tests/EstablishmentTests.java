package com.teamsantos.easybarber.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.testData.EstablishmentData;
import com.teamsantos.easybarber.testData.ServiceData;
import com.teamsantos.easybarber.utils.CreateTest;

@SpringBootTest
@AutoConfigureMockMvc
public class EstablishmentTests {
    private final MockMvc mockMvc;

    @Autowired
    public EstablishmentTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private void create(String path, String jwt, String item) throws Exception {
        CreateTest.createOrFound(mockMvc, path, jwt, item);
    }

    @Test
    public void test() {
        test(true, true);
    }

    public void test(boolean initAuth, boolean initEmployee) {
        try {
            String jwt;
            jwt = new AuthTests(mockMvc).login(initAuth);
            ResultActions result = CreateTest.post(mockMvc, "/establishment", jwt,
                    EstablishmentData.establishments.get(0).toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
            jwt = new EmployeeTests(mockMvc).login(initEmployee);
            create("/establishment", jwt, EstablishmentData.establishments.get(0).toString());
            jwt = new AuthTests(mockMvc).login(false);
            result = CreateTest.post(mockMvc, "/establishment", jwt,
                    EstablishmentData.establishments.get(1).toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
            jwt = new EmployeeTests(mockMvc).login(false);
            create("/establishment", jwt, EstablishmentData.establishments.get(1).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmployees() {
        testEmployees(true, true);
    }

    public void testEmployees(boolean initAuth, boolean initEmployee) {
        try {
            test(initAuth, initEmployee);
            String jwt = new EmployeeTests(mockMvc).login(false);
            create("/establishment/1/employee/2", jwt, EmployeeData.employees.get(0).toString());
            jwt = new EmployeeTests(mockMvc).login(1, false);
            ResultActions result = CreateTest.post(mockMvc, "/establishment/2/employee/2", jwt,
                    EmployeeData.employees.get(1).toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
            jwt = new EmployeeTests(mockMvc).login(false);
            create("/establishment/2/employee/2", jwt, EmployeeData.employees.get(0).toString());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testService() {
        testService(true, true);
    }

    public void testService(boolean initAuth, boolean initEmployee) {
        try {
            testEmployees(initAuth, initEmployee);
            new EmployeeTests(mockMvc).testServices(false);
            String jwt = new EmployeeTests(mockMvc).login(false);
            create("/establishment/1/service/1", jwt, ServiceData.services.get(0).toString());
            create("/establishment/1/service/2", jwt, ServiceData.services.get(1).toString());
            jwt = new EmployeeTests(mockMvc).login(1, false);
            ResultActions result = CreateTest.post(mockMvc, "/establishment/1/service/2", jwt,
                    ServiceData.services.get(2).toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
            jwt = new EmployeeTests(mockMvc).login(false);
            create("/establishment/1/service/3", jwt, ServiceData.services.get(2).toString());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }
}
