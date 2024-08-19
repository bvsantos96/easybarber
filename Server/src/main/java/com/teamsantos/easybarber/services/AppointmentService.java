package com.teamsantos.easybarber.services;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.utils.Utils;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.repositories.AppointmentRepository;
import com.teamsantos.easybarber.DTO.AppointmentDTO;
import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.filters.AppointmentFilter;
import com.teamsantos.easybarber.entities.Appointment;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.utils.Pair;

@Service
public class AppointmentService {
    private final UserService userService;
    private final EstablishmentService establishmentService;
    private final AppointmentRepository appointmentRepository;
    private final SchedulesService scheduleService;

    @Autowired
    public AppointmentService(UserService userService, EstablishmentService establishmentService,
            AppointmentRepository appointmentRepository, SchedulesService scheduleService) {
        this.userService = userService;
        this.establishmentService = establishmentService;
        this.appointmentRepository = appointmentRepository;
        this.scheduleService = scheduleService;
    }

    public Pair<Long, String> create(AppointmentDTO appointmentDTO, Principal principal) {
        Pair<Long, String> result = new Pair<>(null, "");
        try {
            // TODO: Is an establishment required? Or can employees be independent?
            if (appointmentDTO.getEstablishmentId() == null) {
                throw new IllegalArgumentException("An appointment must be associated with an establishment");
            }
            if (appointmentDTO.getDate() == null) {
                throw new IllegalArgumentException("Appointment date must not be null");
            }
            if (appointmentDTO.getTime() == null) {
                throw new IllegalArgumentException("Appointment time must not be null");
            }
            if (appointmentDTO.getDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Appointment date must be in the future");
            } else if (appointmentDTO.getDate().isEqual(LocalDate.now())
                    && appointmentDTO.getTime().isBefore(LocalTime.now())) {
                throw new IllegalArgumentException("Appointment time must be in the future");
            }
            User user = userService.getUser(principal);
            Establishment establishment = establishmentService.getEstablishment(appointmentDTO.getEstablishmentId());
            if (appointmentDTO.getEmployeeId() == null) {
                throw new IllegalArgumentException("An appointment must be associated with an employee");
            } else {
                if (!establishmentService.isStaff(appointmentDTO.getEstablishmentId(),
                        appointmentDTO.getEmployeeId())) {
                    throw new IllegalArgumentException("Employee is not associated with the establishment");
                }
            }
            Employee employee = userService.getEmployee(appointmentDTO.getEmployeeId());
            if (appointmentDTO.getUserId() == null) {
                if (appointmentDTO.getNonRegisteredUser() == null) {
                    throw new IllegalArgumentException("An appointment must be associated with a user");
                } else {
                    if (user.getId() == employee.getUser().getId()) {
                        appointmentDTO.setUserId(user.getId());
                    }
                }
            }
            if (user.getId() != appointmentDTO.getUserId()) {
                throw new IllegalArgumentException(
                        "You do not have permission to create an appointment for another user");
            }
            if (appointmentDTO.getServiceId() == null) {
                throw new IllegalArgumentException("An appointment must be associated with a service");
            }
            com.teamsantos.easybarber.entities.EstablishmentService service = establishmentService
                    .getEstablishmentService(appointmentDTO.getEstablishmentId(), appointmentDTO.getServiceId());
            if (!scheduleService.isAppointmentDateTimeValid(appointmentDTO, service.getService().getDuration())) {
                throw new IllegalArgumentException("Appointment date must be within the employee's schedule");
            }
            result.setFirst(appointmentRepository.save(appointmentDTO.toEntity(user, employee, establishment, service))
                    .getId());
        } catch (Exception e) {
            result.setSecond(e.getMessage());
        }
        return result;
    }

    public BasePageDTO<AppointmentDTO> listAppointment(AppointmentFilter filter, Pageable pageable) {
        return new BasePageDTO<>(appointmentRepository.findAll(filter.getSpecification(), pageable)
                .map((element) -> Utils.getModelMapper().map(element, AppointmentDTO.class)));
    }

    public void cancel(long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        if (appointment.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("You cannot cancel an appointment that has already happened");
        }
        appointment.setActive(false);
        appointmentRepository.save(appointment);
    }

    public void confirm(long id, Principal principal) {
        long employeeId = userService.getEmployee(principal).getId();
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        if (appointment.getEmployee().getId() != employeeId) {
            throw new IllegalArgumentException("You do not have permission to confirm this appointment");
        }
        appointment.setConfirmed(true);
        appointmentRepository.save(appointment);
    }
}
