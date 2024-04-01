package com.teamsantos.easybarber.tests;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.testData.ServiceData;
import com.teamsantos.easybarber.utils.CreateTest;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeTests {
    private final MockMvc mockMvc;
    private static boolean created = false;

    @Autowired
    public EmployeeTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public String loginUser() throws Exception {
        if (!created)
            test();
        AuthTests authTests = new AuthTests(mockMvc);
        return authTests.loginUser(EmployeeData.employees.get(0).toString());
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
            create("/employee", EmployeeData.employees.get(0).toString());
            create("/register", EmployeeData.employees.get(1).toString());
            create("/employee", EmployeeData.employees.get(1).toString());
            created = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testServices() {
        try {
            new ServiceTests(mockMvc).test();
            created = false;
            create("/employee/service", ServiceData.services.get(0).toString());
            create("/employee/service", ServiceData.services.get(1).toString());
            create("/employee/service", ServiceData.services.get(2).toString());
            created = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
