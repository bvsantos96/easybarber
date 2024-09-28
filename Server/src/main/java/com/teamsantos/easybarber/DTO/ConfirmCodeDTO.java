package com.teamsantos.easybarber.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmCodeDTO {
    private String phoneNr;
    private String confirmationCode;
}
