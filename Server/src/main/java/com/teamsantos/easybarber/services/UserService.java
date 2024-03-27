package com.teamsantos.easybarber.services;

import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.DTO.UserDTO;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.security.utils.JwtUtils;
import com.teamsantos.easybarber.security.utils.PasswordEncoding;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final JwtUtils jwtUtils;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.jwtUtils = jwtUtils;
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::userEntityToDTO)
                .collect(Collectors.toList());
    }

    public String loginUser(UserCreateDTO userCreateDTO) {
        Optional<User> user = userRepository.findByMobileInformation(userCreateDTO.getMobileInformation());
        if (user.isPresent())
            if (PasswordEncoding.getPasswordEncoder().matches(userCreateDTO.getPassword(), user.get().getPassword())) {
                return jwtUtils.generateToken(user.get().getMobileInformation());
            } else {
                throw new IllegalArgumentException("Password is incorrect");
            }
        else {
            throw new IllegalArgumentException("User was not found");
        }
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO) throws Exception {
        return createUser(userCreateDTO, false);
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO, boolean isEmployee) throws Exception {
        userCreateDTO.setPassword(PasswordEncoding.encode(userCreateDTO.getPassword()));
        User user = modelMapper.map(userCreateDTO, User.class);
        if (user != null) {
            try {
                Optional<User> oUser = userRepository.findByMobileInformation(user.getMobileInformation());
                if (oUser.isPresent()) {
                    user = oUser.get();
                    if (!isEmployee || UserTypeService.isEmployee(user) || user.equalsIgnoreEmptyValues(userCreateDTO))
                        throw new UserAlreadyExistsException();
                }
            } catch (Exception e) {
                throw new UserAlreadyExistsException();
            }
            user.setUserTypeId(UserTypeService
                    .getUserType(isEmployee ? UserTypeService.UserTypes.EMPLOYEE : UserTypeService.UserTypes.CLIENT));
            userRepository.save(user);
            return modelMapper.map(user, UserDTO.class);
        } else
            throw new IllegalArgumentException("User cannot be null");

    }

    public UserDTO updateUser(UserCreateDTO userCreateDTO) throws Exception {
        /*
         * Optional<User> optionalUser = userRepository.findById(userId);
         * if (optionalUser.isPresent()) {
         * User user = optionalUser.get();
         * user.setPassword(userCreateDTO.getPassword());
         * user.setCountryMobile(userCreateDTO.getCountryMobile());
         * user.setMobile(userCreateDTO.getMobile());
         * userRepository.save(user);
         * return modelMapper.map(user, UserDTO.class);
         * }
         * else{
         * throw new UserNotFoundException("User was not founded");
         * }
         */
        return null;
    }

    public void deleteUser(UserCreateDTO user) {

    }

    private UserDTO userEntityToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User getUser(Principal principal) {
        return userRepository.findByMobileInformation(principal.getName())
                .map((element) -> modelMapper.map(element, User.class))
                .orElseThrow(UserNotFoundException::new);
    }

    public Long getUserId(Principal principal) {
        return userRepository.getIdByMobileInformation(principal.getName()).orElseThrow(UserNotFoundException::new);
    }

    public boolean userChangePermissions(Principal principal, String mobileInformation) {
        return principal.getName().equals(mobileInformation);
    }
}
