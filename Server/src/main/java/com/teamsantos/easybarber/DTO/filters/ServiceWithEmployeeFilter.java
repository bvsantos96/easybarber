package com.teamsantos.easybarber.DTO.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceWithEmployeeFilter extends ServiceFilter {
    private boolean includeEmployeeImage = false;
}
