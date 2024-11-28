package com.teamsantos.easybarber.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode {
    @Id
    private String phoneNumber;
    private String code;
    private int attempts = 0;
    private Long lastAttempt;

    public VerificationCode(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeCode(String code) {
        this.code = code;
        this.attempts++;
        this.lastAttempt = System.currentTimeMillis();
    }
}
