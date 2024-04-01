package com.teamsantos.easybarber.tests;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.utils.CreateTest;

@SpringBootTest
@AutoConfigureMockMvc
public class UserTests {
    private final MockMvc mockMvc;

    @Autowired
    public UserTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void test() {
        try {
            AuthTests authTests = new AuthTests(mockMvc);
            ResultActions result = CreateTest.post(mockMvc, "/user", authTests.loginUser(),
                    "{\"name\":\"Filipe Miguel Pinho Santos\"}");
            result
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
