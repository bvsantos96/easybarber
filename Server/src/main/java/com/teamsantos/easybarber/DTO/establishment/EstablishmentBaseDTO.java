package com.teamsantos.easybarber.DTO.establishment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EstablishmentBaseDTO {
    private long id;
    private String name;
    private String image;
}
