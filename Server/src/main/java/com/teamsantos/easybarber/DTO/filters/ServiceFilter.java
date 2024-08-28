package com.teamsantos.easybarber.DTO.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServiceFilter {
    private String name;
    private Long serviceTypeId;
    private Long employeeId;
    private boolean includeServiceImage = true;
    private boolean includeEmployeeImage = false;

    public void parseName() {
        if (name != null) {
            name = String.format("%%%s%%", name.trim());
        }
    }
}
