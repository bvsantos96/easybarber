package com.teamsantos.easybarber.tests;

import com.teamsantos.easybarber.utils.CreateTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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
    public void test() {
        try {
            ResultActions result = CreateTest.put(mockMvc, "/user", new AuthTests(mockMvc).loginUser(),
                    "{\"name\":\"Bruno Vicente dos Santos\"}");
            result
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
