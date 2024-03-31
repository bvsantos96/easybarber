package com.teamsantos.easybarber.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.utils.AnyOfStatusMatcher;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeTests {
    private final MockMvc mockMvc;
    private final AuthTests authTests;

    @Autowired
    public EmployeeTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.authTests = new AuthTests(mockMvc);
    }

    private void _createEmployee(String employee) throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employee));
        result
                .andExpect(MockMvcResultMatchers.status()
                        .isCreated());
    }

    private void createEmployee(String employee) throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employee));
        result
                .andExpect(MockMvcResultMatchers.status()
                        .is(AnyOfStatusMatcher.createdOrFound()));
    }

    @Test
    public void _testEmployee() {
        try {
            _createEmployee(EmployeeData.employees.get(0).toString());
            authTests.registerUser(EmployeeData.employees.get(1).toString());
            _createEmployee(EmployeeData.employees.get(1).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testEmployee() {
        try {
            createEmployee(EmployeeData.employees.get(0).toString());
            authTests.registerUser(EmployeeData.employees.get(1).toString());
            createEmployee(EmployeeData.employees.get(1).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
