package com.teamsantos.easybarber.DTO;

import lombok.Data;

@Data
public class UserCreateDTO {
    private String          password;
    private String          countryMobile;
    private String          mobile;

    public String getMobileInformation() {
        return countryMobile + mobile;
    }
}
