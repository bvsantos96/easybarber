package com.teamsantos.easybarber.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.entities.VerificationCode;
import com.teamsantos.easybarber.repositories.VerificationCodeRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class MessagingService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Value("${twilio.sender-id}")
    private String senderId;

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

    public void verifyCode(String phoneNumber, String code) throws Exception{
        VerificationCode verificationCode = verificationCodeRepository.findById(phoneNumber).orElseThrow( () -> new Exception("Phone number does not have a code assigned"));
        
        if(verificationCode.getCode().isEmpty()){
            throw new Exception("Code is empty");
        }
        
        if(!verificationCode.getCode().isEmpty() && !verificationCode.getCode().equals(code)){
            throw new Exception("Code does not match");
        }
    }

    public String verificationCodeBodyMessage(String code) {
        return "Welcome to TeamSantos. Your confirmation code is: " + code;
    }

    public String pwdRecoveryCodeBodyMessage(String code) {
        return "Ola. Your verification code is: " + code+". Use this code to reset your password. If you didn't request this, please ignore this message.";
    }

    public void sendMessage(String to, String body) throws Exception {
        Message.creator(
            new PhoneNumber(to),
            new PhoneNumber(senderId),
            body).create();
    }

}
