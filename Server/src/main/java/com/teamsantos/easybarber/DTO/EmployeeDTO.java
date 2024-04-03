package com.teamsantos.easybarber.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class EmployeeDTO extends UserDTO {
    private String description;
}
