package com.teamsantos.easybarber.controllers;

import com.teamsantos.easybarber.services.MessagingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.ConfirmCodeDTO;
import com.teamsantos.easybarber.DTO.RequestConfirmationCodeDTO;

@RestController
@RequestMapping("/sms")
public class MessagingController {

    private static final Logger logger = LoggerFactory.getLogger(MessagingController.class);
    private final MessagingService messagingService;

    @Autowired
    public MessagingController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @PostMapping("/confirmation")
    public ResponseEntity<BaseResponseDTO> sendMobileConfirmationMessage(@RequestBody RequestConfirmationCodeDTO requestConfirmationCodeDTO) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            final String code = messagingService.generateCode();
            messagingService.saveVerificationCode(requestConfirmationCodeDTO.getPhoneNr(), code);
            messagingService.sendMessage(requestConfirmationCodeDTO.getPhoneNr(), messagingService.verificationCodeBodyMessage(code));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to send confirmation message: " + e.getMessage(), e);
            response.setResponseMessage("Failed to send confirmation message");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/resetpwd")
    public ResponseEntity<BaseResponseDTO> sendResetPwdMessage(@RequestBody RequestConfirmationCodeDTO requestConfirmationCodeDTO) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            final String code = messagingService.generateCode();
            messagingService.saveVerificationCode(requestConfirmationCodeDTO.getPhoneNr(), code);
            messagingService.sendMessage(requestConfirmationCodeDTO.getPhoneNr(), messagingService.verificationCodePwdReset(code));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {     
            logger.error("Failed to send reset pwd message: " + e.getMessage(), e);
            response.setResponseMessage("Failed to send confirmation message");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<BaseResponseDTO> confirmMobileCode(@RequestBody ConfirmCodeDTO confirmCodeDTO) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            messagingService.verifyCode(confirmCodeDTO.getPhoneNr(), confirmCodeDTO.getConfirmationCode());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to confirm code: " + e.getMessage(), e);
            response.setResponseMessage("Failed to confirm code");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}