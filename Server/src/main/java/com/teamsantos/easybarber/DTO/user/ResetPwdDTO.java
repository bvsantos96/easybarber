package com.teamsantos.easybarber.DTO.user;

import lombok.Data;

@Data
public class ResetPwdDTO {
    private String confirmationCode;
    private String phoneNr;
    private String newPassword;
}
