package com.teamsantos.easybarber.DTO;

import java.util.Objects;

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
    private Long userTypeId;

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

    public UserDTO initUserType(Long userTypeId) {
        this.userTypeId = userTypeId;
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
            Long _userTypeId = JSONToDTO.getLong(obj, "userTypeId");
            String _name = JSONToDTO.getString(obj, "name");
            return this.mobile.equals(Objects.requireNonNull(JSONToDTO.getString(obj, "mobile")))
                    && this.countryMobile
                            .equals(Objects.requireNonNull(JSONToDTO.getString(obj, "countryMobile")))
                    && (this.name == null || _name == null || this.name.equals(_name))
                    && (this.userTypeId == null || _userTypeId == null
                            || this.userTypeId.equals(_userTypeId));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
