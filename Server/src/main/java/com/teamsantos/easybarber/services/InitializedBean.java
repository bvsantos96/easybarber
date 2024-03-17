package com.teamsantos.easybarber.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.entities.UserType;
import com.teamsantos.easybarber.repositories.UserTypeRepository;

import jakarta.annotation.PostConstruct;

@Service
public class InitializedBean implements ApplicationListener<ApplicationReadyEvent> {
    public enum UserTypes {
        EMPLOYEE, CLIENT
    }

    @Autowired
    private UserTypeRepository userTypeRepository;

    private static Map<String, Long> userTypes;

    public void init() {
        userTypes = userTypeRepository.findAll().stream()
                .collect(Collectors.toMap(usertype -> usertype.getUserType().toUpperCase(), UserType::getId));
    }

    public static Long getUserType(UserTypes userType) {
        if (userTypes == null || userTypes.isEmpty()) {
            throw new RuntimeException("UserTypes not initialized");
        }
        return userTypes.get(userType.toString().toUpperCase());
    }

    public static boolean isEmployee(User user) {
        if (userTypes == null || userTypes.isEmpty()) {
            throw new RuntimeException("UserTypes not initialized");
        }
        return user.getUserTypeId() == userTypes.get(UserTypes.EMPLOYEE);
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        init();
        if (userTypes == null || userTypes.isEmpty()) {
            System.err.println("UserTypes not initialized");
        }
    }
}
