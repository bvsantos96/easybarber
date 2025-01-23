package com.teamsantos.easybarber.DTO.filters;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDynamicFilter {
    private Long establishmentId;
    private Long establishmentServiceEmployeeId;
    private Long establishmentEmployeeId;
    private Long establishmentServiveId;
    private LocalDateTime from;
    private LocalDateTime to;
}
