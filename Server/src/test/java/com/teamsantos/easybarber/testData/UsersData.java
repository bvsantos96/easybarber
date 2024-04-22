package com.teamsantos.easybarber.testData;

import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.DTO.UserDTO;

import java.util.ArrayList;
import java.util.List;

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
        usersUpdateDTO = new ArrayList<>() {
            {
                add(new UserDTO().initName("Bruno Vicente dos Santos"));
                add(new UserDTO().initName("Filipe Miguel Pinho Santos"));
            }
        };
    }
}
