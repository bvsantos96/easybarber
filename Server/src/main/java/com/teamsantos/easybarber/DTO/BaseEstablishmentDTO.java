package com.teamsantos.easybarber.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseEstablishmentDTO extends BaseResponseDTO {
    private Long id;
    private String name;
    private String description;

    public BaseEstablishmentDTO() {
        super();
    }

    public BaseEstablishmentDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public BaseEstablishmentDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
