package com.teamsantos.easybarber.tests;

import com.teamsantos.easybarber.testData.ServiceData;
import com.teamsantos.easybarber.utils.CreateTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ServiceTests {
    private final MockMvc mockMvc;
    public static boolean created = false;

    @Autowired
    public ServiceTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private void create(String path, String jwt, String item) throws Exception {
        if (!ServiceTests.created)
            CreateTest.create(mockMvc, path, jwt, item);
        else
            CreateTest.createOrFound(mockMvc, path, jwt, item);
    }

    @Test
    public void test() {
        try {
            String jwt = new EmployeeTests(mockMvc).loginUserSafe();
            create("/service", jwt, ServiceData.serviceTypes.get(0).toString());
            create("/service", jwt, ServiceData.serviceTypes.get(1).toString());
            create("/service", jwt, ServiceData.serviceTypes.get(2).toString());
            if (!ServiceTests.created)
                ServiceTests.created = true;
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }
}
