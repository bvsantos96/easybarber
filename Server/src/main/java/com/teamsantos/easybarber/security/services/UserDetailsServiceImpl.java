package com.teamsantos.easybarber.security.services;

import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.Exceptions.UserNotFoundException;
import com.teamsantos.easybarber.entities.User;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String mobileInformation) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByMobileInformation(mobileInformation);

        User user = userOptional.orElseThrow(() -> new UserNotFoundException());

        return new org.springframework.security.core.userdetails.User(
                user.getMobileInformation(),
                user.getPassword(),
                Collections.emptyList());
        // Add any additional roles or authorities as needed
        // Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
