package com.teamsantos.easybarber.testData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.teamsantos.easybarber.DTO.location.LocationDTO;
import com.teamsantos.easybarber.DTO.user.UserDTO;
import com.teamsantos.easybarber.testDTOs.UserTestDTO;

public class UsersData {
    public static UserTestDTO systemAdmin;
    public static final List<UserTestDTO> usersDTO;
    public static final List<UserDTO> usersUpdateDTO;
    public static final List<LocationDTO> locations;

    static {
        systemAdmin = new UserTestDTO("+1", "999999999", "Test123*", "System Admin");
        usersDTO = new ArrayList<>() {
            {
                add(new UserTestDTO("+351", "962844407", "Test123*", "Filipe Santos"));
                add(new UserTestDTO("+351", "927030780", "Test123*", "Bruno Santos"));
            }
        };
        usersDTO.sort(Comparator.comparing(UserTestDTO::getId));

        usersUpdateDTO = new ArrayList<>() {
            {
                add(new UserDTO().initName("Filipe Miguel Pinho Santos"));
                add(new UserDTO().initName("Bruno Vicente dos Santos"));
            }
        };
        usersUpdateDTO.sort(Comparator.comparing(UserDTO::getId));

        locations = new ArrayList<>() {
            {
                add(new LocationDTO(37.785834, -122.406417, "Powell St", "United States", "San Francisco", "",
                        false));
                add(new LocationDTO(38.67068454237639, -9.165591813983495, "Almada", "Portugal", "Setubal", "",
                        false));
            }
        };
    }

}
