package com.teamsantos.easybarber.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeDTO extends UserCreateDTO {
    private String description;
}
