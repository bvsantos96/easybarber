package com.teamsantos.easybarber.security;

import com.teamsantos.easybarber.security.filters.JwtAuthenticationFilter;
import com.teamsantos.easybarber.security.services.UserDetailsServiceImpl;
import com.teamsantos.easybarber.security.utils.JwtUtils;
import com.teamsantos.easybarber.security.utils.PasswordEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurity {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // TODO: See what this is and configure it properly
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/hello").permitAll()
                        .anyRequest().authenticated())
                .cors(cors -> cors.disable())
                // TODO: update this to use cors properly (use expo url as allowed origin)
                // .cors(cors -> cors.configurationSource(request -> {
                // var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                // corsConfiguration.setAllowedOrigins(java.util.List.of("http://localhost:3000",
                // "http:192.168.1.225:3000", "http://127.0.0.1:3000"));
                // corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT",
                // "DELETE", "OPTIONS"));
                // corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                // return corsConfiguration;
                // }))
                // .httpBasic(Customizer.withDefaults())
                .httpBasic(httpBasic -> httpBasic.disable())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userDetailsService, jwtUtils);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoding.getPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }
}
