package com.teamsantos.easybarber.testData;

import com.teamsantos.easybarber.DTO.UserCreateDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmployeeData {
    public static final List<UserCreateDTO> employees;
    public static final Map<Long, List<Long>> employeesEstablishments;

    static {
        employees = new ArrayList<>() {
            {
                add(new UserCreateDTO(2L, "+351", "999999999", "Test123*", "Henrique"));
                add(new UserCreateDTO(3L, "+351", "900000000", "Test123*", "Amigo do Joao"));
            }
        };
        employeesEstablishments = Map.of(
                2L, new ArrayList<>() {
                    {
                        add(1L);
                    }
                },
                3L, new ArrayList<>() {
                    {
                        add(2L);
                    }
                }
        );
    }
}
