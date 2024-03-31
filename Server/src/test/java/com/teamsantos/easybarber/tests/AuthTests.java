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

import com.teamsantos.easybarber.testData.UsersData;

@SpringBootTest
@AutoConfigureMockMvc
class AuthTests {
    private final MockMvc mockMvc;

    @Autowired
    public AuthTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testAuth() {
        try {
            System.out.println("Running AuthTests");
            ResultActions registerResult = mockMvc.perform(MockMvcRequestBuilders
                    .post("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(UsersData.users.get(0).toString()));
            registerResult
                .andExpect(MockMvcResultMatchers.status().isCreated());
            
            ResultActions loginResult = mockMvc.perform(MockMvcRequestBuilders
                    .post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(UsersData.users.get(0).toString()));
            loginResult
                .andExpect(MockMvcResultMatchers.status().isOk());
            System.out.println("Finished AuthTests");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
