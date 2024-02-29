package com.teamsantos.easybarber.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.DTO.UserDTO;
import com.teamsantos.easybarber.DTO.UsersDTO;
import com.teamsantos.easybarber.Exceptions.UserNotFoundException;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.security.services.PasswordEncoding;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<UserDTO> getAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
                                    .map(this::userEntityToDTO)
                                    .collect(Collectors.toList());

        return userDTOs;
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO) throws Exception{
        userCreateDTO.setPassword(PasswordEncoding.encode(userCreateDTO.getPassword()));
        User user = modelMapper.map(userCreateDTO, User.class);
        if (user != null) {
            userRepository.save(user);
            return modelMapper.map(user, UserDTO.class);
        } else {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    public UserDTO updateUser(UserCreateDTO userCreateDTO) throws Exception{
        /** Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(userCreateDTO.getPassword());
            user.setCountryMobile(userCreateDTO.getCountryMobile());
            user.setMobile(userCreateDTO.getMobile());
            userRepository.save(user);
            return modelMapper.map(user, UserDTO.class);
        }
        else{
            throw new UserNotFoundException("User was not founded");
        }**/
        return null;
    }

    public void deleteUser(UserCreateDTO user){

    }

    private UserDTO userEntityToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
