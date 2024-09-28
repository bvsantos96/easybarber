package com.teamsantos.easybarber.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.entities.VerificationCode;
import com.teamsantos.easybarber.repositories.VerificationCodeRepository;

@Service
public class MessagingService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    public String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void saveVerificationCode(String phoneNumber, String code) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setPhoneNumber(phoneNumber);
        verificationCode.setCode(code);
        verificationCodeRepository.save(verificationCode);
    }

    public boolean verifyCode(String phoneNumber, String code) {
        VerificationCode verificationCode = verificationCodeRepository.findById(phoneNumber).orElseThrow();
        System.out.println("Code sent:" + code);
        System.out.println("Phone number sent:" + phoneNumber);
        System.out.println("Code in db:" + verificationCode.getCode());
        System.out.println("phone in db:" + verificationCode.getPhoneNumber());
        return verificationCode != null && verificationCode.getCode().equals(code);
    }

    public String verificationCodeBodyMessage(String code) {
        return "Welcome to TeamSantos. Your confirmation code is: " + code;
    }
}
