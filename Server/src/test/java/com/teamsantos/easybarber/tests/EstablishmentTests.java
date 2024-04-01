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
import com.teamsantos.easybarber.testData.EstablishmentData;
import com.teamsantos.easybarber.testData.UsersData;
import com.teamsantos.easybarber.utils.AnyOfStatusMatcher;

@SpringBootTest
@AutoConfigureMockMvc
public class EstablishmentTests {
    private final MockMvc mockMvc;
    private final AuthTests authTests;
    private final EmployeeTests employeeTests;

    @Autowired
    public EstablishmentTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.authTests = new AuthTests(mockMvc);
        this.employeeTests = new EmployeeTests(mockMvc);
    }

    private ResultActions _createEstablishment(String jwt, String establishment) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .post("/establishment")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(establishment));
    }

    private void createEstablishment(String jwt, String establishment) throws Exception {
        _createEstablishment(jwt, establishment)
                .andExpect(MockMvcResultMatchers.status().is(AnyOfStatusMatcher.createdOrFound()));
    }

    @Test
    public void testEstablishment() {
        try {
            employeeTests.testEmployee();
            String jwt = authTests.loginUser(UsersData.users.get(0).toString());
            ResultActions result = _createEstablishment(jwt, EstablishmentData.establishments.get(0).toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
            jwt = authTests.loginUser(EmployeeData.employees.get(0).toString());
            createEstablishment(jwt, EstablishmentData.establishments.get(0).toString());
            createEstablishment(jwt, EstablishmentData.establishments.get(1).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
