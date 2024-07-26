package com.teamsantos.easybarber.security.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.entities.AuthUser;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String mobileInformation) throws UsernameNotFoundException {
        Optional<AuthUser> authUserOptional = userRepository.findByMobileInformationAuth(mobileInformation);
        AuthUser user = authUserOptional.orElseThrow(UserNotFoundException::new);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (user.isEmployee()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getMobileInformation(),
                user.getPassword(),
                authorities);
        return userDetails;
    }
}
