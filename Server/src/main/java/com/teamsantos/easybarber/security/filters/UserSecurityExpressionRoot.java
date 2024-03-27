package com.teamsantos.easybarber.security.filters;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.services.UserTypeService;

public class UserSecurityExpressionRoot extends SecurityExpressionRoot {
    private final UserRepository userRepository;

    public UserSecurityExpressionRoot(Authentication authentication, UserRepository userRepository) {
        super(authentication);
        this.userRepository = userRepository;
    }

    public boolean isEmployee() {
        User user = userRepository.findByMobileInformation(getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new);
        return UserTypeService.isEmployee(user);
    }
}
