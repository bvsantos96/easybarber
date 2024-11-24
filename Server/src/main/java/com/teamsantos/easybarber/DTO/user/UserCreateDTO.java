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

    public String getCountryMobile() {
        return countryMobile.startsWith("+") ? countryMobile : "+" + countryMobile;
    }

    public String getMobileInformation() {
        return getCountryMobile() + mobile.replace(" ", "");
    }

    public UserCreateDTO(Long id, String countryMobile, String mobile, String password, String name) {
        super(id);
        this.countryMobile = countryMobile;
        this.mobile = mobile.replace(" ", "");
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

    public boolean isValidNumberString() {
        String pattern = "[-+]?\\d*\\.?\\d+";
        return getMobileInformation().matches(pattern);
    }

    public boolean isValidPassword() {
        // Check if the password meets the following criteria:
        // - At least 8 characters long
        // - Contains at least one uppercase letter
        // - Contains at least one lowercase letter
        // - Contains at least one digit
        boolean hasMinLength = password.length() >= 8;
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasDigit = password.matches(".*\\d.*");

        return hasMinLength && hasUppercase && hasLowercase && hasDigit;
    }
}
