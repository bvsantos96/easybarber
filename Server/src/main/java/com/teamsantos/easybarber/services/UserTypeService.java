package com.teamsantos.easybarber.services;

import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.entities.UserType;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.repositories.UserTypeRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserTypeService implements ApplicationListener<ApplicationReadyEvent> {
    private final UserTypeRepository userTypeRepository;
    private final UserRepository userRepository;

    public UserTypeService(UserTypeRepository userTypeRepository, UserRepository userRepository) {
        this.userTypeRepository = userTypeRepository;
        this.userRepository = userRepository;
    }

    public enum UserTypes {
        EMPLOYEE, CLIENT
    }

    private static Map<String, Long> userTypes;

    public void init() {
        userTypes = userTypeRepository.findAll().stream()
                .collect(Collectors.toMap(usertype -> usertype.getUserType().toUpperCase(), UserType::getId));
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

    public boolean isEmployee(Long userId) {
        if (userTypes == null || userTypes.isEmpty()) {
            throw new RuntimeException("UserTypes not initialized");
        }
        return userRepository.existsByIdAndUserTypeId(userId, getUserType(UserTypes.EMPLOYEE));
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        init();
        if (userTypes == null || userTypes.isEmpty()) {
            System.err.println("UserTypes not initialized");
        }
    }
}
