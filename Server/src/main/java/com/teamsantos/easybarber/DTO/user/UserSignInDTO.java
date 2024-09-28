package com.teamsantos.easybarber.DTO.user;

import java.util.Set;

import com.teamsantos.easybarber.DTO.BaseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInDTO extends BaseDTO {
    private String password;
    private String mobileInformation;
    private Long employeeId;
    private Set<Long> userTypeIds;

    public UserSignInDTO(long id, String password, String mobileInformation, Long employeeId) {
        super(id);
        this.password = password;
        this.mobileInformation = mobileInformation;
        this.employeeId = employeeId;
    }
}
