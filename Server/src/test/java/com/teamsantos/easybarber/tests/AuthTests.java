package com.teamsantos.easybarber.tests;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.testData.UsersData;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.JSONToDTO;
import com.teamsantos.easybarber.utils.TestsState;

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
        return login(UsersData.usersDTO.get(0).toString(), false);
    }

    public String loginById(Long id, boolean init) throws Exception {
        if (init)
            test();
        for (UserCreateDTO user : UsersData.usersDTO) {
            if (user.getId().equals(id)) {
                return login(user.toString(), false);
            }
        }
        throw new IllegalArgumentException("User not found");
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

    private long create(String path, String item) throws Exception {
        ResultActions result = CreateTest.createOrFoundWithResult(mockMvc, path, item);
        BaseResponseDTO response = JSONToDTO.toDTO(
                new JSONObject(result.andReturn().getResponse().getContentAsString()),
                BaseResponseDTO.class);
        return response.getId();
    }

    @Test
    public void test() {
        if (TestsState.ran("test")) {
            return;
        }
        TestsState.mark("test");
        try {
            UsersData.usersDTO.get(0).setId(create("/register", UsersData.usersDTO.get(0).toString()));
            UsersData.usersDTO.get(1).setId(create("/register", UsersData.usersDTO.get(1).toString()));
            login(UsersData.usersDTO.get(1).toString(), false);
            if (!AuthTests.created)
                AuthTests.created = true;
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }
}
