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

    @Autowired
    public EmployeeTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testEmployee() {
        try {
            ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                    .post("/employee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(EmployeeData.employees.get(0).toString()));
            result
                .andExpect(MockMvcResultMatchers.status()
                    .is(AnyOfStatusMatcher.createdOrFound()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
