package com.teamsantos.easybarber.tests;

import com.teamsantos.easybarber.DTO.*;
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
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureMockMvc
public class EstablishmentTests {
    private final MockMvc mockMvc;

    @Autowired
    public EstablishmentTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private void create(String path, String jwt, String item) throws Exception {
        CreateTest.createOrFound(mockMvc, path, jwt, item);
    }

    @Test
    public void createEstablishments() {
        createEstablishments(true, true);
    }

    public void createEstablishments(boolean initAuth, boolean initEmployee) {
        try {
            String jwt;
            jwt = new AuthTests(mockMvc).login(initAuth);
            ResultActions result = CreateTest.post(mockMvc, "/establishment", jwt,
                    EstablishmentData.establishments.get(0).toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
            for (Long employeeId : EmployeeData.employeesEstablishments.keySet()) {
                jwt = new EmployeeTests(mockMvc).loginById(employeeId, initEmployee);
                for (Long establishmentId : EmployeeData.employeesEstablishments.get(employeeId)) {
                    create("/establishment", jwt, EstablishmentData.establishments.stream()
                            .filter(e -> e.getId().equals(establishmentId)).findFirst()
                            .orElseThrow(NotFoundException::new).toString());
                }
                initEmployee = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testEmployees() {
        testEmployees(true, true);
    }

    public void testEmployees(boolean initAuth, boolean initEmployee) {
        try {
            createEstablishments(initAuth, initEmployee);
            String jwt = new EmployeeTests(mockMvc).login(false);
            Long establishmentId = EstablishmentData.establishments.get(0).getId();
            UserCreateDTO employee = EmployeeData.employees.get(0);
            create(String.format("/establishment/%d/employee/%d", establishmentId, employee.getId()), jwt,
                    employee.toString());
            jwt = new EmployeeTests(mockMvc).login(1, false);
            employee = EmployeeData.employees.get(1);
            ResultActions result = CreateTest.post(mockMvc,
                    String.format("/establishment/%d/employee/%d", establishmentId, employee.getId()), jwt,
                    employee.toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
            jwt = new EmployeeTests(mockMvc).login(false);
            create(String.format("/establishment/%d/employee/%d", establishmentId, employee.getId()), jwt,
                    employee.toString());
            jwt = new EmployeeTests(mockMvc).login(1, false);
            establishmentId = EstablishmentData.establishments.get(1).getId();
            create(String.format("/establishment/%d/employee/%d", establishmentId, employee.getId()), jwt,
                    employee.toString());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void testService() {
        testService(true, true);
    }

    public void testService(boolean initAuth, boolean initEmployee) {
        try {
            testEmployees(initAuth, initEmployee);
            new EmployeeTests(mockMvc).createServices(false);
            String jwt = new EmployeeTests(mockMvc).login(false);
            CreateEstablishmentServiceDTO service = EstablishmentData.establishmentServices.get(0);
            create(String.format("/establishment/%d/service", service.getEstablishmentId()), jwt, service.toString());
            service = EstablishmentData.establishmentServices.get(1);
            create(String.format("/establishment/%d/service", service.getEstablishmentId()), jwt, service.toString());
            jwt = new EmployeeTests(mockMvc).login(1, false);
            service = EstablishmentData.establishmentServices.get(2);
            ResultActions result = CreateTest.post(mockMvc, "/establishment/1/service", jwt,
                    EstablishmentData.establishmentServices.get(2).toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
            create(String.format("/establishment/%d/service", service.getEstablishmentId()), jwt, service.toString());
            service = EstablishmentData.establishmentServices.get(3);
            jwt = new EmployeeTests(mockMvc).login(false);
            create(String.format("/establishment/%d/service", service.getEstablishmentId()), jwt, service.toString());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listEmployees() {
        try {
            testEmployees(true, true);
            ResultActions result = CreateTest.get(mockMvc, "/establishment/1/employees");
            result.andExpect(MockMvcResultMatchers.status().isOk());
            JSONObject response = new JSONObject(result.andReturn().getResponse().getContentAsString());
            List<EmployeeDTO> establishments = JSONToDTO.fromPageDTO(response, EmployeeDTO.class);
            establishments.sort(Comparator.comparingLong(EmployeeDTO::getId));
            assert establishments.equals(EmployeeData.employees);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    private boolean testEstablishmentServices(Long establishmentId, List<ServiceDTO> _services) {
        _services.sort(Comparator.comparingLong(ServiceDTO::getId));
        List<Long> ids = EstablishmentData.establishmentServices.stream()
                .filter(e -> e.getEstablishmentId().equals(establishmentId)).map(e -> e.getServiceId()).toList();
        List<ServiceDTO> services = ServiceData.services.stream().filter(e -> ids.contains(e.getId()))
                .toList();
        boolean test = _services.equals(services);
        if (!test) {
            services.forEach(e -> {
                ServiceData.serviceUpdate.stream()
                        .filter(el -> el.getId().equals(e.getId()))
                        .findFirst().ifPresent(matchingData -> e.setDescription(matchingData.getDescription()));
            });
            return false;
        }
        return true;
    }

    @Test
    public void listEstablishmentServices() {
        try {
            testService(true, true);
            ResultActions result = CreateTest.get(mockMvc, "/establishment/1/services");
            result.andExpect(MockMvcResultMatchers.status().isOk());
            JSONObject response = new JSONObject(result.andReturn().getResponse().getContentAsString());
            List<ServiceDTO> serviceDTO = JSONToDTO.fromPageDTO(response, ServiceDTO.class);
            assert serviceDTO != null;
            serviceDTO.sort(Comparator.comparingLong(ServiceDTO::getId));
            assert !testEstablishmentServices(1L, serviceDTO);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listClosestEstablishments() {
        try {
            createEstablishments(true, true);
            float latitude = 38.671870f,
                    longitude = -9.165940f;
            ResultActions result = CreateTest.get(mockMvc,
                    String.format(Locale.US, "/establishment/list?latitude=%f&longitude=%f", latitude, longitude));
            result.andExpect(MockMvcResultMatchers.status().isOk());
            List<BaseEstablishmentDTO> establishments = JSONToDTO.fromPageDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), BaseEstablishmentDTO.class);
            assert establishments != null && establishments.equals(
                    Arrays.asList(EstablishmentData.establishments.get(1), EstablishmentData.establishments.get(0)));
            latitude = 38.622584f;
            longitude = -9.208970f;
            result = CreateTest.get(mockMvc,
                    String.format(Locale.US, "/establishment/list?latitude=%f&longitude=%f", latitude, longitude));
            result.andExpect(MockMvcResultMatchers.status().isOk());
            establishments = JSONToDTO.fromPageDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), BaseEstablishmentDTO.class);
            assert establishments != null && establishments.equals(EstablishmentData.establishments);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listEstablishmentsByServiceType() {
        try {
            testService(true, true);
            long serviceTypeId = 3L;
            ResultActions result = CreateTest.get(mockMvc,
                    String.format("/establishment/list?serviceType=%d", serviceTypeId));
            result.andExpect(MockMvcResultMatchers.status().isOk());
            List<BaseEstablishmentDTO> establishments = JSONToDTO.fromPageDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), BaseEstablishmentDTO.class);
            Set<Long> serviceIds = ServiceTypeTests.getServicesIdByServiceType(serviceTypeId);
            Set<Long> establishmentIds = EstablishmentData.establishmentServices.stream()
                    .filter(e -> serviceIds.contains(e.getServiceId())).map(es -> es.getEstablishmentId())
                    .collect(Collectors.toSet());
            List<BaseEstablishmentDTO> _establishments = EstablishmentData.establishments.stream()
                    .filter(e -> establishmentIds.contains(e.getId()))
                    .toList();
            assert establishments != null && establishments.equals(_establishments);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    public String loginAdminByEstablishmentId(Long id) throws Exception {
        for (Long employeeId : EmployeeData.employeesEstablishments.keySet()) {
            if (EmployeeData.employeesEstablishments.get(employeeId).contains(id)) {
                return new EmployeeTests(mockMvc).loginById(employeeId, false);
            }
        }
        return null;
    }

    @Test
    public void addImages() {
        addImages(true, true);
    }

    public void addImages(boolean initAuth, boolean initEmployee) {
        try {
            createEstablishments(initAuth, initEmployee);
            for (Long establishmentId : EmployeeData.employeesEstablishments.values().stream()
                    .flatMap(Collection::stream)
                    .toList()) {
                String jwt = loginAdminByEstablishmentId(establishmentId);
                List<ImageDTO> images = EstablishmentData.establishmentImages.get(establishmentId);
                new ImageUtils(mockMvc, String.format("/establishment/%d", establishmentId)).saveImages(images, jwt);
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }
}
