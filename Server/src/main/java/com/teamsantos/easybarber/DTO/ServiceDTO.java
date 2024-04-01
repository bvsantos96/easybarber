package com.teamsantos.easybarber.DTO;

import lombok.Data;

@Data
public class ServiceDTO {
    private Long id;
    private Long employeeId;
    private Long serviceTypeId;
    private String name;
    private String description;
    private String imageUrl;
    private double price;
}
