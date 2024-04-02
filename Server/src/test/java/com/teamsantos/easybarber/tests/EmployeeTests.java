package com.teamsantos.easybarber.tests;

import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.testData.ServiceData;
import com.teamsantos.easybarber.utils.CreateTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeTests {
    private final MockMvc mockMvc;

    @Autowired
    public EmployeeTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public String login(boolean init) throws Exception {
        if (init)
            test();
        return new AuthTests(mockMvc).login(EmployeeData.employees.get(0).toString());
    }

    public String login(int index, boolean init) throws Exception {
        if (init)
            test();
        return new AuthTests(mockMvc).login(EmployeeData.employees.get(index).toString());
    }

    public String login() throws Exception {
        return login(true);
    }

    public String login(int index) throws Exception {
        return login(index, true);
    }

    private void create(String path, String jwt, String item) throws Exception {
        CreateTest.createOrFound(mockMvc, path, jwt, item);
    }

    private void create(String path, String item) throws Exception {
        CreateTest.createOrFound(mockMvc, path, item);
    }

    @Test
    public void test() {
        try {
            create("/employee", EmployeeData.employees.get(0).toString());
            create("/register", EmployeeData.employees.get(1).toString());
            create("/employee", EmployeeData.employees.get(1).toString());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testServices() {
        testServices(true);
    }

    public void testServices(boolean init) {
        try {
            new ServiceTests(mockMvc).test(init);
            String jwt = login(init);
            create("/employee/service", jwt, ServiceData.services.get(0).toString());
            create("/employee/service", jwt, ServiceData.services.get(1).toString());
            jwt = login(1, false);
            create("/employee/service", jwt, ServiceData.services.get(2).toString());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }
}
