package com.teamsantos.easybarber.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.teamsantos.easybarber.security.filters.UserContextFilter;
import com.teamsantos.easybarber.security.services.PrePermissionEvaluator;
import com.teamsantos.easybarber.security.utils.JwtUtils;
import com.teamsantos.easybarber.security.utils.PasswordEncoding;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ApplicationSecurity {
    private final PrePermissionEvaluator prePermissionEvaluator;
    private final JwtUtils jwtUtils;

    @Autowired
    public ApplicationSecurity(JwtUtils jwtUtils,
            PrePermissionEvaluator establishmentPermissionEvaluator) {
        this.jwtUtils = jwtUtils;
        this.prePermissionEvaluator = establishmentPermissionEvaluator;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // TODO: See what this is and configure it properly
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,
                                "/v3/api-docs/**",
                                "/register",
                                "/registerAdmin",
                                "/login",
                                "/employee",
                                "/sms/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/version",
                                "/establishment/list",
                                "/employee/services",
                                "/employee/establishments",
                                "/employee/{id}/establishments",
                                "/establishment/{id}",
                                "/establishment/list",
                                "/establishment/{id}/services",
                                "/establishment/{id}/employees",
                                "/service/list",
                                "/service/types",
                                "/icons/**",
                                "/schedules/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .cors(Customizer.withDefaults())
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
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public UserContextFilter jwtAuthenticationFilter() {
        return new UserContextFilter(jwtUtils);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoding.getPasswordEncoder();
    }

    @Bean
    public MethodSecurityExpressionHandler expressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(prePermissionEvaluator);
        return expressionHandler;
    }
}
