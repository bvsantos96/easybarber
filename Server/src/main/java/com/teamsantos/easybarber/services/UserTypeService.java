package com.teamsantos.easybarber.services;

import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.entities.UserType;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.repositories.UserTypeRepository;

@Service
public class UserTypeService implements ApplicationListener<ApplicationReadyEvent> {
    private final UserTypeRepository userTypeRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public UserTypeService(UserTypeRepository userTypeRepository, UserRepository userRepository,
            EmployeeRepository employeeRepository) {
        this.userTypeRepository = userTypeRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    public enum UserTypes {
        EMPLOYEE, CLIENT
    }

    private static Map<String, Long> userTypes;

    public void init() {
        userTypes = userTypeRepository.findAll().stream()
                .collect(Collectors.toMap(usertype -> usertype.getUserType().toUpperCase(), UserType::getId));
    }

    public static UserTypes getUserType(String userType) {
        if (userTypes == null || userTypes.isEmpty()) {
            throw new RuntimeException("UserTypes not initialized");
        }
        return UserTypes.valueOf(userType.toUpperCase());
    }

    public static Long getUserType(UserTypes userType) {
        if (userTypes == null || userTypes.isEmpty()) {
            throw new RuntimeException("UserTypes not initialized");
        }
        return userTypes.get(userType.name().toUpperCase());
    }

    public static boolean isEmployee(User user) {
        if (userTypes == null || userTypes.isEmpty()) {
            throw new RuntimeException("UserTypes not initialized");
        }
        return user.getUserTypeId().equals(userTypes.get(UserTypes.EMPLOYEE.name()));
    }

    /**
     * DEPRECATED: Note this method might no longer be good enough, as it does not
     * check if the user is connected to an employee entity
     * 
     * @param userId the id of the user
     * @return true if the user has user type employee
     */
    public boolean isEmployee(Long userId) {
        if (userTypes == null || userTypes.isEmpty()) {
            throw new RuntimeException("UserTypes not initialized");
        }
        return userRepository.existsByIdAndUserTypeId(userId, getUserType(UserTypes.EMPLOYEE))
                && employeeRepository.existsByUserId(userId);
    }

    public Employee getEmployee(Principal principal) {
        return employeeRepository.findByUserId(userRepository.findByMobileInformation(principal.getName())
                .orElseThrow(UserNotFoundException::new).getId()).orElseThrow(UserNotFoundException::new);
    }

    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        init();
        if (userTypes == null || userTypes.isEmpty()) {
            System.err.println("UserTypes not initialized");
        }
    }
}
