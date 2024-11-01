package com.teamsantos.easybarber.services;

import java.security.Principal;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.employee.EmployeeCreateDTO;
import com.teamsantos.easybarber.DTO.establishment.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.user.UserCreateDTO;
import com.teamsantos.easybarber.DTO.user.UserDTO;
import com.teamsantos.easybarber.DTO.user.UserSignInDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.entities.UserType;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.security.utils.JwtUtils;
import com.teamsantos.easybarber.security.utils.PasswordEncoding;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.utils.PageDTO;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.EntityManager;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;
    private final EntityManager entityManager;

    @Autowired
    public UserService(UserRepository userRepository,
            EmployeeRepository employeeRepository,
            JwtUtils jwtUtils, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = Utils.getModelMapper();
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
        // TODO: if we get 2 many users this might me moved to a string in the user
        // table so that we can load them faster.
        // This will make the user type change a bit slower but that is not that
        // frequent of a request compared with the login that affects everyuser
        user.setUserTypeIds(userRepository.getAllUserTypes(user.getId()));

        if (PasswordEncoding.getPasswordEncoder().matches(userCreateDTO.getPassword(), user.getPassword())) {
            return jwtUtils.generateToken(user.getId(), user.getEmployeeId(),
                    UserTypeService.getUserRoles(user.getUserTypeIds()));
        } else {
            throw new IllegalArgumentException("Password is incorrect");
        }
    }

    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO) throws Exception {
        return createUser(userCreateDTO, false);
    }

    private Employee createEmployee(EmployeeCreateDTO employeeDTO, long userId) throws UserAlreadyExistsException {
        if (employeeRepository.existsByUserId(userId))
            throw new UserAlreadyExistsException();
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        employee.setEnabled(true);
        employee.setUser(entityManager.getReference(User.class, userId));
        return employeeRepository.save(employee);
    }

    public UserDTO createUser(UserCreateDTO userCreateDTO, boolean isEmployee) throws Exception {
        return createUser(userCreateDTO, isEmployee, false);
    }

    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO, boolean isEmployee, boolean systemAdmin) throws Exception {
        if (!userCreateDTO.isValidNumberString()) {
            throw new IllegalArgumentException("Mobile information is not valid");
        }
        if (!userCreateDTO.isValidPassword()) {
            throw new IllegalArgumentException("Password is not valid");
        }
        userCreateDTO.setPassword(PasswordEncoding.encode(userCreateDTO.getPassword()));
        User user = modelMapper.map(userCreateDTO, User.class);
        if (user != null) {
            try {
                if (userCreateDTO.getId() != null && userRepository.existsById(userCreateDTO.getId())) {
                    userCreateDTO.setId(null);
                }
                if (userRepository.existsByMobileInformation(user.getMobileInformation())) {
                    if (!isEmployee) {
                        throw new UserAlreadyExistsException();
                    } else {
                        user = userRepository.findByMobileInformation(user.getMobileInformation())
                                .orElseThrow(UserAlreadyExistsException::new);
                    }
                }
            } catch (Exception e) {
                throw new UserAlreadyExistsException();
            }

            if (user.getUserTypes() == null || user.getUserTypes().isEmpty()) {
                user.addUserType(entityManager.getReference(UserType.class, UserTypeService
                        .getUserType(UserTypeService.UserTypes.CLIENT)));
            }
            user.addUserType(entityManager.getReference(UserType.class, UserTypeService
                    .getUserType(isEmployee ? UserTypeService.UserTypes.EMPLOYEE : UserTypeService.UserTypes.CLIENT)));
            if (systemAdmin)
                user.addUserType(entityManager.getReference(UserType.class,
                        UserTypeService.getUserType(UserTypeService.UserTypes.SYSTEM_ADMIN)));

            user = userRepository.save(user);
            if (isEmployee) {
                UserDTO ret = new UserDTO();
                ret.setId(createEmployee((EmployeeCreateDTO) userCreateDTO, user.getId()).getId());
                return ret;
            }
            return modelMapper.map(user, UserDTO.class);
        } else
            throw new IllegalArgumentException("User cannot be null");
    }

    @Transactional
    public UserDTO createAdmin(UserCreateDTO userCreateDTO) throws Exception {
        return createUser(userCreateDTO, false, true);
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

    @Transactional(readOnly = true)
    public boolean existsUserByMobileInformation(String mobileInformation) {
        return userRepository.existsByMobileInformation(mobileInformation);
    }

    @Transactional
    public Page<UserDTO> getAllUsersByType(String userType, Pageable pageable) {
        return userRepository
                .findByUserTypeId(UserTypeService.getUserType(userType), pageable)
                .map((element) -> modelMapper.map(element, UserDTO.class));
    }

    @Transactional(readOnly = true)
    public UserDTO getUserByMobileNr(String mobileNr) throws Exception {
        Optional<User> user = userRepository.findByMobileInformation(mobileNr);

        if (user.isEmpty()) {
            throw new Exception("The mobile number doesnt exist");
        }

        return modelMapper.map(user.get(), UserDTO.class);
    }

    @Transactional
    public void changeUserPwd(UserDTO userDTO, String newPwd) throws Exception {
        Optional<User> userOpt = userRepository.findById(userDTO.getId());

        if (userOpt.isEmpty()) {
            throw new Exception("User not found");
        }

        User user = userOpt.get();
        user.setPassword(PasswordEncoding.encode(newPwd));
        userRepository.save(user);
    }

    @Transactional
    public void favorite(Long establishmentId) {
        userRepository.findById(UserContext.getUserId()).ifPresent(user -> {
            user.addFavoriteEstablishment(entityManager.getReference(Establishment.class, establishmentId));
            userRepository.save(user);
        });
    }

    @Transactional
    public void unfavorite(Long establishmentId) {
        userRepository.findById(UserContext.getUserId()).ifPresent(user -> {
            user.getFavoriteEstablishments().removeIf(establishment -> establishment.getId().equals(establishmentId));
            userRepository.save(user);
        });
    }

    @Transactional(readOnly = true)
    public Page<EstablishmentDTO> getFavoriteEstablishments(Long userId, Pageable pageable) {
        return userRepository.findFavoriteEstablishmentsByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(Long establishmentId) {
        return userRepository.findById(UserContext.getUserId()).map(user -> {
            return user.getFavoriteEstablishments().stream()
                    .anyMatch(establishment -> establishment.getId().equals(establishmentId)) ? true : false;
        }).orElse(false);
    }
}
