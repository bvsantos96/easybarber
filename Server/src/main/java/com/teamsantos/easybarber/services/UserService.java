package com.teamsantos.easybarber.services;

import java.security.Principal;
import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.EmployeeCreateDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.DTO.UserDTO;
import com.teamsantos.easybarber.DTO.UserSignInDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.entities.UserType;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.EstablishmentRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.security.utils.JwtUtils;
import com.teamsantos.easybarber.security.utils.PasswordEncoding;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.utils.PageDTO;

import jakarta.persistence.EntityManager;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final EstablishmentRepository establishmentRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    private final EntityManager entityManager;

    @Autowired
    public UserService(UserRepository userRepository,
            EmployeeRepository employeeRepository,
            EstablishmentRepository establishmentRepository,
            ModelMapper modelMapper, JwtUtils jwtUtils, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.establishmentRepository = establishmentRepository;
        this.modelMapper = modelMapper;
        this.jwtUtils = jwtUtils;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return PageDTO.toDTO(modelMapper, userRepository.findAll(pageable), UserDTO.class, pageable);
    }

    @Transactional(readOnly = true)
    public String loginUser(UserCreateDTO userCreateDTO) {
        UserSignInDTO user = userRepository
                .findUserSignInByMobileInformation(userCreateDTO.getMobileInformation())
                .orElseThrow(UserNotFoundException::new);
        if (PasswordEncoding.getPasswordEncoder().matches(userCreateDTO.getPassword(), user.getPassword())) {
            return jwtUtils.generateToken(user.getId(), user.getEmployeeId(),
                    UserTypeService.getRoles(user.getUserTypeIds()));
        } else {
            throw new IllegalArgumentException("Password is incorrect");
        }
    }

    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO) throws Exception {
        return createUser(userCreateDTO, false);
    }

    private void createEmployee(EmployeeCreateDTO employeeDTO, long userId) throws UserAlreadyExistsException {
        if (employeeRepository.existsByUserId(userId))
            throw new UserAlreadyExistsException();
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        employee.setEnabled(true);
        employee.setUser(entityManager.getReference(User.class, userId));
        employeeRepository.save(employee);
    }

    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO, boolean isEmployee) throws Exception {
        userCreateDTO.setPassword(PasswordEncoding.encode(userCreateDTO.getPassword()));
        User user = modelMapper.map(userCreateDTO, User.class);
        if (user != null) {
            try {
                if (userRepository.existsByMobileInformation(user.getMobileInformation())) {
                    if (!isEmployee || employeeRepository.existsByUserId(user.getId())) {
                        throw new UserAlreadyExistsException();
                    }
                }
            } catch (Exception e) {
                throw new UserAlreadyExistsException();
            }
            user.setUserTypeId(Collections.singleton(entityManager.getReference(UserType.class, UserTypeService
                    .getUserType(isEmployee ? UserTypeService.UserTypes.EMPLOYEE : UserTypeService.UserTypes.CLIENT))));
            user = userRepository.save(user);
            if (isEmployee)
                createEmployee((EmployeeCreateDTO) userCreateDTO, user.getId());
            return modelMapper.map(user, UserDTO.class);
        } else
            throw new IllegalArgumentException("User cannot be null");

    }

    @Transactional
    public void deleteUser(Long id) {
        if (employeeRepository.existsByUserId(id))
            employeeRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(UserCreateDTO userCreateDTO) {
        User oldUser = userRepository.findById(UserContext.getUserId()).orElseThrow(UserNotFoundException::new);
        oldUser.updateNonNullValues(userCreateDTO);
        userRepository.save(oldUser);
    }

    @Transactional(readOnly = true)
    public UserDTO getUser() {
        return modelMapper.map(userRepository.findById(UserContext.getUserId()), UserDTO.class);
    }

    @Transactional(readOnly = true)
    public boolean userChangePermissions(Principal principal, String mobileInformation) {
        return principal.getName().equals(mobileInformation);
    }

    @Transactional
    public Page<EstablishmentDTO> getEstablishments(boolean admin, Pageable pageable) {
        return getEstablishments(UserContext.getUserId(), admin, pageable);
    }

    @Transactional
    public Page<EstablishmentDTO> getEstablishments(Long id, boolean admin, Pageable pageable) {
        return PageDTO.toDTO(modelMapper, establishmentRepository.findEstablishmentsByEmployeeId(id, admin, pageable),
                EstablishmentDTO.class, pageable);
    }

    @Transactional
    public Page<UserDTO> getAllUsersByType(String userType, Pageable pageable) {
        return userRepository
                .findByUserTypeId(UserTypeService.getUserType(userType), pageable)
                .map((element) -> modelMapper.map(element, UserDTO.class));
    }
}
