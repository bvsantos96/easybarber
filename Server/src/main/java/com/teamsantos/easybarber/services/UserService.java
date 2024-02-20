package com.teamsantos.easybarber.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.DTO.UserDTO;
import com.teamsantos.easybarber.DTO.UsersDTO;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public UsersDTO getAllUsers(){
        List<User> users = userRepository.findAll();
        //List<UserDTO> userDTOs = users.stream()

        return null;

    }

    public UserDTO createUser(){
        return null;

    }

    public UserDTO updateUser(){
        return null;
       
    }

    public void deleteUser(){

    }
}
