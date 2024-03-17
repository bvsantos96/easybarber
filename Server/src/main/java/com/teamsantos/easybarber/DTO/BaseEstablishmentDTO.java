package com.teamsantos.easybarber.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseEstablishmentDTO extends BaseResponseDTO{
    private Long id;
    private String name;
    private String description;
}
