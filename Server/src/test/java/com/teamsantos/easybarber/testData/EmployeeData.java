package com.teamsantos.easybarber.testData;

import com.teamsantos.easybarber.DTO.UserCreateDTO;

import java.util.ArrayList;
import java.util.List;

public class EmployeeData {
    public static final List<UserCreateDTO> employees;

    static {
        employees = new ArrayList<>() {
            {
                add(new UserCreateDTO(1L, "+351", "999999999", "Test123*", "Henrique"));
                add(new UserCreateDTO(2L, "+351", "900000000", "Test123*", "Amigo do Joao"));
            }
        };
    }
}
