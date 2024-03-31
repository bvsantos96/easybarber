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

@SpringBootTest
@AutoConfigureMockMvc
public class UserTests {
    private final MockMvc mockMvc;

    @Autowired
    public UserTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testUser() {
        try {
            System.out.println("Running UserTests");
            AuthTests authTests = new AuthTests(mockMvc);
            ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                    .put("/user")
                    .header("Authorization", "Bearer " + authTests.testAuth())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"Filipe Miguel Pinho Santos\"}"));
            result
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
