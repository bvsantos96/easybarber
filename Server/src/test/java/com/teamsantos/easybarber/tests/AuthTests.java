package com.teamsantos.easybarber.tests;

import com.teamsantos.easybarber.testData.UsersData;
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
public class AuthTests {
    private final MockMvc mockMvc;
    public static boolean created = false;

    @Autowired
    public AuthTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public String login(boolean init) throws Exception {
        if (init)
            test();
        return login(UsersData.usersDTO.get(0).toString());
    }

    public String login(String user, boolean init) throws Exception {
        if (init)
            test();
        ResultActions result = CreateTest.post(mockMvc, "/login", user);
        result
                .andExpect(MockMvcResultMatchers.status().isOk());
        return result.andReturn().getResponse().getContentAsString();
    }

    public String login() throws Exception {
        return login(true);
    }

    public String login(String user) throws Exception {
        return login(user, true);
    }

    private void create(String path, String item) throws Exception {
        CreateTest.createOrFound(mockMvc, path, item);
    }

    @Test
    public void test() {
        try {
            create("/register", UsersData.usersDTO.get(0).toString());
            create("/register", UsersData.usersDTO.get(1).toString());
            login(UsersData.usersDTO.get(1).toString(), false);
            if (!AuthTests.created)
                AuthTests.created = true;
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }
}
