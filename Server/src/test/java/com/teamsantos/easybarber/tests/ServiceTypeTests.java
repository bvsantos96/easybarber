package com.teamsantos.easybarber.tests;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.DTO.service.ServiceDTO;
import com.teamsantos.easybarber.DTO.service.ServiceWithImagesDTO;
import com.teamsantos.easybarber.testData.ServiceData;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.JSONToDTO;
import com.teamsantos.easybarber.utils.TestsState;

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
        if (TestsState.ran(TestsState.SERVICE_TYPES_CREATE_SERVICE_TYPES)) {
            return;
        }
        TestsState.mark(TestsState.SERVICE_TYPES_CREATE_SERVICE_TYPES);
        try {
            String jwt = new AuthTests(mockMvc).loginAdmin();
            ServiceData.serviceTypes.forEach(serviceType -> {
                try {
                    create("/service", jwt, serviceType.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    org.junit.jupiter.api.Assertions.fail(e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listServicesByType() {
        try {
            new EmployeeTests(mockMvc).createServices(true);
            long serviceType = 3L;
            ResultActions result = CreateTest.get(mockMvc, String.format("/service/list?serviceType=%d", serviceType));
            result.andExpect(MockMvcResultMatchers.status().isOk());
            List<ServiceWithImagesDTO> services = JSONToDTO.fromPageDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), ServiceWithImagesDTO.class);
            assert services != null;
            List<ServiceDTO> _services = getServicesByServiceType(serviceType);
            assert services.size() == _services.size();
            for (int i = 0; i < _services.size(); i++) {
                assert services.get(i).equalsWithoutPrice(_services.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    static public List<ServiceDTO> getServicesByServiceType(long serviceTypeId) {
        try {
            return ServiceData.services.stream().filter(e -> e.getServiceTypeId().equals(serviceTypeId))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
        return null;
    }

    static public Set<Long> getServicesIdByServiceType(long serviceTypeId) {
        try {
            return ServiceData.services.stream().filter(e -> e.getServiceTypeId().equals(serviceTypeId))
                    .map(e -> e.getId()).collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
        return null;
    }
}
