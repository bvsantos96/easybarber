package com.teamsantos.easybarber.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeBaseDTO extends BaseDTO {
    private String name;
    private String image;

    public EmployeeBaseDTO(Long id, String name) {
        super(id);
        this.name = name;
    }

    public EmployeeBaseDTO(Long id, String name, String image) {
        this(id, name);
        this.image = image;
    }
}
