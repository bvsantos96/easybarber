package com.teamsantos.easybarber.testData;

import java.util.ArrayList;
import java.util.List;

import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceTypeDTO;

public class ServiceData {
    public static final List<ServiceTypeDTO> serviceTypes;
    public static final List<ServiceDTO> services;
    public static final List<ServiceDTO> serviceUpdate;

    static {
        try {
            serviceTypes = new ArrayList<>() {
                {
                    add(new ServiceTypeDTO(1L, "Haircut", "Simple haircut", "https://google.com"));
                    add(new ServiceTypeDTO(2L, "Beard", "Simple beard trim", "https://google.com"));
                    add(new ServiceTypeDTO(3L, "Beard and haircut", "Simple haircut and beard trim",
                            "https://google.com"));
                }
            };
            services = new ArrayList<>() {
                {
                    add(new ServiceDTO(1L, EmployeeData.employees.get(0).getId(), serviceTypes.get(0).getId(), "Haircut", "Simple haircut", "https://youtube.com", 10.0));
                    add(new ServiceDTO(2L, EmployeeData.employees.get(0).getId(), serviceTypes.get(1).getId(), "Beard", "Simple beard trim", "https://youtube.com", 5.0));
                    add(new ServiceDTO(3L, EmployeeData.employees.get(1).getId(), serviceTypes.get(2).getId(), "Beard and haircut", "Simple haircut and beard trim",
                            "https://youtube.com", 15.0));
                }
            };
            serviceUpdate = new ArrayList<>() {
                {
                    add(new ServiceDTO().addId(services.get(0).getId()).addDescription("Simple haircut (updated)"));
                    add(new ServiceDTO().addId(services.get(1).getId()).addDescription("Simple beard trim (updated)"));
                    add(new ServiceDTO().addId(services.get(2).getId()).addDescription("Simple haircut and beard trim (updated)"));
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
