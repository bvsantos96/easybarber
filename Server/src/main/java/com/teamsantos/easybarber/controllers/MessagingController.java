package com.teamsantos.easybarber.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.sms.RequestConfirmationCode;
import com.teamsantos.easybarber.DTO.sms.SmsDTO;
import com.teamsantos.easybarber.services.MessagingService;

@RestController
public class MessagingController {

    private static final Logger logger = LoggerFactory.getLogger(MessagingController.class);
    private final MessagingService messagingService;

    @Autowired
    public MessagingController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @GetMapping("/sms/test")
    public ResponseEntity<String> test() {
            return new ResponseEntity<>("it worked eheh", HttpStatus.OK);
    }

    @PostMapping("/sms/confirmation")
    public ResponseEntity<BaseResponseDTO> sendMobileConfirmationMessage(@RequestBody RequestConfirmationCode sms) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            final String code = messagingService.generateCode();
            messagingService.saveVerificationCode(sms.getPhoneNr(), code);
            messagingService.sendMessage(sms.getPhoneNr(), messagingService.verificationCodeBodyMessage(code));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to send confirmation message: " + e.getMessage(), e);
            response.setResponseMessage("Failed to send confirmation message");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sms/resetpwd")
    public ResponseEntity<BaseResponseDTO> sendResetPwdConfirmationMessage(@RequestBody RequestConfirmationCode sms) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            final String code = messagingService.generateCode();
            messagingService.saveVerificationCode(sms.getPhoneNr(), code);
            messagingService.sendMessage(sms.getPhoneNr(), messagingService.pwdRecoveryCodeBodyMessage(code));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to send confirmation message: " + e.getMessage(), e);
            response.setResponseMessage("Failed to send confirmation message");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sms/confirm")
    public ResponseEntity<BaseResponseDTO> confirmMobileCode(@RequestBody SmsDTO sms) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            messagingService.verifyCode(sms.getPhoneNr(), sms.getConfirmationCode());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to confirm code: " + e.getMessage(), e);
            response.setResponseMessage("Failed to confirm code");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
