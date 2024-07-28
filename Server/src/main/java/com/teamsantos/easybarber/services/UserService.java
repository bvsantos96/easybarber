package com.teamsantos.easybarber.services;

import com.teamsantos.easybarber.DTO.EmployeeCreateDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.UserCreateDTO;
import com.teamsantos.easybarber.DTO.UserDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.EstablishmentRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.security.utils.JwtUtils;
import com.teamsantos.easybarber.security.utils.PasswordEncoding;
import com.teamsantos.easybarber.utils.PageDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final EstablishmentRepository establishmentRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserService(UserRepository userRepository, EmployeeRepository employeeRepository,
            EstablishmentRepository establishmentRepository, ModelMapper modelMapper,
            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.establishmentRepository = establishmentRepository;
        this.modelMapper = modelMapper;
        this.jwtUtils = jwtUtils;
    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return PageDTO.toDTO(modelMapper, userRepository.findAll(pageable), UserDTO.class, pageable);
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

    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO) throws Exception {
        return createUser(userCreateDTO, false);
    }

    private void createEmployee(EmployeeCreateDTO employeeDTO, User user) throws UserAlreadyExistsException {
        if (employeeRepository.existsByUserId(user.getId()))
            throw new UserAlreadyExistsException();
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        employee.setEnabled(true);
        employee.setUser(user);
        employeeRepository.save(employee);
    }

    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO, boolean isEmployee) throws Exception {
        userCreateDTO.setPassword(PasswordEncoding.encode(userCreateDTO.getPassword()));
        User user = modelMapper.map(userCreateDTO, User.class);
        if (user != null) {
            try {
                Optional<User> oUser = userRepository.findByMobileInformation(user.getMobileInformation());
                if (oUser.isPresent()) {
                    user = oUser.get();
                    if (!isEmployee
                            || (UserTypeService.isEmployee(user) && employeeRepository.existsByUserId(user.getId())))
                        throw new UserAlreadyExistsException();
                }
            } catch (Exception e) {
                throw new UserAlreadyExistsException();
            }
            user.setUserTypeId(UserTypeService
                    .getUserType(isEmployee ? UserTypeService.UserTypes.EMPLOYEE : UserTypeService.UserTypes.CLIENT));
            user = userRepository.save(user);
            if (isEmployee)
                createEmployee((EmployeeCreateDTO) userCreateDTO, user);
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

    public void updateUser(UserCreateDTO userCreateDTO, Principal principal) {
        User oldUser = getUser(principal);
        oldUser.updateNonNullValues(userCreateDTO);
        userRepository.save(oldUser);
    }

    public User getUser(Principal principal) {
        return userRepository.findByMobileInformation(principal.getName())
                .map((element) -> modelMapper.map(element, User.class))
                .orElseThrow(UserNotFoundException::new);
    }

    public Employee getEmployee(Principal principal) {
        return employeeRepository.findByMobileInformation(principal.getName()).orElseThrow(UserNotFoundException::new);
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public Long getUserId(Principal principal) {
        return userRepository.getIdByMobileInformation(principal.getName()).orElseThrow(UserNotFoundException::new);
    }

    public boolean userChangePermissions(Principal principal, String mobileInformation) {
        return principal.getName().equals(mobileInformation);
    }

    public Page<EstablishmentDTO> getEstablishments(Principal principal, boolean admin, Pageable pageable) {
        return getEstablishments(getEmployee(principal).getId(), admin, pageable);
    }

    public Page<EstablishmentDTO> getEstablishments(Long id, boolean admin, Pageable pageable) {
        return PageDTO.toDTO(modelMapper, establishmentRepository.findEstablishmentsByEmployeeId(id, admin, pageable),
                EstablishmentDTO.class, pageable);
    }

    public Page<UserDTO> getAllUsersByType(String userType, Pageable pageable) {
        return userRepository
                .findByUserTypeId(UserTypeService.getUserType(UserTypeService.getUserType(userType)), pageable)
                .map((element) -> modelMapper.map(element, UserDTO.class));
    }
}
