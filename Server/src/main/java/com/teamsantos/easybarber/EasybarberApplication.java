package com.teamsantos.easybarber;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.teamsantos.easybarber.security.utils.JwtUtils;
import com.teamsantos.easybarber.utils.Utils;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "EasyBarber", version = "1.0", description = "EasyBarber API"))
public class EasybarberApplication {
    @Bean
    public ModelMapper modelMapper() {
        return Utils.createModelMapper();
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    public static void main(String[] args) {
        SpringApplication.run(EasybarberApplication.class, args);
    }
}
