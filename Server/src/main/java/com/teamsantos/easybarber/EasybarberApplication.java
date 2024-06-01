package com.teamsantos.easybarber;

import com.teamsantos.easybarber.security.utils.JwtUtils;
import com.teamsantos.easybarber.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
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
