package com.teamsantos.easybarber.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserSignInDTO extends BaseDTO {
    private String password;
    private String mobileInformation;
}
