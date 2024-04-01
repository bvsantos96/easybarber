package com.teamsantos.easybarber.security.services;

import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String mobileInformation) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByMobileInformation(mobileInformation);

        User user = userOptional.orElseThrow(UserNotFoundException::new);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (employeeRepository.existsByUser(user)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getMobileInformation(),
                user.getPassword(),
                authorities);
    }
}
