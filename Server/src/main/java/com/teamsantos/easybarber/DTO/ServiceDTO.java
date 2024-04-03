package com.teamsantos.easybarber.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ServiceDTO {
    private Long id;
    private Long employeeId;
    private Long serviceTypeId;
    private String name;
    private String description;
    private String imageUrl;
    private double price;
}
