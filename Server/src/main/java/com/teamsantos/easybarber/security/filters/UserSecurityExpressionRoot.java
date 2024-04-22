package com.teamsantos.easybarber.security.filters;

import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.services.UserTypeService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

public class UserSecurityExpressionRoot extends SecurityExpressionRoot {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public UserSecurityExpressionRoot(Authentication authentication, UserRepository userRepository,
            EmployeeRepository employeeRepository) {
        super(authentication);
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    public boolean isEmployee() {
        User user = userRepository.findByMobileInformation(getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new);
        return UserTypeService.isEmployee(user) && employeeRepository.existsByUserId(user.getId());
    }

    public boolean isUser(Long userId) {
        Long authId = userRepository.findByMobileInformation(getAuthentication().getName())
                .orElseThrow(UserNotFoundException::new).getId();
        return userId.equals(authId);
    }
}
