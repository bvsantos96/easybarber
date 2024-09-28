package com.teamsantos.easybarber.DTO.user;

import com.teamsantos.easybarber.DTO.BaseDTO;
import com.teamsantos.easybarber.DTO.employee.EmployeeDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO extends BaseDTO {
    private String countryMobile;
    private String mobile;
    private String password;
    private String name;

    public String getMobileInformation() {
        return countryMobile + mobile;
    }

    public UserCreateDTO(Long id, String countryMobile, String mobile, String password, String name) {
        super(id);
        this.countryMobile = countryMobile;
        this.mobile = mobile;
        this.password = password;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }
        if (obj instanceof EmployeeDTO) {
            EmployeeDTO employeeDTO = (EmployeeDTO) obj;
            return this.countryMobile.equals(employeeDTO.getCountryMobile())
                    && this.mobile.equals(employeeDTO.getMobile()) && this.name.equals(employeeDTO.getName());
        }
        return false;
    }
}
