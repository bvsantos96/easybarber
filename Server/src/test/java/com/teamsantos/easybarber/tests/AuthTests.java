package com.teamsantos.easybarber.tests;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.testData.UsersData;
import com.teamsantos.easybarber.utils.CreateTest;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthTests {
    private final MockMvc mockMvc;
    public static boolean created = false;

    @Autowired
    public AuthTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public String loginUser() throws Exception {
        if(!created)
            test();
        return loginUser(UsersData.users.get(0).toString());
    }

    public String loginUser(String user) throws Exception {
        ResultActions result = CreateTest.post(mockMvc, "/login", user);
        result
                .andExpect(MockMvcResultMatchers.status().isOk());
        return result.andReturn().getResponse().getContentAsString();
    }

    private void create(String path, String item) throws Exception {
        if (!created)
            CreateTest.create(mockMvc, path, item);
        else
            CreateTest.createOrFound(mockMvc, path, item);
    }

    @Test
    public void test() {
        try {
            create("/register", UsersData.users.get(0).toString());
            create("/register", UsersData.users.get(1).toString());
            loginUser(UsersData.users.get(1).toString());
            if (!created)
                created = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
