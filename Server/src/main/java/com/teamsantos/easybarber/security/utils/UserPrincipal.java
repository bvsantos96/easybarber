package com.teamsantos.easybarber.security.utils;

import com.teamsantos.easybarber.services.UserTypeService;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPrincipal {
    private long id;
    private Long employeeId;
    private boolean[] roles;
    private Object[] permissions;

    public UserPrincipal(long userId, Long employeeId, boolean[] roles) {
        this.id = userId;
        this.employeeId = employeeId;
        this.roles = new boolean[UserTypeService.UserTypes.values().length];
        if (roles != null) {
            for (int i = 0; i < roles.length; i++) {
                this.roles[i] = roles[i];
            }
        }
    }

    public boolean hasPermission(UserTypeService.UserTypes userType) {
        if (userType == null)
            return false;
        return roles[userType.ordinal()];
    }
}
