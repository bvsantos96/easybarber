package com.teamsantos.easybarber.controllers;

import com.teamsantos.easybarber.services.MessagingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.teamsantos.easybarber.DTO.SmsDTO;

@Controller
public class MessagingController {

    private static final Logger logger = LoggerFactory.getLogger(MessagingController.class);
    private final MessagingService messagingService;

    @Value("${twilio.sender-id}")
    private String senderId;

    @Autowired
    public MessagingController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @PostMapping("/sms")
    public ResponseEntity<String> sendSmsTest(@RequestBody SmsDTO sms) {
        return sendMessage(sms.getPhoneNr(), sms.getBody());
    }

    @PostMapping("/sms/confirmation")
    public ResponseEntity<String> sendMobileConfirmationMessage(@RequestBody SmsDTO sms) {
        try {
            final String code = messagingService.generateCode();
            messagingService.saveVerificationCode(sms.getPhoneNr(), code);
            return sendMessage(sms.getPhoneNr(), messagingService.verificationCodeBodyMessage(code));
        } catch (Exception e) {
            logger.error("Failed to send confirmation message: " + e.getMessage(), e);
            return new ResponseEntity<>("Failed to send confirmation message", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sms/confirm")
    public ResponseEntity<String> confirmMobileCode(@RequestBody SmsDTO sms) {
        try {
            boolean isVerified = messagingService.verifyCode(sms.getPhoneNr(), sms.getConfirmationCode());
            return new ResponseEntity<>("Code confirmed: " + isVerified, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to confirm code: " + e.getMessage(), e);
            return new ResponseEntity<>("Failed to confirm code", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<String> sendMessage(String to, String body) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(senderId),
                    body
            ).create();
            logger.info("Message sent successfully: " + message.getSid());
            return new ResponseEntity<>("Message sent successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to send message: " + e.getMessage(), e);
            return new ResponseEntity<>("Failed to send message", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}