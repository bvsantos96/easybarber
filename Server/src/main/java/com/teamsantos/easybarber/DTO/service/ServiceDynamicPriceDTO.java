package com.teamsantos.easybarber.DTO.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDynamicPriceDTO {
    private Long id;
    private Integer duration;
    private Double price;
    private Boolean usingDynamicPrice;
}
