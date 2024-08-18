package com.teamsantos.easybarber.testData;

import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceTypeDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ServiceData {
    public static final List<ServiceTypeDTO> serviceTypes;
    public static final List<ServiceDTO> services;
    public static final List<ServiceDTO> serviceUpdate;
    public static final List<ServiceTypeDTO> serviceTypesUpdate;
    public static final Map<Long, List<ImageDTO>> serviceImages;

    static {
        try {
            serviceTypes = new ArrayList<>() {
                {
                    add(new ServiceTypeDTO(1L, "Haircut", "Simple haircut", "/icons/categories/haircut.svg"));
                    add(new ServiceTypeDTO(2L, "Spa", "Spa", "/icons/categories/spa.svg"));
                    add(new ServiceTypeDTO(3L, "Creambath", "Creambath", "/icons/categories/creamBath.svg"));
                    add(new ServiceTypeDTO(4L, "Massage", "Massage", "/icons/categories/massage.svg"));
                }
            };
            serviceTypes.sort(Comparator.comparing(ServiceTypeDTO::getId));

            services = new ArrayList<>() {
                {
                    add(new ServiceDTO(1L, EmployeeData.employees.get(0).getId(), serviceTypes.get(0).getId(),
                            "Haircut", "Simple haircut", "https://youtube.com", 10.0, 30));
                    add(new ServiceDTO(2L, EmployeeData.employees.get(0).getId(), serviceTypes.get(1).getId(), "Beard",
                            "Simple beard trim", "https://youtube.com", 5.0, 15));
                    add(new ServiceDTO(3L, EmployeeData.employees.get(1).getId(), serviceTypes.get(2).getId(),
                            "Beard and haircut", "Simple haircut and beard trim",
                            "https://youtube.com", 15.0, 45));
                }
            };
            services.sort(Comparator.comparing(ServiceDTO::getId));

            serviceUpdate = new ArrayList<>() {
                {
                    add(new ServiceDTO().addId(services.get(0).getId()).addDescription("Simple haircut (updated)"));
                    add(new ServiceDTO().addId(services.get(1).getId()).addDescription("Simple beard trim (updated)"));
                    add(new ServiceDTO().addId(services.get(2).getId())
                            .addDescription("Simple haircut and beard trim (updated)"));
                }
            };
            serviceUpdate.sort(Comparator.comparing(ServiceDTO::getId));

            serviceTypesUpdate = new ArrayList<>() {
                {
                    add(new ServiceTypeDTO().addId(serviceTypes.get(0).getId())
                            .addDescription("Simple haircut (updated)"));
                    add(new ServiceTypeDTO().addId(serviceTypes.get(1).getId())
                            .addDescription("Simple beard trim (updated)"));
                    add(new ServiceTypeDTO().addId(serviceTypes.get(2).getId())
                            .addDescription("Simple haircut and beard trim (updated)"));
                }
            };
            serviceTypesUpdate.sort(Comparator.comparing(ServiceTypeDTO::getId));

            serviceImages = Map.of(1L, new ArrayList<>() {
                {
                    add(new ImageDTO(null,
                            "https://cdn-fnknc.nitrocdn.com/jwqHRGAzpUgGskUSHlppNQzwuXgXIKwg/assets/images/optimized/rev-99e07b0/www.fashionbeans.com/wp-content/uploads/2023/08/smartcutzbarbers_manwithnumber2andskinfadehaircut-696x445.jpg"));
                    add(new ImageDTO(null,
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQg6C-koP0XN8yAeszXhRukicFHhQnSkhVQUrQABvhnKQ&s"));
                    add(new ImageDTO(null, "https://i.pinimg.com/736x/09/25/9d/09259d4ab3cbf58d8d09312d4c1816b8.jpg"));
                }
            }, 2L, new ArrayList<>() {
                {
                    add(new ImageDTO(null, "https://i.ytimg.com/vi/KBKAIdtRinc/maxresdefault.jpg"));
                    add(new ImageDTO(null,
                            "https://i.ytimg.com/vi/V4aG7_zsyrg/hq720.jpg?sqp=-oaymwEhCK4FEIIDSFryq4qpAxMIARUAAAAAGAElAADIQj0AgKJD&rs=AOn4CLCQSDIXessxdD6pYgn5Wr5uI2AGcA"));
                    add(new ImageDTO(null,
                            "https://cdn.thebeardclub.com/articles/Trim_Your_Beard_2_3202ea96-9f43-43af-bc17-81955f6ddabc_1920x.jpg?ixlib=imgixjs-4.0.1"));
                }
            }, 3L, new ArrayList<>() {
                {
                    add(new ImageDTO(null,
                            "https://cdn11.bigcommerce.com/s-h7l2pcerei/product_images/uploaded_images/trimming-beard.jpg"));
                    add(new ImageDTO(null,
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTOfIzEg-rxJJg7GqAE9vsPYE9NaduGYpFsh0NDOR8DGw&s"));
                    add(new ImageDTO(null,
                            "https://cdn.shopify.com/s/files/1/0013/3536/1603/files/Short-And-Shaped.jpg?v=1603734407"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
