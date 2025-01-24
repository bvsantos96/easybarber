package com.teamsantos.easybarber.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentCountDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentListDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentReminderDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentsHashDTO;
import com.teamsantos.easybarber.DTO.appointment.CancelAppointmentDTO;
import com.teamsantos.easybarber.DTO.appointment.FeedbackDTO;
import com.teamsantos.easybarber.DTO.filters.AppointmentFilter;
import com.teamsantos.easybarber.DTO.filters.ProductRequestFilter;
import com.teamsantos.easybarber.DTO.product.ProductRequestsDTO;
import com.teamsantos.easybarber.DTO.service.ServiceDynamicPriceDTO;
import com.teamsantos.easybarber.entities.Appointment;
import com.teamsantos.easybarber.exceptions.GenericNotFoundException;
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

    public Pair<Long, Double> create(AppointmentDTO appointmentDTO) throws Exception {
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
        if (appointmentDTO.getEmployeeId() == null && appointmentDTO.getEstablishmentStaffId() == null) {
            throw new IllegalArgumentException("An appointment must be associated with an employee");
        } else {
            if (appointmentDTO.getEstablishmentStaffId() == null) {
                if (!establishmentService.isStaff(appointmentDTO.getEstablishmentId(),
                        appointmentDTO.getEmployeeId())) {
                    throw new IllegalArgumentException("Employee is not associated with the establishment");
                }
            } else {
                appointmentDTO.setEmployeeId(employeeService.getEmployeeId(appointmentDTO.getEstablishmentStaffId()));
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
            throw new IllegalArgumentException("You do not have permission to create an appointment for another user");
        }

        if (appointmentDTO.getEstablishmentServiceId() != null) {
            appointmentDTO.setServiceId(establishmentService.getServiceId(appointmentDTO.getEstablishmentServiceId()));
        }

        if (appointmentDTO.getServiceId() == null) {
            throw new IllegalArgumentException("An appointment must be associated with a service");
        }

        ServiceDynamicPriceDTO _establishmentService = establishmentService.getDurationAndPriceOfService(
                appointmentDTO.getEstablishmentId(),
                appointmentDTO.getServiceId(),
                appointmentDTO.getEmployeeId(),
                appointmentDTO.getDate().atTime(appointmentDTO.getTime()));

        if (!scheduleService.isAppointmentDateTimeValid(appointmentDTO, _establishmentService.getDuration())) {
            throw new IllegalArgumentException("Appointment date must be within the employee's schedule");
        }
        appointmentDTO.setServiceId(_establishmentService.getId());
        Appointment appointment = appointmentDTO.toEntity(entityManager);

        appointment.setPrice(_establishmentService.getPrice());
        return new Pair<>(appointmentRepository.save(appointment).getId(),
                _establishmentService.getUsingDynamicPrice() ? _establishmentService.getPrice() : null);
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

    @Transactional(readOnly = true)
    public AppointmentCountDTO countAppointments(boolean userView) {
        Optional<Tuple> count = appointmentRepository.countAppointments(UserContext.getUserId(), userView);
        if (count.isEmpty()) {
            return new AppointmentCountDTO(0, 0);
        }
        int upcomming = 0;
        int past = 0;
        try {
            upcomming = count.get().get(0, BigDecimal.class).intValue();
        } catch (Exception e) {
        }
        try {
            past = count.get().get(1, BigDecimal.class).intValue();
        } catch (Exception e) {
        }
        return new AppointmentCountDTO(upcomming, past);
    }

    @Transactional
    public void feedback(long id, int feedback) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        if (appointment.getUser().getId() != UserContext.getUserId()) {
            throw new IllegalArgumentException("You do not have permission to give feedback for this appointment");
        }

        boolean replace = false;
        if (appointment.getFeedback() != null) {
            if (appointment.getFeedback() == feedback) {
                return;
            }
            replace = true;
            feedback -= appointment.getFeedback();
        }
        employeeService.addFeedback(appointment.getEmployee().getId(), feedback, replace);
        establishmentService.addFeedback(appointment.getEstablishment().getId(), feedback, replace);
        appointment.setFeedback(feedback);
        appointment.setFeedbackAsked(true);
        appointmentRepository.save(appointment);
    }

    @Transactional
    public FeedbackDTO feedbackAsked() {
        Pageable pageable = PageRequest.of(0, 1);
        List<Appointment> appointments = appointmentRepository
                .findTopByUserIdAndFeedbackAskedFalseOrderByDateDescTimeDesc(UserContext.getUserId(), pageable);

        Appointment appointment = appointments.isEmpty() ? null : appointments.get(0);
        if (appointment == null) {
            return new FeedbackDTO();
        }
        appointment.setFeedbackAsked(true);
        appointmentRepository.save(appointment);
        return new FeedbackDTO(appointment.getId(), appointment.getEmployee().getUser().getName(),
                appointment.getEstablishment().getName());
    }

    @Transactional(readOnly = true)
    public String validateAppointments(boolean userView) {
        List<AppointmentsHashDTO> appointments = appointmentRepository.findAllAppointmentsHash(UserContext.getUserId(),
                userView);

        String hash = "";
        for (AppointmentsHashDTO appointment : appointments) {
            hash += appointment.toString();
        }

        return Utils.hash(hash);
    }

    @Transactional(readOnly = false)
    public void createProductRequest(Long appointmentId, Set<Long> productIds) throws GenericNotFoundException {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (!appointment.isPresent()) {
            throw new GenericNotFoundException("Appointment");
        }
        for (Long productId : productIds) {
            appointmentRepository.createProductRequest(appointmentId, productId);
        }
    }

    @Transactional(readOnly = true)
    public List<ProductRequestsDTO> getProductRequests(ProductRequestFilter filter) {
        return appointmentRepository.getProductRequests(filter);
    }
}
