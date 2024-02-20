package com.teamsantos.easybarber.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int userTypeId;
    private String email;
    private String password;
    private String countryMobile;
    private String mobile;
    private String tokenFacebook;
    private String tokenTwitter;
    private String userToken;
    private LocalDateTime tokenExpiration;
}
