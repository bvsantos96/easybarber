package com.teamsantos.easybarber.tests;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.testData.EstablishmentData;
import com.teamsantos.easybarber.testData.ServiceData;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.JSONToDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

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
            createEmployees();
        return new AuthTests(mockMvc).login(EmployeeData.employees.get(0).toString());
    }

    public String loginById(Long id, boolean init) throws Exception {
        if (init)
            createEmployees();
        return new AuthTests(mockMvc)
                .login(EmployeeData.employees.stream().filter(e -> Objects.equals(e.getId(), id)).findFirst()
                        .orElseThrow(UserNotFoundException::new).toString());
    }

    public String login(int index, boolean init) throws Exception {
        if (init)
            createEmployees();
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
    public void createEmployees() {
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
    public void testDelete() {
        // TODO: Need to implement this test after defining and implementing the all
        // processo of deleting a employee
        // - Mark employee as deleted
        // - Mark every EstablishmentStaff as deleted
        // - Alert Establishment Admins that this employee is no longer working
        // - Mark every EstablishmentService as deleted
        // - Mark every Service related to this employee as deleted
        // - Cancel every appointment related to this employee
        // - Alert every client that had an appointment with this employee
    }

    @Test
    public void createService() {
        createServices(true);
    }

    public void createServices(boolean init) {
        try {
            new ServiceTypeTests(mockMvc).createServiceTypes(init);
            String jwt = login(false);
            create("/employee/service", jwt, ServiceData.services.get(0).toString());
            create("/employee/service", jwt, ServiceData.services.get(1).toString());
            jwt = login(1, false);
            create("/employee/service", jwt, ServiceData.services.get(2).toString());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void updateService() {
        try {
            createServices(true);
            String jwt = login(false);
            ResultActions result = CreateTest.put(mockMvc, "/employee/service", jwt,
                    ServiceData.serviceUpdate.get(0).toString());
            result.andExpect(MockMvcResultMatchers.status().isOk());
            result = CreateTest.put(mockMvc, "/employee/service", jwt, ServiceData.serviceUpdate.get(2).toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
            result = CreateTest.put(mockMvc, "/employee/service", jwt, ServiceData.serviceUpdate.get(1).toString());
            result.andExpect(MockMvcResultMatchers.status().isOk());
            jwt = login(1, false);
            result = CreateTest.put(mockMvc, "/employee/service", jwt, ServiceData.serviceUpdate.get(2).toString());
            result.andExpect(MockMvcResultMatchers.status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listServices() {
        try {
            createServices(true);
            String jwt = login(false);
            ResultActions result = CreateTest.get(mockMvc, "/employee/services", jwt);
            result.andExpect(MockMvcResultMatchers.status().isOk());
            JSONObject response = new JSONObject(result.andReturn().getResponse().getContentAsString());
            List<ServiceDTO> services = JSONToDTO.fromPageDTO(response, ServiceDTO.class);
            List<ServiceDTO> servicesDTO = Arrays.asList(ServiceData.services.get(0), ServiceData.services.get(1));
            assert services != null;
            for (int i = 0; i < services.size(); i++) {
                assert services.get(i).equalsWithoutPrice(servicesDTO.get(i));
            }
            result = CreateTest.get(mockMvc,
                    String.format("/employee/%d/services", EmployeeData.employees.get(1).getId()), jwt);
            result.andExpect(MockMvcResultMatchers.status().isOk());
            services = JSONToDTO.fromPageDTO(new JSONObject(result.andReturn().getResponse().getContentAsString()),
                    ServiceDTO.class);
            servicesDTO = Collections.singletonList(ServiceData.services.get(2));
            assert services != null;
            for (int i = 0; i < services.size(); i++) {
                assert services.get(i).equalsWithoutPrice(servicesDTO.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listEstablishments() {
        try {
            new EstablishmentTests(mockMvc).testEmployees(true, true);
            String jwt = login(1, false);
            ResultActions result = CreateTest.get(mockMvc, "/employee/establishments", jwt);
            JSONObject response = new JSONObject(result.andReturn().getResponse().getContentAsString());
            List<BaseEstablishmentDTO> establishments = JSONToDTO.fromPageDTO(response, BaseEstablishmentDTO.class);
            assert establishments != null;
            establishments.sort(Comparator.comparingLong(BaseEstablishmentDTO::getId));
            List<BaseEstablishmentDTO> establishmentsDTO = Arrays.asList(EstablishmentData.establishments.get(0),
                    EstablishmentData.establishments.get(1));
            establishmentsDTO.sort(Comparator.comparingLong(BaseEstablishmentDTO::getId));
            assert establishments.equals(establishmentsDTO);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void addImages() {
        addImages(true, true);
    }

    public void addImages(boolean initAuth, boolean initEmployee) {
        try {
            for (Long employeeId : EmployeeData.employees.stream().map(UserCreateDTO::getId).toList()) {
                String jwt = loginById(employeeId, initAuth);
                List<ImageDTO> images = EmployeeData.employeeImages.get(employeeId);
                addImageAndCheckIfSaved(images, jwt, employeeId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void deleteImages() {
        deleteImages(true, true);
    }

    public void deleteImages(boolean initAuth, boolean initEmployee) {
        try {
            addImages(initAuth, initEmployee);
            for (Long employeeId : EmployeeData.employees.stream().map(UserCreateDTO::getId).toList()) {
                String jwt = loginById(employeeId, initAuth);
                List<ImageDTO> images = EmployeeData.employeeImages.get(employeeId).subList(0, 1);
                addImageAndCheckIfSaved(images, jwt, employeeId);
            }
            // This is meant to reset the images to the original state for following tests
            addImages(false, false);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    private void addImageAndCheckIfSaved(List<ImageDTO> images, String jwt, Long establishmentId) {
        if (images == null || images.isEmpty()) {
            return;
        }
        ImageUtils imageUtils = new ImageUtils(mockMvc, String.format("/employee/%d", establishmentId));
        imageUtils.saveImages(images, jwt);
        List<ImageDTO> _images = imageUtils.getImages(jwt);
        assert _images.equals(images);

    }
}
