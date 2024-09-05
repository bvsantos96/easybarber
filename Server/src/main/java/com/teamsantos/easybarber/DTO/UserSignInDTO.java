package com.teamsantos.easybarber.DTO;

import java.util.Set;
import java.util.stream.Collectors;

import com.teamsantos.easybarber.entities.UserType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInDTO extends BaseDTO {
    private String password;
    private String mobileInformation;
    private Long employeeId;
    private Set<Long> userTypeIds;

    public UserSignInDTO(long id, String password, String mobileInformation, Long employeeId,
            UserType userTypeIds) {
        super(id);
        this.password = password;
        this.mobileInformation = mobileInformation;
        this.employeeId = employeeId;
        this.userTypeIds = Set.of(userTypeIds.getId());
    }

    public UserSignInDTO(long id, String password, String mobileInformation, Long employeeId,
            Set<UserType> userTypeIds) {
        super(id);
        this.password = password;
        this.mobileInformation = mobileInformation;
        this.employeeId = employeeId;
        this.setServiceTypeIDFromHash(userTypeIds);
    }

    public void setServiceTypeIDFromHash(Set<UserType> userTypes) {
        try {
            this.userTypeIds = userTypes.stream().map(UserType::getId).collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
