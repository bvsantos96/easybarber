package com.teamsantos.easybarber.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    @Autowired
    public ApplicationSecurity(JwtUtils jwtUtils,
            PrePermissionEvaluator establishmentPermissionEvaluator) {
        this.jwtUtils = jwtUtils;
        this.prePermissionEvaluator = establishmentPermissionEvaluator;
    }

    public static final List<String> ALLOWED_POST_PATHS = List.of(
            "/v3/api-docs/**",
            "/register",
            "/registerAdmin",
            "/login",
            "/employee",
            "/sms/**",
            "/pwd/reset");

    public static final List<String> ALLOWED_PUT_PATHS = List.of(
            "/v3/api-docs/**",
            "/pwd/reset");

    public static final List<String> ALLOWED_GET_PATHS = List.of(
            "/establishment/image/**",
            "/createheavydb",
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
            "/establishment/{id}/servicetypes",
            "/establishment/{id}/services",
            "/establishment/{id}/services/list",
            "/establishment/{id}/employees",
            "/establishment/{establishmentId}/service/{serviceId}/employees",
            "/service/list",
            "/service/types",
            "/icons/**",
            "/schedules/**",
            "/sms/**",
            "/establishment/image/**");

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // TODO: See what this is and configure it properly
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, ALLOWED_POST_PATHS.toArray(String[]::new))
                        .permitAll()
                        .requestMatchers(HttpMethod.PUT, ALLOWED_PUT_PATHS.toArray(String[]::new))
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, ALLOWED_GET_PATHS.toArray(String[]::new))
                        .permitAll()
                        .anyRequest().authenticated())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "access_token"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
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
