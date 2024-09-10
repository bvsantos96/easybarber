package com.teamsantos.easybarber.services;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.entities.UserType;
import com.teamsantos.easybarber.repositories.UserTypeRepository;

@Service
public class UserTypeService implements ApplicationListener<ApplicationReadyEvent> {
    private final UserTypeRepository userTypeRepository;

    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    // LOCKED = 1, SYSTEM_ADMIN = 2, CLIENT = 3, EMPLOYEE = 4
    public enum UserTypes {
        NONE, LOCKED, SYSTEM_ADMIN, CLIENT, EMPLOYEE
    }

    private static Map<String, Long> userTypes;
    private static Map<Long, String> userTypesById;

    public void init() {
        userTypes = userTypeRepository.findAll().stream()
                .collect(Collectors.toMap(usertype -> usertype.getUserType().toUpperCase(), UserType::getId));
        userTypesById = userTypeRepository.findAll().stream()
                .collect(Collectors.toMap(UserType::getId, UserType::getUserType));
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        init();
        if (userTypes == null || userTypes.isEmpty()) {
            System.err.println("UserTypes not initialized");
        }
    }

    public static boolean[] getRoles(Set<Long> roles) {
        boolean[] result = new boolean[userTypes.size()];
        for (Long role : roles) {
            result[role.intValue()] = true;
        }
        return result;
    }

    public static long getUserType(String type) {
        return userTypes.get(type.toUpperCase());
    }

    public static long getUserType(UserTypes type) {
        return userTypes.get(type.toString().toUpperCase());
    }

    public static Set<String> getUserRoles(Set<Long> userTypeIds) {
        return userTypeIds.stream().map(id -> userTypesById.get(id)).collect(Collectors.toSet());
    }
}
