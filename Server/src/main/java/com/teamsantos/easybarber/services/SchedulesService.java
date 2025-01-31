package com.teamsantos.easybarber.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentDTO;
import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.DTO.schedule.CalendarDayInfoDTO;
import com.teamsantos.easybarber.DTO.schedule.ScheduleDTO;
import com.teamsantos.easybarber.DTO.schedule.ScheduleExceptionDTO;
import com.teamsantos.easybarber.DTO.schedule.SchedulesDTO;
import com.teamsantos.easybarber.DTO.schedule.TimeSlotsDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.ScheduleException;
import com.teamsantos.easybarber.repositories.AppointmentRepository;
import com.teamsantos.easybarber.repositories.EmployeeScheduleRepository;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.ScheduleExceptionsRepository;
import com.teamsantos.easybarber.repositories.establishmentServices.EstablishmentServiceRepository;
import com.teamsantos.easybarber.repositories.services.ServiceRepository;
import com.teamsantos.easybarber.services.helper.AvailabilityCalculation;
import com.teamsantos.easybarber.utils.PageDTO;
import com.teamsantos.easybarber.utils.Pair;
import com.teamsantos.easybarber.utils.Triple;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.EntityManager;

@Service
public class SchedulesService {
    private final AppointmentRepository appointmentRepository;
    private final EmployeeScheduleRepository employeeScheduleRepository;
    private final ServiceRepository serviceRepository;
    private final ServiceService serviceService;
    private final EstablishmentService establishmentService;
    private final ScheduleExceptionsRepository scheduleExceptionRepository;
    private final EstablishmentStaffRepository establishmentStaffRepository;
    private final EstablishmentServiceRepository establishmentServiceRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Autowired
    public SchedulesService(EmployeeScheduleRepository employeeScheduleRepository,
            AppointmentRepository appointmentRepository,
            ServiceRepository serviceRepository,
            ServiceService serviceService,
            ScheduleExceptionsRepository scheduleExceptionRepository,
            EstablishmentStaffRepository establishmentStaffRepository,
            EstablishmentServiceRepository establishmentServiceRepository,
            EstablishmentService establishmentService,
            ModelMapper modelMapper, EntityManager entityManager) {
        this.employeeScheduleRepository = employeeScheduleRepository;
        this.establishmentService = establishmentService;
        this.scheduleRepository = scheduleRepository;
        this.scheduleExceptionRepository = scheduleExceptionRepository;
        this.establishmentStaffRepository = establishmentStaffRepository;
        this.establishmentServiceRepository = establishmentServiceRepository;
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.serviceService = serviceService;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;
    }

    private long canChangeSchedule(ScheduleDTO exception, long employeeId) {
        boolean authorized = false;
        Boolean isOwner = null;
        Boolean isStaff = null;
        Long employee = null;
        if (exception.getEmployeeId() == null && exception.getEstablishmentId() == null) {
            throw new IllegalArgumentException("Employee or Establishment must be informed");
        }
        if (exception.getEmployeeId() != null && exception.getEstablishmentId() != null) {
            isOwner = establishmentService.isAdmin(exception.getEstablishmentId(), employeeId);
            isStaff = establishmentService.isStaff(exception.getEstablishmentId(), employeeId);
            if (exception.getEmployeeId() == employeeId) {
                if (isStaff) {
                    employee = exception.getEmployeeId();
                    authorized = true;
                }
            } else {
                employee = exception.getEmployeeId();
                authorized = isOwner && isStaff;
            }
        } else if (exception.getEmployeeId() != null) {
            employee = employeeId;
            authorized = exception.getEmployeeId() == employeeId;
        } else {
            if (isOwner == null) {
                isOwner = establishmentService.isAdmin(exception.getEstablishmentId(), employeeId);
            }
            authorized = isOwner;
        }

        if (!authorized || employee == null) {
            throw new IllegalArgumentException("Employee is not authorized to create this exception");
        }

        return employee;
    }

    public Pair<List<Long>, String> create(ScheduleDTO schedule, long employeeId, Boolean forceSave,
            Boolean replaceExisting) {
        String response = "";
        long employee = canChangeSchedule(schedule, employeeId);
        if (employeeScheduleRepository
                .hasOverlappingSchedule(
                        employee,
                        schedule.getDays(), schedule.getStartHour(), schedule.getEndHour(), true)) {
            if (!forceSave) {
                throw new IllegalArgumentException(
                        "Employee already has a schedule that overlaps with this day/hours combination.");
            } else {
                response = "Employee already has a schedule for this day/hours;";
                if (replaceExisting) {
                    Optional<List<EmployeeSchedule>> oSchedule = employeeScheduleRepository
                            .findByEmployeeIdAndDayInAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndActive(
                                    employee,
                                    schedule.getDays(), schedule.getStartHour(), schedule.getEndHour(), true);
                    for (EmployeeSchedule s : oSchedule.get()) {
                        s.setActive(false);
                        employeeScheduleRepository.save(s);
                    }
                }
            }
        }
        // Optional<ScheduleExceptions> oExceptions = scheduleExceptionsRepository
        // .findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndDateAfter(employee.getId(),
        // schedule.getDay(), schedule.getStartHour(), schedule.getEndHour(), new
        // Date());
        // if (oExceptions.isPresent()) {
        // if (!forceSave) {
        // throw new IllegalArgumentException("Employee has an exception for this
        // day/hours");
        // } else {
        // response += "Employee has an exception for this day/hours";
        // oExceptions.get().setActive(false);
        // scheduleExceptionsRepository.save(oExceptions.get());
        // }
        // }
        List<EmployeeSchedule> schedules = this.employeeScheduleRepository.saveAll(
                schedule.toEntities(entityManager));
        return new Pair<List<Long>, String>(
                schedules.stream().map(EmployeeSchedule::getId).collect(Collectors.toList()), response);
    }

    @Transactional(readOnly = true)
    public BaseListDTO<ScheduleDTO> getSchedulesMerged(ScheduleFilter filter) throws Exception {
        filter.parseDate();
        return new BaseListDTO<ScheduleDTO>(employeeScheduleRepository.findAll(filter.getSpecification()).stream()
                .map(EmployeeSchedule::toDTO).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    private Map<LocalDate, List<ScheduleException>> getExceptionMapByDate(
            Specification<ScheduleException> specification) {
        return scheduleExceptionRepository.findAll(specification).stream()
                .collect(Collectors.groupingBy(ScheduleException::getDate));
    }

    private void convertEstablishmentStaffAndServiceIds(ScheduleFilter filter) {
        if (filter.getEstablishmentStaffId() != null) {
            filter.setEmployeeId(establishmentStaffRepository.getEmployeeId(filter.getEstablishmentStaffId()));
        }
        if (filter.getEstablishmentServiceId() != null) {
            filter.setServiceId(establishmentServiceRepository.getServiceId(filter.getEstablishmentServiceId()));
        }
    }

    @Transactional(readOnly = true)
    public BasePageDTO<SchedulesDTO> getSchedulesMerged(ScheduleFilter filter, Pageable pageable) throws Exception {
        convertEstablishmentStaffAndServiceIds(filter);
        filter.parseDate(pageable);
        AvailabilityCalculation availability = new AvailabilityCalculation(filter, employeeScheduleRepository,
                scheduleExceptionRepository, appointmentRepository, serviceRepository);
        return availability.getSchedulesMerged(pageable);
    }

    @Transactional(readOnly = true)
    public TimeSlotsDTO getSchedulesByDay(ScheduleFilter filter) throws Exception {
        convertEstablishmentStaffAndServiceIds(filter);
        filter.parseDate();
        CompletableFuture<Triple<LocalDateTime, LocalDateTime, Double>> pricesFuture = serviceService.getPrices(
                filter.getEstablishmentServiceId(),
                filter.getEstablishmentStaffId(),
                LocalDateTime.of(filter.getTo(), LocalTime.MAX),
                LocalDateTime.of(filter.getFrom(), filter.getStartHour()));
        AvailabilityCalculation availability = new AvailabilityCalculation(filter, employeeScheduleRepository,
                scheduleExceptionRepository, appointmentRepository, serviceRepository);
        TimeSlotsDTO timeSlots = availability.getTimeSlots().sort();
        try {
            Triple<LocalDateTime, LocalDateTime, Double> prices = pricesFuture.get(5, TimeUnit.SECONDS);
            timeSlots.setPrices(prices);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException("Failed to retrieve price", e);
        }
        return timeSlots;
    }

    @Transactional(readOnly = true)
    public List<String> getDaysByAvailability(ScheduleFilter filter) throws Exception {
        convertEstablishmentStaffAndServiceIds(filter);
        filter.parseDate();
        AvailabilityCalculation availability = new AvailabilityCalculation(filter, employeeScheduleRepository,
                scheduleExceptionRepository, appointmentRepository, serviceRepository);
        return availability.getUnavailableDates();
    }

    @Transactional
    public void disable(long id, long currentEmployeeId) {
        EmployeeSchedule schedule = employeeScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        ScheduleDTO dto = schedule.toDTO();
        canChangeSchedule(dto, currentEmployeeId);
        schedule.setActive(false);
        employeeScheduleRepository.save(schedule);
    }

    @Transactional
    public Set<Long> createException(ScheduleExceptionDTO exception, long currentEmployeeId) {
        long employee = canChangeSchedule(exception, currentEmployeeId);
        Set<ScheduleException> _exceptions = exception.toEntitiesExceptions(entityManager,
                employee,
                exception.getEstablishmentId());
        Set<Long> ids = new HashSet<>();
        List<ScheduleException> exceptions = scheduleExceptionRepository.saveAll(_exceptions);
        for (ScheduleException e : exceptions) {
            ids.add(e.getId());
        }
        return ids;
    }

    @Transactional(readOnly = true)
    public BasePageDTO<ScheduleExceptionDTO> getExceptions(ScheduleFilter filter, Pageable pageable) throws Exception {
        filter.parseDate(pageable);
        return new BasePageDTO<ScheduleExceptionDTO>(
                PageDTO.toDTO(modelMapper,
                        scheduleExceptionRepository.findAll(filter.getExceptionSpecification(), pageable),
                        ScheduleExceptionDTO.class, pageable));
    }

    @Transactional(readOnly = true)
    public BasePageDTO<ScheduleDTO> getSchedules(ScheduleFilter filter, Pageable pageable) throws Exception {
        filter.parseDate(pageable);
        return new BasePageDTO<ScheduleDTO>(
                PageDTO.toDTO(modelMapper,
                        employeeScheduleRepository.findAll(filter.getSpecification(), pageable),
                        ScheduleDTO.class, pageable));
    }

    @Transactional(readOnly = true)
    public boolean isAppointmentDateTimeValid(AppointmentDTO appointmentDTO, int duration) {
        boolean notValid = scheduleExceptionRepository.intercepts(appointmentDTO.getEmployeeId(),
                appointmentDTO.getEstablishmentId(), appointmentDTO.getDate(), appointmentDTO.getTime(),
                appointmentDTO.getTime().plusMinutes(duration));
        if (notValid) {
            return false;
        }
        notValid = scheduleExceptionRepository.intercepts(null, appointmentDTO.getEstablishmentId(),
                appointmentDTO.getDate(), appointmentDTO.getTime(), appointmentDTO.getTime().plusMinutes(duration));
        if (notValid) {
            return false;
        }
        notValid = scheduleExceptionRepository.intercepts(appointmentDTO.getEmployeeId(), null,
                appointmentDTO.getDate(),
                appointmentDTO.getTime(), appointmentDTO.getTime().plusMinutes(duration));
        if (notValid) {
            return false;
        }
        notValid = appointmentRepository.intercepts(appointmentDTO.getEmployeeId(), appointmentDTO.getDate(),
                appointmentDTO.getTime(), appointmentDTO.getTime().plusMinutes(duration));
        if (notValid) {
            return false;
        }
        notValid = !employeeScheduleRepository
                .existsByEmployeeIdAndEstablishmentIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
                        appointmentDTO.getEmployeeId(), appointmentDTO.getEstablishmentId(),
                        Utils.getDayOfWeek(appointmentDTO.getDate()), appointmentDTO.getTime(),
                        appointmentDTO.getTime().plusMinutes(duration));
        return !notValid;
    }

    @Transactional(readOnly = false)
    public void deleteSchedule(Long id, long employeeId) {
        EmployeeSchedule schedule = employeeScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        if (schedule.getEmployee().getId() != employeeId) {
            throw new IllegalArgumentException("Employee is not authorized to delete this schedule");
        }
        employeeScheduleRepository.delete(schedule);
    }

    @Transactional(readOnly = false)
    public void deactivateSchedule(Long id, long employeeId) {
        EmployeeSchedule schedule = employeeScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        if (schedule.getEmployee().getId() != employeeId) {
            throw new IllegalArgumentException("Employee is not authorized to delete this schedule");
        }
        schedule.setActive(false);
        employeeScheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public Map<LocalDate, CalendarDayInfoDTO> getCalendarInfo(ScheduleFilter filter) {
        Map<LocalDate, Long> appointments = appointmentRepository.getAppointmentsCalendar(filter.getFrom(),
                filter.getTo(), filter.getEmployeeId(), filter.getEstablishmentId());
        List<LocalDate> exceptions = scheduleExceptionRepository.getExceptionsCalendar(filter.getFrom(),
                filter.getTo(), filter.getEmployeeId(), filter.getEstablishmentId());
        List<DAY_OF_WEEK> days = employeeScheduleRepository.getDaysWithNoSchedule(filter.getEmployeeId(),
                filter.getEstablishmentId());

        Map<LocalDate, CalendarDayInfoDTO> calendarInfo = new HashMap<>();
        for (LocalDate date = filter.getFrom(); date.isBefore(filter.getTo()); date = date.plusDays(1)) {
            CalendarDayInfoDTO info = new CalendarDayInfoDTO();
            info.setNAppointments(appointments.getOrDefault(date, 0L));
            info.setDisabled(exceptions.contains(date));
            info.setHasSchedules(days.contains(Utils.getDayOfWeek(date)));
            calendarInfo.put(date, info);
        }
        return calendarInfo;
    }
}
