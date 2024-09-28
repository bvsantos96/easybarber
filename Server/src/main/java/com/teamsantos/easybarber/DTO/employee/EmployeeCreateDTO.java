package com.teamsantos.easybarber.DTO.employee;

import com.teamsantos.easybarber.DTO.user.UserCreateDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeCreateDTO extends UserCreateDTO {
    private String description;
}
