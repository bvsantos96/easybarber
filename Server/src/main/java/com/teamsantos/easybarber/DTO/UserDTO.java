package com.teamsantos.easybarber.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends BaseResponseDTO {
    private Long userTypeId;
    private String countryMobile;
    private String mobile;
}
