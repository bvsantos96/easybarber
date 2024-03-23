package com.teamsantos.easybarber.security.services;

import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {
    private UserRepository userRepository;
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
