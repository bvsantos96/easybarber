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
import com.teamsantos.easybarber.testDTOs.UserTestDTO;
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
        return login(UsersData.usersDTO.get(0), false);
    }

    public String loginById(Long id, boolean init) throws Exception {
        if (init)
            test();
        for (UserTestDTO user : UsersData.usersDTO) {
            if (user.getId().equals(id)) {
                return login(user, false);
            }
        }
        throw new IllegalArgumentException("User not found");
    }

    public String login(UserTestDTO user, boolean init) throws Exception {
        if (init)
            test();
        if (user.getJwt() != null)
            return user.getJwt();
        ResultActions result = CreateTest.post(mockMvc, "/login", user.toString());
        result
                .andExpect(MockMvcResultMatchers.status().isOk());
        user.setJwt(result.andReturn().getResponse().getContentAsString());
        return user.getJwt();
    }

    public String login() throws Exception {
        return login(true);
    }

    public String login(UserTestDTO user) throws Exception {
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
        if (TestsState.ran(TestsState.AUTH_TEST)) {
            return;
        }
        TestsState.mark(TestsState.AUTH_TEST);
        try {
            for (UserTestDTO user : UsersData.usersDTO) {
                user.setId(create("/register", user.toString()));
            }
            login(UsersData.usersDTO.get(1), false);
            if (!AuthTests.created)
                AuthTests.created = true;
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    public String loginAdmin() {
        if (TestsState.ran(TestsState.AUTH_CREATE_SYSTEM_ADMIN)) {
            return TestsState.SYSTEM_ADMIN_JWT;

        }
        TestsState.mark(TestsState.AUTH_CREATE_SYSTEM_ADMIN);
        try {
            UsersData.systemAdmin.setId(create("/registerAdmin", UsersData.systemAdmin.toString()));
            TestsState.SYSTEM_ADMIN_JWT = login(UsersData.systemAdmin, false);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
        return TestsState.SYSTEM_ADMIN_JWT;
    }
}
