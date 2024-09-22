package com.teamsantos.easybarber.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsDTO {
    private String phoneNr;
    private String body;
    private String confirmationCode;
}
