package com.teamsantos.easybarber.testDTOs;

import com.teamsantos.easybarber.DTO.user.UserCreateDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserTestDTO extends UserCreateDTO {
    private String jwt;

    public UserTestDTO(String countryMobile, String mobile, String password, String name) {
        super(countryMobile, mobile, password, name);
    }

    public UserTestDTO(Long id, String countryMobile, String mobile, String password, String name) {
        super(id, countryMobile, mobile, password, name);
    }

    public static UserTestDTO createDummy(int idx) {
        return new UserTestDTO("+351", "90000000" + idx, "Test123*", "Test" + idx);
    }
}
