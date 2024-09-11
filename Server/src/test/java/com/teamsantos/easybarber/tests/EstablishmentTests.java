package com.teamsantos.easybarber.tests;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.CreateEstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.EmployeeDTO;
import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.testDTOs.UserTestDTO;
import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.testData.EstablishmentData;
import com.teamsantos.easybarber.testData.ServiceData;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.JSONToDTO;
import com.teamsantos.easybarber.utils.TestsState;

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
        if (TestsState.ran(TestsState.ESTABLISHMENT_CREATE_ESTABLISHMENTS)) {
            return;
        }
        TestsState.mark(TestsState.ESTABLISHMENT_CREATE_ESTABLISHMENTS);
        try {
            String jwt;
            jwt = new AuthTests(mockMvc).login(initAuth);
            ResultActions result = CreateTest.post(mockMvc, "/establishment", jwt,
                    EstablishmentData.establishments.get(0).toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
            for (Long employeeId : EmployeeData.adminEstablishments.keySet()) {
                jwt = new EmployeeTests(mockMvc).loginById(employeeId, initEmployee);
                for (Long establishmentId : EmployeeData.adminEstablishments.get(employeeId)) {
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
        if (TestsState.ran(TestsState.ESTABLISHMENT_TEST_EMPLOYEES)) {
            return;
        }
        TestsState.mark(TestsState.ESTABLISHMENT_TEST_EMPLOYEES);
        try {
            createEstablishments(initAuth, initEmployee);

            for (Long establishmentId : EstablishmentData.establishments.stream().map(BaseEstablishmentDTO::getId)
                    .toList()) {
                String jwt = loginAdminByEstablishmentId(establishmentId);
                for (Long employeeId : EmployeeData.employeesEstablishments.keySet()) {
                    if (EmployeeData.employeesEstablishments.get(employeeId).contains(establishmentId)) {
                        create(String.format("/establishment/%d/employee/%d", establishmentId, employeeId), jwt,
                                EmployeeData.employees.stream().filter(e -> e.getId().equals(employeeId)).findFirst()
                                        .orElseThrow(NotFoundException::new).toString());
                    }
                }
            }

            String jwt = new EmployeeTests(mockMvc).loginById(3L, false);
            Long establishmentId = EstablishmentData.establishments.get(0).getId();
            UserCreateDTO employee = EmployeeData.employees.get(1);
            ResultActions result = CreateTest.post(mockMvc,
                    String.format("/establishment/%d/employee/%d", establishmentId, employee.getId()), jwt,
                    employee.toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
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
        if (TestsState.ran(TestsState.ESTABLISHMENT_TEST_SERVICE)) {
            return;
        }
        TestsState.mark(TestsState.ESTABLISHMENT_TEST_SERVICE);
        try {
            testEmployees(initAuth, initEmployee);
            EmployeeTests employeeTests = new EmployeeTests(mockMvc);
            employeeTests.createServices(false);
            String jwt;
            Long employeeId;
            for (CreateEstablishmentServiceDTO service : EstablishmentData.establishmentServices) {
                employeeId = ServiceData.services.stream().filter(e -> e.getId().equals(service.getServiceId()))
                        .findFirst().orElseThrow(NotFoundException::new).getEmployeeId();
                jwt = employeeTests.loginById(employeeId, false);
                create(String.format("/establishment/%d/service", service.getEstablishmentId()), jwt,
                        service.toString());
            }
            jwt = employeeTests.login(1, false);
            CreateEstablishmentServiceDTO service = EstablishmentData.establishmentServices.get(2);
            ResultActions result = CreateTest.post(mockMvc, "/establishment/1/service", jwt, service.toString());
            result.andExpect(MockMvcResultMatchers.status().isForbidden());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listEmployees() {
        listEmployees(true, true);
    }

    public void listEmployees(boolean initAuth, boolean initEmployee) {
        try {
            testEmployees(initAuth, initEmployee);
            ResultActions result = CreateTest.get(mockMvc, "/establishment/1/employees");
            result.andExpect(MockMvcResultMatchers.status().isOk());
            JSONObject response = new JSONObject(result.andReturn().getResponse().getContentAsString());
            List<EmployeeDTO> _employees = JSONToDTO.fromPageDTO(response, EmployeeDTO.class);
            _employees.sort(Comparator.comparingLong(EmployeeDTO::getId));
            List<UserTestDTO> employees = EmployeeData.employees.stream()
                    .filter(e -> EmployeeData.employeesEstablishments.get(e.getId()).contains(1L)).toList();
            assert employees.equals(_employees);
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
        listEstablishmentServices(true, true);
    }

    public void listEstablishmentServices(boolean initAuth, boolean initEmployee) {
        try {
            testService(initAuth, initEmployee);
            ResultActions result = CreateTest.get(mockMvc, "/establishment/1/services");
            result.andExpect(MockMvcResultMatchers.status().isOk());
            JSONObject response = new JSONObject(result.andReturn().getResponse().getContentAsString());
            List<ServiceDTO> serviceDTO = JSONToDTO.fromPageDTO(response, ServiceDTO.class);
            assert serviceDTO != null;
            serviceDTO.sort(Comparator.comparingLong(ServiceDTO::getId));
            assert testEstablishmentServices(1L, serviceDTO);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listClosestEstablishments() {
        listClosestEstablishments(true, true);
    }

    public void listClosestEstablishments(boolean initAuth, boolean initEmployee) {
        try {
            testService(initAuth, initEmployee);
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
            if (EmployeeData.adminEstablishments.get(employeeId).contains(id)) {
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
        if (TestsState.ran(TestsState.ESTABLISHMENT_ADD_IMAGES)) {
            return;
        }
        TestsState.mark(TestsState.ESTABLISHMENT_ADD_IMAGES);
        try {
            createEstablishments(initAuth, initEmployee);
            for (Long establishmentId : EstablishmentData.establishments.stream().map(BaseEstablishmentDTO::getId)
                    .toList()) {
                String jwt = loginAdminByEstablishmentId(establishmentId);
                List<ImageDTO> images = EstablishmentData.establishmentImages.get(establishmentId);
                addImageAndCheckIfSaved(images, jwt, establishmentId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void deleteImages() {
        return;
        // deleteImages(true, true);
    }

    public void deleteImages(boolean initAuth, boolean initEmployee) {
        if (TestsState.ran(TestsState.ESTABLISHMENT_DELETE_IMAGES)) {
            return;
        }
        TestsState.mark(TestsState.ESTABLISHMENT_DELETE_IMAGES);
        try {
            addImages(initAuth, initEmployee);
            for (Long establishmentId : EstablishmentData.establishments.stream().map(BaseEstablishmentDTO::getId)
                    .toList()) {
                String jwt = loginAdminByEstablishmentId(establishmentId);
                List<ImageDTO> images = EstablishmentData.establishmentImages.get(establishmentId).subList(0, 1);
                addImageAndCheckIfSaved(images, jwt, establishmentId);
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
        ImageUtils imageUtils = new ImageUtils(mockMvc, String.format("/establishment/%d", establishmentId));
        imageUtils.saveImages(images, jwt);
        List<ImageDTO> _images = imageUtils.getImages(jwt);
        if (!_images.equals(images))
            System.out.println();
        assert _images.equals(images);
    }
}
