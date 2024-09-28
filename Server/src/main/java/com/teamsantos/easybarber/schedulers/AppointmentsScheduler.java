package com.teamsantos.easybarber.schedulers;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.teamsantos.easybarber.DTO.appointment.AppointmentReminderDTO;
import com.teamsantos.easybarber.services.AppointmentService;
import com.teamsantos.easybarber.services.MessagingService;

@Component
public class AppointmentsScheduler {
    private static final Logger log = LoggerFactory.getLogger(AppointmentsScheduler.class);
    private final MessagingService messagingService;
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentsScheduler(MessagingService messagingService, AppointmentService appointmentService){
        this.messagingService = messagingService;
        this.appointmentService = appointmentService;
    }

    @Scheduled(cron = "${scheduler.appointments.reminder}")
    public void sendAppointmentReminders() {
        log.info("Running appointment reminder scheduler...");
        try {
            List<AppointmentReminderDTO> appointmentsToRemind = appointmentService.getNextDayAppointmentsNotReminded();
            if(appointmentsToRemind.isEmpty()){
                log.info("No appointments to remind");
            }
            for (AppointmentReminderDTO appointment : appointmentsToRemind) {
                String messageBody = String.format(
                        "Dear %s, this is a reminder of your upcoming appointment at %s with %s. "
                        + "Your appointment is scheduled for %s at %s. If you need to reschedule, please contact us. "
                        + "We look forward to seeing you!",
                        appointment.getUserName(),
                        appointment.getEstablishmentName(),
                        appointment.getEmployeeName(),
                        appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                        appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("hh:mm a"))
                );
                
                // Send the message to the user's mobile phone
                messagingService.sendMessage(appointment.getMobileInformation(), messageBody);
                appointmentService.setAppointmentAsReminded(appointment.getAppointmentID());
            }
        } catch (Exception e) {
            log.error("Failed to send appointment reminders", e);
        }
    }
}
