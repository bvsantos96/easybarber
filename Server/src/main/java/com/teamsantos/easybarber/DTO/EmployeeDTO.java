package com.teamsantos.easybarber.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeDTO extends UserDTO {
    private String description;

    public EmployeeDTO(Long id, String mobileCode, String mobileNumber, String name, String description) {
        super(id, mobileCode, mobileNumber, name);
        this.description = description;
    }
}
