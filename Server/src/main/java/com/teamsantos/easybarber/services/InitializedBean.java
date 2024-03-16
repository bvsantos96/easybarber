package com.teamsantos.easybarber.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.entities.UserType;
import com.teamsantos.easybarber.repositories.UserTypeRepository;

import jakarta.annotation.PostConstruct;

@Service
public class InitializedBean {
    public enum UserTypes{
        EMPLOYEE, CLIENT
    }

	@Autowired
    private UserTypeRepository userTypeRepository;

    private static Map<String, Long> userTypes;

    @PostConstruct
    public void init() {
        userTypes = userTypeRepository.findAll().stream().collect(Collectors.toMap(usertype -> usertype.getUserType().toUpperCase(), UserType::getId));
    }

    public static Long getUserType(UserTypes userType) {
        return userTypes.get(userType.toString().toUpperCase());
    }
}
