package com.teamsantos.easybarber.DTO.filters;

import com.teamsantos.easybarber.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceFilter {
    private String name;
    private String description;
    private Long serviceTypeId;
    private Long employeeId;
    private boolean includeServiceImage = true;

    public String getName() {
        return Utils.formatStringToLIKE(name);
    }

    public String getDescription() {
        return Utils.formatStringToLIKE(description);
    }
}
