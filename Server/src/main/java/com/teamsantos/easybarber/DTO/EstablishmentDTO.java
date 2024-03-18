package com.teamsantos.easybarber.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class EstablishmentDTO extends BaseEstablishmentDTO {
    private Set<UserDTO> owner;
}
