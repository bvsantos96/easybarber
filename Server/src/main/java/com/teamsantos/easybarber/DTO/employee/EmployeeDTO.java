package com.teamsantos.easybarber.DTO.employee;

import java.util.Arrays;

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
    private long rating;
    private long nVotes;
    private String image;
    private Long[] serviceTypes;
    private Boolean me;

    public EmployeeDTO() {
        super();
    }

    public EmployeeDTO(Long id, String mobileCode, String mobileNumber, String name, String description) {
        super(id, mobileCode, mobileNumber, name);
        this.description = description;
    }

    public EmployeeDTO(String description) {
        this.description = description;
    }

    public EmployeeDTO(Long id, Employee employee, Boolean me) {
        this(employee);
        this.me = me;
        this.setId(id);
    }

    public EmployeeDTO(Employee employee) {
        super(employee.getId(), employee.getUser().getCountryMobile(), employee.getUser().getMobile(),
                employee.getUser().getName());
        this.description = employee.getDescription();
        this.rating = (employee.getNVotes() == null || employee.getNVotes() == 0) ? 0
                : employee.getSumVotes() / employee.getNVotes();
        this.nVotes = (employee.getNVotes() == null || employee.getNVotes() == 0) ? 0 : employee.getNVotes();
    }

    public EmployeeDTO(Employee employee, String data) {
        this(employee);
        this.image = data;
    }

    public EmployeeDTO(Long id, String mobileCode, String mobileNumber, String name, String description, Long sumVotes,
            Long nVotes, String image, Object serviceTypes) {
        super(id, mobileCode, mobileNumber, name);
        this.description = description;
        this.rating = (sumVotes == null || nVotes == null || nVotes == 0) ? 0 : sumVotes / nVotes;
        this.nVotes = nVotes == null ? 0 : nVotes;
        this.image = image;
        if (serviceTypes == null) {
            this.serviceTypes = new Long[0];
        } else {
            String _serviceTypes = serviceTypes.toString();
            this.serviceTypes = (serviceTypes == null || _serviceTypes.isEmpty()) ? new Long[0]
                    : Arrays.stream(_serviceTypes.split(",")).map(Long::parseLong).toArray(Long[]::new);
        }
    }
}
