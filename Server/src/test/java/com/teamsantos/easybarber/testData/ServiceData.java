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
                    add(new ServiceDTO(1L, 1L, 1L, "Haircut", "Simple haircut", "https://youtube.com", 10.0));
                    add(new ServiceDTO(2L, 1L, 2L, "Beard", "Simple beard trim", "https://youtube.com", 5.0));
                    add(new ServiceDTO(3L, 2L, 3L, "Beard and haircut", "Simple haircut and beard trim",
                            "https://youtube.com", 15.0));
                }
            };
            serviceUpdate = new ArrayList<>() {
                {
                    add(new ServiceDTO().addId(1L).addDescription("Simple haircut (updated)"));
                    add(new ServiceDTO().addId(2L).addDescription("Simple beard trim (updated)"));
                    add(new ServiceDTO().addId(3L).addDescription("Simple haircut and beard trim (updated)"));
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
