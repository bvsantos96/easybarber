package com.teamsantos.easybarber.DTO;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInDTO {
    private long id;
    private String password;
    private String mobileInformation;
    private Long employeeId;
    private Set<Long> userTypeIds;
}
