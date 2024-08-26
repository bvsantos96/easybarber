package com.teamsantos.easybarber.testData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.DTO.UserDTO;

public class UsersData {
    public static final List<UserCreateDTO> usersDTO;
    public static final List<UserDTO> usersUpdateDTO;

    static {
        usersDTO = new ArrayList<>() {
            {
                add(new UserCreateDTO("+351", "927030780", "Test123*", "Bruno Santos"));
                add(new UserCreateDTO("+351", "962844407", "Test123*", "Filipe Santos"));
            }
        };
        usersDTO.sort(Comparator.comparing(UserCreateDTO::getId));

        usersUpdateDTO = new ArrayList<>() {
            {
                add(new UserDTO().initName("Bruno Vicente dos Santos"));
                add(new UserDTO().initName("Filipe Miguel Pinho Santos"));
            }
        };
        usersUpdateDTO.sort(Comparator.comparing(UserDTO::getId));
    }
}
