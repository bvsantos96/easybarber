package com.teamsantos.easybarber.tests;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.DTO.EmployeeDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.testData.EstablishmentData;
import com.teamsantos.easybarber.testData.ServiceData;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.JSONToDTO;

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
            new EmployeeTests(mockMvc).createServices(false);
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

    @Test
    public void listEmployees() {
        try {
            testEmployees(true, true);
            ResultActions result = CreateTest.get(mockMvc, "/establishment/1/employees");
            result.andExpect(MockMvcResultMatchers.status().isOk());
            JSONObject response = new JSONObject(result.andReturn().getResponse().getContentAsString());
            List<EmployeeDTO> establishments = JSONToDTO.fromPageDTO(response, EmployeeDTO.class);
            establishments.sort(Comparator.comparingLong(EmployeeDTO::getId));
            assert establishments.equals(EmployeeData.employees);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listEstablishmentServices() {
        try {
            testService(true, true);
            ResultActions result = CreateTest.get(mockMvc, "/establishment/1/services");
            result.andExpect(MockMvcResultMatchers.status().isOk());
            JSONObject response = new JSONObject(result.andReturn().getResponse().getContentAsString());
            List<ServiceDTO> serviceDTO = JSONToDTO.fromPageDTO(response, ServiceDTO.class);
            serviceDTO.sort(Comparator.comparingLong(ServiceDTO::getId));
            assert serviceDTO.equals(Arrays.asList(ServiceData.services.get(0), ServiceData.services.get(1)));
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listEstablishmentEmployeeServices() {
        try {
            System.out.println("TODO");
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }
}
