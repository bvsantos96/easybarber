package com.teamsantos.easybarber.security;

import com.teamsantos.easybarber.security.filters.JwtAuthenticationFilter;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.security.services.UserDetailsServiceImpl;
import com.teamsantos.easybarber.security.utils.JwtUtils;
import com.teamsantos.easybarber.security.utils.PasswordEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ApplicationSecurity {

    private final UserDetailsServiceImpl userDetailsService;
    private final PrePermissionEvaluator prePermissionEvaluator;
    private final JwtUtils jwtUtils;

    @Autowired
    public ApplicationSecurity(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils,
            PrePermissionEvaluator establishmentPermissionEvaluator) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
        this.prePermissionEvaluator = establishmentPermissionEvaluator;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // TODO: See what this is and configure it properly
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,
                                "/register",
                                "/login",
                                "/employee")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/hello",
                                "/establishment/list",
                                "/employee/services",
                                "/employee/establishments",
                                "/employee/{id}/establishments",
                                "/establishment/{id}",
                                "/establishment/list",
                                "/establishment/{id}/services",
                                "/establishment/{id}/employees",
                                "/service/list")
                        .permitAll()
                        .anyRequest().authenticated())
                .cors(AbstractHttpConfigurer::disable)
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
                .httpBasic(AbstractHttpConfigurer::disable)
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

    @Bean
    public MethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(prePermissionEvaluator);
        return expressionHandler;
    }
}
