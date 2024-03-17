package com.teamsantos.easybarber.DTO;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EstablishmentDTO extends BaseEstablishmentDTO {
    private Set<UserDTO> owner;
}
