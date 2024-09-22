package com.teamsantos.easybarber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teamsantos.easybarber.entities.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {
    VerificationCode findByPhoneNumber(String phoneNumber);
}
