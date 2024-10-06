package com.teamsantos.easybarber.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentCountDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentListDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentReminderDTO;
import com.teamsantos.easybarber.DTO.appointment.CancelAppointmentDTO;
import com.teamsantos.easybarber.DTO.filters.AppointmentFilter;
import com.teamsantos.easybarber.entities.Appointment;
import com.teamsantos.easybarber.repositories.AppointmentRepository;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.utils.Pair;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;

@Service
public class AppointmentService {
    private final EmployeeService employeeService;
    private final EstablishmentService establishmentService;
    private final AppointmentRepository appointmentRepository;
    private final SchedulesService scheduleService;
    private final EntityManager entityManager;
    private final MessagingService messagingService;

    @Autowired
    public AppointmentService(EmployeeService employeeService,
            EstablishmentService establishmentService,
            AppointmentRepository appointmentRepository, SchedulesService scheduleService,
            EntityManager entityManager, MessagingService messagingService) {
        this.employeeService = employeeService;
        this.establishmentService = establishmentService;
        this.appointmentRepository = appointmentRepository;
        this.scheduleService = scheduleService;
        this.entityManager = entityManager;
        this.messagingService = messagingService;
    }

    public Long create(AppointmentDTO appointmentDTO) throws Exception {
        if (appointmentDTO.getId() != null) {
            appointmentDTO.setId(null);
        }
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
        if (appointmentDTO.getEmployeeId() == null) {
            throw new IllegalArgumentException("An appointment must be associated with an employee");
        } else {
            if (!establishmentService.isStaff(appointmentDTO.getEstablishmentId(),
                    appointmentDTO.getEmployeeId())) {
                throw new IllegalArgumentException("Employee is not associated with the establishment");
            }
        }
        if (appointmentDTO.getUserId() == null) {
            if (appointmentDTO.getNonRegisteredUser() == null) {
                appointmentDTO.setUserId(UserContext.getUserId());
            } else {
                if (employeeService.getUserId(appointmentDTO.getEmployeeId()) == UserContext.getUserId()) {
                    appointmentDTO.setUserId(UserContext.getUserId());
                }
            }
        }
        if (UserContext.getUserId() != appointmentDTO.getUserId()) {
            throw new IllegalArgumentException(
                    "You do not have permission to create an appointment for another user");
        }
        if (appointmentDTO.getServiceId() == null) {
            throw new IllegalArgumentException("An appointment must be associated with a service");
        }

        Pair<Long, Integer> _establishmentService = establishmentService.getDurationOfService(
                appointmentDTO.getEstablishmentId(),
                appointmentDTO.getServiceId());
        if (!scheduleService.isAppointmentDateTimeValid(appointmentDTO, _establishmentService.getSecond())) {
            throw new IllegalArgumentException("Appointment date must be within the employee's schedule");
        }
        appointmentDTO.setServiceId(_establishmentService.getFirst());
        return appointmentRepository.save(appointmentDTO.toEntity(entityManager)).getId();
    }

    @Transactional(readOnly = true)
    public BasePageDTO<AppointmentDTO> listAppointment(AppointmentFilter filter, Pageable pageable) {
        return new BasePageDTO<>(appointmentRepository.findAll(filter.getSpecification(), pageable)
                .map((element) -> Utils.getModelMapper().map(element, AppointmentDTO.class)));
    }

    @Transactional(readOnly = true)
    public BasePageDTO<AppointmentListDTO> listAppointmentBase(AppointmentFilter filter, Pageable pageable) {
        if (filter.getUserView() == null || filter.getUserView()) {
            return new BasePageDTO<>(appointmentRepository.findAllToUser(filter, pageable)
                    .map((element) -> Utils.getModelMapper().map(element, AppointmentListDTO.class)));
        }
        return new BasePageDTO<>(appointmentRepository.findAllToEmployee(filter, pageable)
                .map((element) -> Utils.getModelMapper().map(element, AppointmentListDTO.class)));
    }

    @Transactional
    public void cancel(CancelAppointmentDTO cancelAppointmentDTO) throws Exception {
        Appointment appointment = appointmentRepository.findById(cancelAppointmentDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        if (appointment.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("You cannot cancel an appointment that has already happened");
        }

        appointment.setActive(false);
        appointmentRepository.save(appointment);
        messagingService.appointmentCancelationMessage(appointment, cancelAppointmentDTO.getReason());
    }

    @Transactional
    public void confirm(long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        if (appointment.getEmployee().getId() != UserContext.getEmployeeId()) {
            throw new IllegalArgumentException("You do not have permission to confirm this appointment");
        }
        appointment.setConfirmed(true);
        appointmentRepository.save(appointment);
    }

    @Transactional(readOnly = true)
    public List<AppointmentReminderDTO> getNextDayAppointmentsNotReminded() throws Exception {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        return appointmentRepository.findNextDayAppointmentsNotReminded(tomorrow);
    }

    @Transactional
    public void setAppointmentAsReminded(Long appointmentID) throws Exception {
        Appointment appointment = appointmentRepository.findById(appointmentID)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        appointment.setReminded(true);
        appointmentRepository.save(appointment);
    }

    public AppointmentCountDTO countAppointments(boolean userView) {
        Optional<Tuple> count = appointmentRepository.countAppointments(UserContext.getUserId(), userView);
        if (count.isEmpty()) {
            return new AppointmentCountDTO(0, 0);
        }
        return new AppointmentCountDTO(count.get());
    }
}
