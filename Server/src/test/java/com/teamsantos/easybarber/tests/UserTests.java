package com.teamsantos.easybarber.tests;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.DTO.UserDTO;
import com.teamsantos.easybarber.DTO.UsersDTO;
import com.teamsantos.easybarber.testData.UsersData;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.JSONToDTO;

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
        test(true);
    }

    public void test(boolean init) {
        try {
            ResultActions result = CreateTest.put(mockMvc, "/user", new AuthTests(mockMvc).login(init),
                    UsersData.usersUpdateDTO.get(0).toString());
            result
                    .andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testList() {
        try {
            ResultActions result = CreateTest.get(mockMvc, "/users?userType=CLIENT",
                    new AuthTests(mockMvc).login(false));
            String json = result.andReturn().getResponse().getContentAsString();
            UsersDTO response = new UsersDTO();
            response.setUsers(JSONToDTO.fromPageDTO(new JSONObject(json), UserDTO.class));
            if (!response.getUsers().equals(UsersData.usersDTO)) {
                org.junit.jupiter.api.Assertions.fail("Response is not equal to expected value.");
            }
            result.andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }
}
