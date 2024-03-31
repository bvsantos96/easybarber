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
import com.teamsantos.easybarber.utils.AnyOfStatusMatcher;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthTests {
    private final MockMvc mockMvc;

    @Autowired
    public AuthTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private void _registerUser(String user) throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user));
        result
                .andExpect(MockMvcResultMatchers.status()
                        .isCreated());
    }

    public void registerUser(String user) throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user));
        result
                .andExpect(MockMvcResultMatchers.status()
                        .is(AnyOfStatusMatcher.createdOrFound()));
    }

    public String loginUser(String user) throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(user));
        result
                .andExpect(MockMvcResultMatchers.status().isOk());
        return result.andReturn().getResponse().getContentAsString();
    }

    @Test
    public void test() {
        try {
            _registerUser(UsersData.users.get(0).toString());
            _registerUser(UsersData.users.get(1).toString());
            loginUser(UsersData.users.get(1).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String testAuth() {
        try {
            registerUser(UsersData.users.get(0).toString());
            registerUser(UsersData.users.get(1).toString());
            return loginUser(UsersData.users.get(1).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
