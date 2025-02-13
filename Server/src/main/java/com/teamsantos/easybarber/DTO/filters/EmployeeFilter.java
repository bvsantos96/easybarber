package com.teamsantos.easybarber.DTO.filters;

import java.util.List;

import com.teamsantos.easybarber.utils.Utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeFilter {
    private String name;
    private String mobileInformation;
    private List<Long> serviceTypeIds;
    private Long greaterThanRating;
    private Long lessThanRating;
    private Boolean hideDeleted; // If nul then show all, if true then hide deleted, if false then show only
                                 // deleted
    private Boolean hideNotApproved; // If nul then show all, if true then hide not approved, if false then show only
                                     // not approved

    public String getName() {
        return Utils.formatStringToLIKE(name);
    }

    public String getMobileInformation() {
        return Utils.formatStringToLIKE(mobileInformation);
    }
}
