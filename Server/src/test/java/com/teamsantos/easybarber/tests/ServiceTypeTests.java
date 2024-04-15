package com.teamsantos.easybarber.tests;

import com.teamsantos.easybarber.DTO.ServiceTypeDTO;
import com.teamsantos.easybarber.testData.ServiceData;
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
public class ServiceTypeTests {
    private final MockMvc mockMvc;

    @Autowired
    public ServiceTypeTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private void create(String path, String jwt, String item) throws Exception {
        CreateTest.createOrFound(mockMvc, path, jwt, item);
    }

    @Test
    public void createServiceTypes() {
        createServiceTypes(true);
    }

    public void createServiceTypes(boolean init) {
        try {
            String jwt = new EmployeeTests(mockMvc).login(init);
            create("/service", jwt, ServiceData.serviceTypes.get(0).toString());
            create("/service", jwt, ServiceData.serviceTypes.get(1).toString());
            create("/service", jwt, ServiceData.serviceTypes.get(2).toString());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }
}
