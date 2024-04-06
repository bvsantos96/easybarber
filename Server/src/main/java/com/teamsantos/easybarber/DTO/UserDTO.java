package com.teamsantos.easybarber.DTO;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseResponseDTO {
    private String mobile;
    private String countryMobile;
    private String name;
    private Long userTypeId;

    public UserDTO initName(String name) {
        this.name = name;
        return this;
    }

    public UserDTO initUserType(Long userTypeId) {
        this.userTypeId = userTypeId;
        return this;
    }

    public UserDTO initMobileInformation(String countryMobile, String mobile) {
        this.countryMobile = countryMobile;
        this.mobile = mobile;
        return this;
    }
}
