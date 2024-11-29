package com.teamsantos.easybarber.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.BaseTypedResponseDTO;
import com.teamsantos.easybarber.DTO.sms.RequestConfirmationCode;
import com.teamsantos.easybarber.DTO.sms.SmsDTO;
import com.teamsantos.easybarber.exceptions.ExceptionWithValue;
import com.teamsantos.easybarber.services.MessagingService;

@RestController
public class MessagingController {

    private static final Logger logger = LoggerFactory.getLogger(MessagingController.class);
    private final MessagingService messagingService;

    @Autowired
    public MessagingController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    public ResponseEntity<BaseTypedResponseDTO<Long>> sendMobileConfirmationMessage(
            @RequestBody RequestConfirmationCode sms,
            final MessagingService.RequestType type, final String errorMessage) {
        BaseTypedResponseDTO<Long> response = new BaseTypedResponseDTO<>();
        try {
            final String code = messagingService.generateCode();
            Long requestBlockUntil = messagingService.saveVerificationCode(sms.getPhoneCountryCode() + sms.getPhoneNr(),
                    code);
            messagingService.verificationCodeMessage(code, sms, type);
            response.setValue(requestBlockUntil);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ExceptionWithValue e) {
            logger.error("Failed to send confirmation message: " + e.getMessage());
            response.setResponseMessage(e.getMessage());
            response.setValue(e.getValue(Long.class));
            return new ResponseEntity<>(response, HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        } catch (Exception e) {
            logger.error("Failed to send confirmation message: " + e.getMessage());
            response.setResponseMessage("Phone number not registered");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sms/confirmation")
    public ResponseEntity<BaseTypedResponseDTO<Long>> sendMobileConfirmationMessage(
            @RequestBody RequestConfirmationCode sms) {
        return sendMobileConfirmationMessage(sms, MessagingService.RequestType.CONFIRMATION,
                "Phone number not registered");
    }

    @PostMapping("/sms/resetpwd")
    public ResponseEntity<BaseTypedResponseDTO<Long>> sendResetPwdConfirmationMessage(
            @RequestBody RequestConfirmationCode sms) {
        return sendMobileConfirmationMessage(sms, MessagingService.RequestType.RESET_PWD,
                "Phone number not registered");
    }

    @PostMapping("/sms/confirm")
    public ResponseEntity<BaseResponseDTO> confirmMobileCode(@RequestBody SmsDTO sms) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            messagingService.verifyCode(sms.getPhoneNr(), sms.getConfirmationCode(), false);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to confirm code: " + e.getMessage());
            response.setResponseMessage("Failed to confirm code");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
