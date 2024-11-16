package com.teamsantos.easybarber.schedulers;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
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

    @Value("${scheduler.appointments.reminder.enabled}")
    private boolean isSchedulerEnabled;
    
    @Autowired
    public AppointmentsScheduler(MessagingService messagingService, AppointmentService appointmentService){
        this.messagingService = messagingService;
        this.appointmentService = appointmentService;
    }

    @Scheduled(cron = "${scheduler.appointments.reminder}")
    public void sendAppointmentReminders() {
        if (!isSchedulerEnabled) {
            log.info("Appointment reminders scheduler is disabled in the current environment.");
            return;
        }
        log.info("Running appointment reminder scheduler...");
        try {
            appointmentService.getNextDayAppointmentsNotReminded().forEach(this::processAppointmentReminders);
        } catch (Exception e) {
            log.error("Failed to send appointment reminders", e);
        }
    }

    @Async("asyncTaskExecutor")
    public void processAppointmentReminders(AppointmentReminderDTO appointment) {
        try {
            String template = String.format(
                    "Dear %s, this is a reminder of your upcoming appointment at %s with %s. "
                    + "Your appointment is scheduled for %s at %s. If you need to reschedule, please contact us. "
                    + "We look forward to seeing you!",
                    appointment.getUserName(),
                    appointment.getEstablishmentName(),
                    appointment.getEmployeeName(),
                    appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                    appointment.getAppointmentTime().format(DateTimeFormatter.ofPattern("hh:mm a"))
            );
            messagingService.sendMessage(appointment.getMobileInformation(), template);
            appointmentService.setAppointmentAsReminded(appointment.getAppointmentID());
        } catch (Exception e) {
            log.error("Failed to send appointment reminders", e);
        }

    }
}
