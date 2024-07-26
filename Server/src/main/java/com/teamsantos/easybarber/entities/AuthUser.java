package com.teamsantos.easybarber.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthUser {
    private Long id;
    private String mobileInformation;
    private String password;
    private boolean isEmployee;
}
