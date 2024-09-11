package com.teamsantos.easybarber.DTO;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.teamsantos.easybarber.entities.UserType;
import com.teamsantos.easybarber.utils.JSONToDTO;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends BaseResponseDTO {
    private String mobile;
    private String countryMobile;
    @Nullable
    private String name;
    @Nullable
    private Set<Long> userTypes;

    public UserDTO(Long id, String mobileCode, String mobileNumber, String name) {
        super(id);
        this.countryMobile = mobileCode;
        this.mobile = mobileNumber;
        this.name = name;
    }

    public UserDTO initName(String name) {
        this.name = name;
        return this;
    }

    public UserDTO initUserType(long userTypeId) {
        this.userTypes = Set.of(userTypeId);
        return this;
    }

    public UserDTO initMobileInformation(String countryMobile, String mobile) {
        this.countryMobile = countryMobile;
        this.mobile = mobile;
        return this;
    }

    public String getMobileInformation() {
        return this.countryMobile + this.mobile;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        try {
            String _name = JSONToDTO.getString(obj, "name");
            Set<Long> _userTypeId = JSONToDTO.getSetLong(obj, "userTypeId");
            String mobile = JSONToDTO.getString(obj, "mobile");
            String countryMobile = JSONToDTO.getString(obj, "countryMobile");
            return this.mobile.equals(Objects.requireNonNull(mobile))
                    && this.countryMobile.equals(Objects.requireNonNull(countryMobile))
                    && (this.name == null || _name == null || this.name.equals(_name))
                    && (this.userTypes == null || _userTypeId == null || this.userTypes.equals(_userTypeId));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setServiceTypeIDFromHash(Set<UserType> userTypes) {
        try {
            this.userTypes = userTypes.stream().map(UserType::getId).collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
