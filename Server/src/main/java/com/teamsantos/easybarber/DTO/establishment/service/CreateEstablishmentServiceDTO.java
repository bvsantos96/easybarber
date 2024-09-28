package com.teamsantos.easybarber.DTO.establishment.service;

import com.teamsantos.easybarber.DTO.BaseDTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateEstablishmentServiceDTO extends BaseDTO {
    private Long serviceId;
    private Long establishmentId;
    private Double price;
    private Boolean active;

    public Boolean getActive() {
        return null != active ? active : true;
    }

    public CreateEstablishmentServiceDTO(Long id, Long serviceId, Long establishmentId, Double price, Boolean active) {
        super(id);
        this.serviceId = serviceId;
        this.establishmentId = establishmentId;
        this.price = price;
        this.active = active;
        this.active = getActive();
    }
}
