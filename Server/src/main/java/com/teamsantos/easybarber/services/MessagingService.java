package com.teamsantos.easybarber.services;

import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Value("${twilio.sender-id}")
    private String senderId;

    private final MessageLoader messageLoader;

    public MessagingService(MessageLoader messageLoader){
        this.messageLoader = messageLoader;
    }

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

    public void verificationCodeMessage(String code, RequestConfirmationCode sms) throws Exception{
        Map<String, String> messages = messageLoader.getMessagesMap().getOrDefault(sms.getPhoneCountryCode(), messageLoader.getMessagesMap().get("en"));
    
        String messageTemplate = messages.get("welcome.message");
    
        if (messageTemplate != null) {
            messageTemplate = messageTemplate.replace("{code}", code);
        }

        sendMessage(sms.getPhoneNr(), messageTemplate);
    }

    public void pwdRecoveryCodeMessage(String code, RequestConfirmationCode sms) throws Exception{
        Map<String, String> messages = messageLoader.getMessagesMap().getOrDefault(sms.getPhoneCountryCode(), messageLoader.getMessagesMap().get("en"));
    
        String messageTemplate = messages.get("password.recovery");
    
        if (messageTemplate != null) {
            messageTemplate = messageTemplate.replace("{code}", code);
        }

        sendMessage(sms.getPhoneNr(), messageTemplate);
    }

    public void appointmentCancelationMessage(Appointment appointment, String reason) throws Exception {
        Map<String, String> messages = messageLoader.getMessagesMap()
            .getOrDefault(appointment.getUser().getCountryMobile(), messageLoader.getMessagesMap().get("en"));
    
        String messageTemplate = reason == null || reason.isBlank() ? 
            messages.get("appointment.cancel.noReason") : 
            messages.get("appointment.cancel.withReason");
    
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
        Message.creator(
            new PhoneNumber(to),
            new PhoneNumber(senderId),
            body).create();
    }

}
