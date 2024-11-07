package com.teamsantos.easybarber.DTO.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

import com.teamsantos.easybarber.DTO.BaseDTO;
import com.teamsantos.easybarber.entities.Appointment;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.EstablishmentService;
import com.teamsantos.easybarber.entities.User;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO extends BaseDTO {
    private Long userId;
    private Long establishmentStaffId;
    private Long employeeId;
    private Long establishmentId;
    private Long establishmentServiceId;
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

    public AppointmentDTO(Long userId, Long employeeId, Long establishmentId, Long serviceId,
            String description, String nonRegisteredUser, LocalDate date, LocalTime time) {
        this.userId = userId;
        this.employeeId = employeeId;
        this.establishmentId = establishmentId;
        this.serviceId = serviceId;
        this.description = description;
        this.nonRegisteredUser = nonRegisteredUser;
        this.date = date;
        this.time = time;
    }

    public AppointmentDTO(Long id, Long userId, Long employeeId, Long establishmentId, Long serviceId,
            String description, String nonRegisteredUser, LocalDate date, LocalTime time) {
        super(id);
        this.userId = userId;
        this.employeeId = employeeId;
        this.establishmentId = establishmentId;
        this.serviceId = serviceId;
        this.description = description;
        this.nonRegisteredUser = nonRegisteredUser;
        this.date = date;
        this.time = time;
    }

    public AppointmentDTO(Appointment appointment) {
        super(appointment.getId());
        this.userId = appointment.getUser().getId();
        this.employeeId = appointment.getEmployee().getId();
        this.establishmentId = appointment.getEstablishment().getId();
        this.serviceId = appointment.getService().getId();
        this.description = appointment.getDescription();
        this.nonRegisteredUser = appointment.getNonRegisteredUser();
        this.date = appointment.getDate();
        this.time = appointment.getTime();
    }

    public Appointment toEntity(EntityManager entityManager) {
        Appointment appointment = new Appointment();
        appointment.setUser(entityManager.getReference(User.class, userId));
        appointment.setEmployee(entityManager.getReference(Employee.class, employeeId));
        appointment.setEstablishment(entityManager.getReference(Establishment.class, establishmentId));
        appointment.setService(entityManager.getReference(EstablishmentService.class, serviceId));
        appointment.setDescription(description);
        appointment.setNonRegisteredUser(nonRegisteredUser);
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setActive(true);
        appointment.setConfirmed(true);
        appointment.setReminded(false);
        return appointment;
    }

    public static AppointmentDTO createDummy(int index, long establishmentId, long serviceId, long employeeId) {
        LocalDate date = LocalDate.now();
        if (index > 0) {
            date = date.plusDays(index);
        } else {
            date = date.minusDays(-index);
        }
        return new AppointmentDTO(null, employeeId, establishmentId, serviceId, "description", null,
                date, LocalTime.parse("09:00"));
    }
}
