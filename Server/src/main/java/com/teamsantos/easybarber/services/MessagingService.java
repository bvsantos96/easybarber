package com.teamsantos.easybarber.services;

import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.DTO.sms.RequestConfirmationCode;
import com.teamsantos.easybarber.components.MessageLoader;
import com.teamsantos.easybarber.entities.Appointment;
import com.teamsantos.easybarber.entities.VerificationCode;
import com.teamsantos.easybarber.repositories.VerificationCodeRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class MessagingService {
    @Value("${teamsantos.istest}")
    private boolean isTestContext;
    @Value("${twilio.sender-id}")
    private String senderId;

    private final int ATTEMPTS_BEFORE_RATE_LIMIT = 2;
    private final int RATE_LIMIT = 1;

    public static enum RequestType {
        CONFIRMATION, RESET_PWD
    }

    private final UserService userService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final MessageLoader messageLoader;

    public MessagingService(MessageLoader messageLoader, VerificationCodeRepository verificationCodeRepository,
            UserService userService) {
        this.userService = userService;
        this.verificationCodeRepository = verificationCodeRepository;
        this.messageLoader = messageLoader;
    }

    public String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * Generates a verification code for a phone number
     * 
     * @param phoneNumber
     * @param code
     * @return time in milliseconds until when the code a code can be resent
     */
    public Long saveVerificationCode(String phoneNumber, final String code) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findById(phoneNumber)
                .orElse(new VerificationCode(phoneNumber));
        if (verificationCode.getAttempts() >= ATTEMPTS_BEFORE_RATE_LIMIT) {
            if (verificationCode.getLastAttempt() + RATE_LIMIT * 60000 > System.currentTimeMillis()) {
                return verificationCode.getLastAttempt() + RATE_LIMIT * 60000;
            }
        }
        verificationCode.changeCode(code);
        verificationCodeRepository.save(verificationCode);
        if (verificationCode.getAttempts() >= ATTEMPTS_BEFORE_RATE_LIMIT) {
            return verificationCode.getLastAttempt() + RATE_LIMIT * 60000;
        }
        return 0L;
    }

    public void verifyCode(String phoneNumber, String code) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findById(phoneNumber)
                .orElseThrow(() -> new Exception("Phone number does not have a code assigned"));

        if (verificationCode.getCode().isEmpty()) {
            throw new Exception("Code is empty");
        }

        if (!verificationCode.getCode().isEmpty() && !verificationCode.getCode().equals(code)) {
            throw new Exception("Code does not match");
        }
    }

    public void verificationCodeMessage(String code, RequestConfirmationCode sms, RequestType type) throws Exception {
        if (userService.existsByMobileInformation(sms.getPhoneCountryCode() + sms.getPhoneNr())) {
            if (type == RequestType.CONFIRMATION) {
                throw new Exception("Phone number already exists");
            }
        } else {
            if (type == RequestType.RESET_PWD) {
                throw new Exception("Phone number does not exist");
            }
        }

        Map<String, String> messages = messageLoader.getMessagesMap().getOrDefault(sms.getPhoneCountryCode(),
                messageLoader.getMessagesMap().get("en"));

        String messageTemplate = switch (type) {
            case CONFIRMATION -> messages.get("welcome.message");
            case RESET_PWD -> messages.get("password.recovery");
        };

        if (messageTemplate != null) {
            messageTemplate = messageTemplate.replace("{code}", code);
        }

        sendMessage(String.format("%s%s", sms.getPhoneCountryCode(), sms.getPhoneNr()), messageTemplate);
    }

    public void appointmentCancelationMessage(Appointment appointment, String reason) throws Exception {
        Map<String, String> messages = messageLoader.getMessagesMap()
                .getOrDefault(appointment.getUser().getCountryMobile(), messageLoader.getMessagesMap().get("en"));

        String messageTemplate = reason == null || reason.isBlank() ? messages.get("appointment.cancel.noReason")
                : messages.get("appointment.cancel.withReason");

        if (messageTemplate != null) {
            messageTemplate = messageTemplate.replace("{date}", appointment.getDate().toString())
                    .replace("{time}", appointment.getTime().toString())
                    .replace("{establishment}", appointment.getEstablishment().getName())
                    .replace("{employee}", appointment.getEmployee().getUser().getName());

            if (reason != null && !reason.trim().isEmpty()) {
                messageTemplate = messageTemplate.replace("{reason}", reason);
            }
        }
        sendMessage(appointment.getUser().getMobileInformation(), messageTemplate);
    }

    public void sendMessage(String to, String body) throws Exception {
        if (isTestContext) {
            return;
        }

        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(senderId),
                body).create();
    }

}
