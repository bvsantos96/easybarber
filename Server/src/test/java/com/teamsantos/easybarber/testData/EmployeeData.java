package com.teamsantos.easybarber.testData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.teamsantos.easybarber.DTO.UserCreateDTO;

public class EmployeeData {
    public static final List<UserCreateDTO> employees;
    public static final Map<Long, List<Long>> employeesEstablishments;

    static {
        employees = new ArrayList<>();
        employees.add(new UserCreateDTO(2L, "+351", "999999999", "Test123*", "Henrique"));
        employees.add(new UserCreateDTO(3L, "+351", "900000000", "Test123*", "Amigo do Joao"));
        employees.sort(Comparator.comparing(UserCreateDTO::getId));

        employeesEstablishments = new TreeMap<>();
        employeesEstablishments.put(2L, new ArrayList<>());
        employeesEstablishments.get(2L).add(1L);
        employeesEstablishments.put(3L, new ArrayList<>());
        employeesEstablishments.get(3L).add(2L);
    }
}
