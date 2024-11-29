package com.teamsantos.easybarber.tests;

import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.DTO.location.LocationDTO;
import com.teamsantos.easybarber.DTO.user.UserDTO;
import com.teamsantos.easybarber.DTO.user.UsersDTO;
import com.teamsantos.easybarber.services.UserTypeService;
import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.testData.UsersData;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.JSONToDTO;
import com.teamsantos.easybarber.utils.TestsState;

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
        if (TestsState.ran(TestsState.USER_TEST)) {
            return;
        }
        TestsState.mark(TestsState.USER_TEST);
        try {
            String jwt = new AuthTests(mockMvc).login(init);
            ResultActions result = CreateTest.put(mockMvc, "/user", jwt,
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
        testList(true);
    }

    public void testList(boolean init) {
        try {
            ResultActions result = CreateTest.get(mockMvc,
                    String.format("/users?userType=%s", UserTypeService.UserTypes.EMPLOYEE.toString()),
                    new EmployeeTests(mockMvc).login(init));
            String json = result.andReturn().getResponse().getContentAsString();
            UsersDTO response = new UsersDTO();
            response.setUsers(JSONToDTO.fromPageDTO(new JSONObject(json), UserDTO.class));
            if (!response.getUsers().equals(EmployeeData.employees)) {
                org.junit.jupiter.api.Assertions.fail("Response is not equal to expected value.");
            }
            result.andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    public void deleteUser() {
        // TODO: This needs to take into account that we are storing jwt in a static
        // manner in the Testing world
    }

    @Test
    public void addUserLocation() {
        addUserLocation(true);
    }

    public void addUserLocation(boolean init) {
        if (TestsState.ran(TestsState.ADD_USER_LOCATION)) {
            return;
        }
        TestsState.mark(TestsState.ADD_USER_LOCATION);

        try {
            String jwt = new AuthTests(mockMvc).login(init);
            LocationDTO location = UsersData.locations.get(0);
            ResultActions result = CreateTest.post(mockMvc, "/location", jwt,
                    location.toString());
            result.andExpect(MockMvcResultMatchers.status().isOk());
            location.setId(Long.parseLong(result.andReturn().getResponse().getContentAsString()));
            location.setSelected(true);
            result = CreateTest.get(mockMvc, "/locations", jwt);
            result.andExpect(MockMvcResultMatchers.status().isOk());
            List<LocationDTO> locations = JSONToDTO.fromPageDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), LocationDTO.class);
            assert locations.get(0).equals(UsersData.locations.get(0));
            location = UsersData.locations.get(1);
            result = CreateTest.post(mockMvc, "/location", jwt, location.toString());
            result.andExpect(MockMvcResultMatchers.status().isOk());
            location.setId(Long.parseLong(result.andReturn().getResponse().getContentAsString()));
            for (LocationDTO loc : UsersData.locations) {
                loc.setSelected(false);
            }
            location.setSelected(true);
            result = CreateTest.get(mockMvc, "/locations", jwt);
            result.andExpect(MockMvcResultMatchers.status().isOk());
            locations = JSONToDTO.fromPageDTO(new JSONObject(result.andReturn().getResponse().getContentAsString()),
                    LocationDTO.class);
            assert locations.equals(UsersData.locations);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }
}
