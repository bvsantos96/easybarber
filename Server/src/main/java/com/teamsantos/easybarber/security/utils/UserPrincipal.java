package com.teamsantos.easybarber.security.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPrincipal {
    private Long id;
    private Long employeeId;
    private List<GrantedAuthority> roles;
    private Object[] permissions;

    public UserPrincipal(long userId, Long employeeId, List<String> roles) {
        this.id = userId;
        this.employeeId = employeeId;
        this.roles = new ArrayList<>();
        for (String role : roles) {
            this.roles.add(new SimpleGrantedAuthority(String.format("ROLE_%s", role.toUpperCase())));
        }
    }

    public org.springframework.security.core.userdetails.User getUserDetails() {
        return new org.springframework.security.core.userdetails.User(String.valueOf(id), "", roles);
    }
}
