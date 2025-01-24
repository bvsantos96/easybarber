package com.teamsantos.easybarber.DTO.filters;

import java.util.List;

import com.teamsantos.easybarber.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilter {
    private String name;
    private String description;
    private Long establishmentId;
    private Long employeeId;
    private List<Long> productTypeId;

    public String getName() {
        return Utils.formatStringToLIKE(name);
    }

    public String getDescription() {
        return Utils.formatStringToLIKE(description);
    }
}
