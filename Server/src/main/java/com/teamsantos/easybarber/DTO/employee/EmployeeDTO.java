package com.teamsantos.easybarber.DTO.employee;

import com.teamsantos.easybarber.DTO.user.UserDTO;
import com.teamsantos.easybarber.entities.Employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeDTO extends UserDTO {
    private String description;

    public EmployeeDTO() {
        super();
    }

    public EmployeeDTO(Long id, String mobileCode, String mobileNumber, String name, String description) {
        super(id, mobileCode, mobileNumber, name);
        this.description = description;
    }

    public EmployeeDTO(Employee employee) {
        super(employee.getId(), employee.getUser().getCountryMobile(), employee.getUser().getMobile(),
                employee.getUser().getName());
        this.description = employee.getDescription();
    }
}
