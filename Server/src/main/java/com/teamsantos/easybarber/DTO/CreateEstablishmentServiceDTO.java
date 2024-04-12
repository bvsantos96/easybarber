package com.teamsantos.easybarber.DTO;

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
}
