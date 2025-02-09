package com.teamsantos.easybarber.DTO.employee;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.entities.Employee;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeListDTO extends BaseResponseDTO {
    private String name;
    private String mobileNumber;
    private String image;
    private String description;
    private long nVotes;
    private long sumVotes;
    private boolean absent;
    private String absentMessage;

    public EmployeeListDTO(Employee employee, String imageData, boolean absent, String absentMessage) {
        super(employee.getId());
        this.name = employee.getUser().getName();
        this.mobileNumber = employee.getUser().getMobileInformation();
        this.image = imageData;
        this.description = employee.getDescription();
        this.nVotes = employee.getNVotes();
        this.sumVotes = employee.getSumVotes();
        this.absent = absent;
        this.absentMessage = absentMessage;
    }
}
