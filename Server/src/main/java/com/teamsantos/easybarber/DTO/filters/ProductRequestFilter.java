package com.teamsantos.easybarber.DTO.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestFilter {
    private Long appointmentId;
    private Long establishmentId;
    private Long employeeId;
    private Long productId;
}
