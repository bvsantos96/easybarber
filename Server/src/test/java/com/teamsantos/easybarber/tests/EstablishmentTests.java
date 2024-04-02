package com.teamsantos.easybarber.tests;

import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.testData.EstablishmentData;
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
public class EstablishmentTests {
    private final MockMvc mockMvc;
    public static boolean created = false;

    @Autowired
    public EstablishmentTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private void create(String path, String jwt, String item) throws Exception {
        if (!EstablishmentTests.created)
            CreateTest.create(mockMvc, path, jwt, item);
        else
            CreateTest.createOrFound(mockMvc, path, jwt, item);
    }

    @Test
    public void test() {
        try {
            String jwt;
            if (!EstablishmentTests.created) {
                jwt = new AuthTests(mockMvc).loginUser();
                ResultActions result = CreateTest.post(mockMvc, "/establishment", jwt,
                        EstablishmentData.establishments.get(0).toString());
                result.andExpect(MockMvcResultMatchers.status().isForbidden());
            }
            jwt = new EmployeeTests(mockMvc).loginUser();
            CreateTest.create(mockMvc, "/establishment", jwt, EstablishmentData.establishments.get(0).toString());
            jwt = new AuthTests(mockMvc).loginUser();
            ResultActions result = CreateTest.post(mockMvc, "/establishment", jwt,
                    EstablishmentData.establishments.get(1).toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
            jwt = new EmployeeTests(mockMvc).loginUser();
            CreateTest.create(mockMvc, "/establishment", jwt, EstablishmentData.establishments.get(1).toString());
            if (!EstablishmentTests.created)
                EstablishmentTests.created = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmployees() {
        boolean created = EstablishmentTests.created;
        try {
            test();
            created = EstablishmentTests.created;
            new EmployeeTests(mockMvc).test();
            EstablishmentTests.created = false;
            String jwt = new EmployeeTests(mockMvc).loginUser();
            create("/establishment/1/employee/0", jwt, EmployeeData.employees.get(0).toString());
            create("/establishment/2/employee/0", jwt, EmployeeData.employees.get(0).toString());
            jwt = new EmployeeTests(mockMvc).loginUser(1);
            ResultActions result = CreateTest.post(mockMvc, "/establishment/2/employee/1", jwt,
                    EmployeeData.employees.get(1).toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
            jwt = new EmployeeTests(mockMvc).loginUser();
            create("/establishment/2/employee/1", jwt, EmployeeData.employees.get(0).toString());
            EstablishmentTests.created = created;
        } catch (Exception e) {
            e.printStackTrace();
        }
        EstablishmentTests.created = created;
    }

    @Test
    public void testService() {
        boolean created = EstablishmentTests.created;
        try {
            testEmployees();
            created = EstablishmentTests.created;
            new EmployeeTests(mockMvc).testServices();
            EstablishmentTests.created = false;
            String jwt = new EmployeeTests(mockMvc).loginUser();
            create("/establishment/1/service/0", jwt, ServiceData.services.get(0).toString());
            create("/establishment/1/service/1", jwt, ServiceData.services.get(1).toString());
            jwt = new EmployeeTests(mockMvc).loginUser(1);
            ResultActions result = CreateTest.post(mockMvc, "/establishment/1/service/2", jwt,
                    ServiceData.services.get(2).toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
            jwt = new EmployeeTests(mockMvc).loginUser();
            create("/establishment/1/service/2", jwt, ServiceData.services.get(2).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        EstablishmentTests.created = created;
    }
}
