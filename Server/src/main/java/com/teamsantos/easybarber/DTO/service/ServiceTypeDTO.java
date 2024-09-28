package com.teamsantos.easybarber.DTO.service;

import com.teamsantos.easybarber.DTO.BaseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTypeDTO extends BaseDTO {
    private String name;
    private String description;
    private String imageURL;

    public ServiceTypeDTO(Long id, String name, String description) {
        super(id);
        this.name = name;
        this.description = description;
    }

    public ServiceTypeDTO(Long id, String name, String description, String imageURL) {
        super(id);
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
    }

    @Override
    public ServiceTypeDTO addId(Long id) {
        super.addId(id);
        return this;
    }

    public ServiceTypeDTO addDescription(String description) {
        this.description = description;
        return this;
    }
}
