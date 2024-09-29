package com.teamsantos.easybarber.DTO.service;

import com.teamsantos.easybarber.DTO.BaseDTO;
import com.teamsantos.easybarber.DTO.image.ImageDTO;
import com.teamsantos.easybarber.entities.images.ServiceImage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceListDTO extends BaseDTO {
    private long serviceTypeId;
    private String name;
    private String description;
    private double price;
    private ImageDTO image;

    public ServiceListDTO(long id, long serviceTypeId, String name, String description, double price,
            ServiceImage image) {
        this(id, serviceTypeId, name, description, price);
        this.image = image.convertToDTO();
    }

    public ServiceListDTO(long id, long serviceTypeId, String name, String description, double price) {
        super(id);
        this.serviceTypeId = serviceTypeId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public ServiceListDTO(long id, long serviceTypeId, String name, String description, double price, String image) {
        this(id, serviceTypeId, name, description, price);
        this.image = new ImageDTO(0L, image, false);
    }
}
