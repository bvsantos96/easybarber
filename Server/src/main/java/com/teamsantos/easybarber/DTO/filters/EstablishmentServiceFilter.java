package com.teamsantos.easybarber.DTO.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EstablishmentServiceFilter extends ServiceWithEmployeeFilter {
    private Long establishmentId;
    private boolean includeEstablishmentImage = true;
}
