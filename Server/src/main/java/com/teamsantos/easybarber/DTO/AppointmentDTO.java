package com.teamsantos.easybarber.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

import com.teamsantos.easybarber.entities.Appointment;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.EstablishmentService;
import com.teamsantos.easybarber.entities.User;

import lombok.Data;

@Data
public class AppointmentDTO {
    private Long userId;
    private Long employeeId;
    private Long establishmentId;
    private Long serviceId;
    private String description;
    // TODO: apagar dps de explicar
    // To let barbers schedule appointments for non-registered users
    // p.e. pessoas mais velhas que nao usam a internet
    // ou pessoas que passam la na loja so para agendar e nao querem usar a app
    // sem isto os barbers tinham de criar um user para cada pessoa
    // ou criar uma exception para cada um desses casos e saber o que cada exception
    // significa (que pessoa representa)
    private String nonRegisteredUser;
    private LocalDate date;
    private LocalTime time;

    public Appointment toEntity(User user, Employee employee, Establishment establishment, EstablishmentService service) {
        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setEmployee(employee);
        appointment.setEstablishment(establishment);
        appointment.setService(service);
        appointment.setDescription(description);
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setActive(true);
        appointment.setConfirmed(false);
        return appointment;

    }
}
