package com.teamsantos.easybarber.tests;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.testData.EstablishmentData;
import com.teamsantos.easybarber.testData.UsersData;
import com.teamsantos.easybarber.utils.AnyOfStatusMatcher;
import com.teamsantos.easybarber.utils.CreateTest;

@SpringBootTest
@AutoConfigureMockMvc
public class EstablishmentTests {
    private final MockMvc mockMvc;
    public static boolean created = false;

    @Autowired
    public EstablishmentTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private void create(String path, String jwt, String item) throws Exception {
        if (!created)
            CreateTest.create(mockMvc, path, jwt, item);
        else
            CreateTest.createOrFound(mockMvc, path, jwt, item);
    }

    @Test
    public void test() {
        try {
            String jwt;
            if (!created) {
                created = true;
                jwt = new AuthTests(mockMvc).loginUser();
                ResultActions result = CreateTest.post(mockMvc, "/establishment", jwt,
                        EstablishmentData.establishments.get(0).toString());
                result.andExpect(MockMvcResultMatchers.status().isForbidden());
            }
            jwt = new EmployeeTests(mockMvc).loginUser();
            CreateTest.create(mockMvc, "/establishment", jwt, EstablishmentData.establishments.get(0).toString());
            CreateTest.create(mockMvc, "/establishment", jwt, EstablishmentData.establishments.get(1).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
