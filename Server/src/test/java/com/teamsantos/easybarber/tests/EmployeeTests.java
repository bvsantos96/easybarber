package com.teamsantos.easybarber.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.DTO.establishment.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.image.ImageDTO;
import com.teamsantos.easybarber.DTO.service.ServiceBaseDTO;
import com.teamsantos.easybarber.DTO.service.ServiceDTO;
import com.teamsantos.easybarber.DTO.user.UserCreateDTO;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.testData.EstablishmentData;
import com.teamsantos.easybarber.testData.ServiceData;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.JSONToDTO;
import com.teamsantos.easybarber.utils.TestsState;

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
        return new AuthTests(mockMvc).login(EmployeeData.employees.get(0), false);
    }

    public String loginById(Long id, boolean init) throws Exception {
        if (init)
            createEmployees();
        return new AuthTests(mockMvc)
                .login(EmployeeData.employees.stream().filter(e -> Objects.equals(e.getId(), id)).findFirst()
                        .orElseThrow(UserNotFoundException::new), false);
    }

    public String login(int index, boolean init) throws Exception {
        if (init)
            createEmployees();
        return new AuthTests(mockMvc).login(EmployeeData.employees.get(index), false);
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
        if (TestsState.ran(TestsState.EMPLOYEE_CREATE_EMPLOYEES)) {
            return;
        }
        TestsState.mark(TestsState.EMPLOYEE_CREATE_EMPLOYEES);
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
        if (TestsState.ran(TestsState.EMPLOYEE_TEST_DELETE)) {
            return;
        }
        TestsState.mark(TestsState.EMPLOYEE_TEST_DELETE);
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
        if (TestsState.ran(TestsState.EMPLOYEE_CREATE_SERVICES)) {
            return;
        }
        TestsState.mark(TestsState.EMPLOYEE_CREATE_SERVICES);
        try {
            new ServiceTypeTests(mockMvc).createServiceTypes();
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

    @Test
    public void updateService() {
        if (TestsState.ran(TestsState.EMPLOYEE_UPDATE_SERVICE)) {
            return;
        }
        TestsState.mark(TestsState.EMPLOYEE_UPDATE_SERVICE);
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
            for (ServiceDTO service : ServiceData.services) {
                jwt = loginServiceAdmin(service.getId(), false);
                result = CreateTest.put(mockMvc, String.format("/employee/service", service.getId()), jwt,
                        service.toString());
                result.andExpect(MockMvcResultMatchers.status().isOk());
            }
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
            List<ServiceBaseDTO> services = JSONToDTO.fromPageDTO(response, ServiceBaseDTO.class);
            List<ServiceDTO> servicesDTO = Arrays.asList(ServiceData.services.get(0), ServiceData.services.get(1));
            assert services != null;
            for (int i = 0; i < services.size(); i++) {
                assert services.get(i).equalsWithoutPrice(servicesDTO.get(i));
            }
            result = CreateTest.get(mockMvc,
                    String.format("/employee/%d/services", EmployeeData.employees.get(1).getId()), jwt);
            result.andExpect(MockMvcResultMatchers.status().isOk());
            services = JSONToDTO.fromPageDTO(new JSONObject(result.andReturn().getResponse().getContentAsString()),
                    ServiceBaseDTO.class);
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
            Long employeeId = EmployeeData.employees.get(0).getId();
            String jwt = loginById(employeeId, false);
            ResultActions result = CreateTest.get(mockMvc, "/employee/establishments", jwt);
            JSONObject response = new JSONObject(result.andReturn().getResponse().getContentAsString());
            List<BaseEstablishmentDTO> establishments = JSONToDTO.fromPageDTO(response, BaseEstablishmentDTO.class);
            assert establishments != null;
            establishments.sort(Comparator.comparingLong(BaseEstablishmentDTO::getId));
            List<BaseEstablishmentDTO> establishmentsDTO = new ArrayList<>(EstablishmentData.establishments.stream()
                    .filter(e -> EmployeeData.employeesEstablishments.get(employeeId).contains(e.getId())).toList());
            establishmentsDTO.sort(Comparator.comparingLong(BaseEstablishmentDTO::getId));
            assert establishments.equals(establishmentsDTO);
            assert false == true;
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
                ImageUtils imageUtils = new ImageUtils(mockMvc, String.format("/employee/%d", employeeId));
                imageUtils.addImageAndCheckIfSaved(images, jwt);
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
        if (TestsState.ran(TestsState.EMPLOYEE_DELETE_IMAGES)) {
            return;
        }
        TestsState.mark(TestsState.EMPLOYEE_DELETE_IMAGES);
        try {
            addImages(initAuth, initEmployee);
            long employeeId = EmployeeData.employees.get(0).getId();
            String jwt = loginById(employeeId, false);
            ImageUtils imageUtils = new ImageUtils(mockMvc, String.format("/employee/%d", employeeId));
            imageUtils.deleteImagesCheckAndReset(EmployeeData.employeeImages.get(employeeId), jwt);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void addServiceImages() {
        addServiceImages(true);
    }

    public void addServiceImages(boolean init) {
        if (TestsState.ran(TestsState.EMPLOYEE_ADD_SERVICE_IMAGES)) {
            return;
        }
        TestsState.mark(TestsState.EMPLOYEE_ADD_SERVICE_IMAGES);
        try {
            createServices(init);
            for (Long serviceId : ServiceData.services.stream().map(ServiceDTO::getId).toList()) {
                String jwt = loginServiceAdmin(serviceId, false);
                List<ImageDTO> images = ServiceData.serviceImages.get(serviceId);
                ImageUtils imageUtils = new ImageUtils(mockMvc, String.format("/service/%d", serviceId));
                imageUtils.addImageAndCheckIfSavedOneByOne(images, jwt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    public void deleteEmployee() {
        // TODO: This needs to take into account that we are storing jwt in a static
        // manner in the Testing world
    }

    private String loginServiceAdmin(Long id, boolean initEmployee) throws Exception {
        for (ServiceDTO service : ServiceData.services) {
            if (service.getId().equals(id)) {
                return new EmployeeTests(mockMvc).loginById(service.getEmployeeId(), initEmployee);
            }
        }
        return null;
    }

    public static long getDifferentEmployee(long id) {
        for (UserCreateDTO employee : EmployeeData.employees) {
            if (!employee.getId().equals(id)) {
                return employee.getId();
            }
        }
        return 0;
    }
}
