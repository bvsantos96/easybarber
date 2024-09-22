package com.teamsantos.easybarber.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class VerificationCode {

    @Id
    private String phoneNumber;
    private String code;
}
