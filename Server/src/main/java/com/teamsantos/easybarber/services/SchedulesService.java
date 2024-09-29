package com.teamsantos.easybarber.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentDTO;
import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.DTO.schedule.ScheduleDTO;
import com.teamsantos.easybarber.DTO.schedule.ScheduleExceptionDTO;
import com.teamsantos.easybarber.DTO.schedule.SchedulesDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.ScheduleException;
import com.teamsantos.easybarber.repositories.AppointmentRepository;
import com.teamsantos.easybarber.repositories.EmployeeScheduleRepository;
import com.teamsantos.easybarber.repositories.ScheduleExceptionsRepository;
import com.teamsantos.easybarber.utils.PageDTO;
import com.teamsantos.easybarber.utils.Pair;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.EntityManager;

@Service
public class SchedulesService {
    private final AppointmentRepository appointmentRepository;
    private final EmployeeScheduleRepository employeeScheduleRepository;
    private final EstablishmentService establishmentService;
    private final ScheduleExceptionsRepository scheduleExceptionRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Autowired
    public SchedulesService(EmployeeScheduleRepository employeeScheduleRepository,
            AppointmentRepository appointmentRepository,
            ScheduleExceptionsRepository scheduleExceptionRepository,
            EstablishmentService establishmentService,
            ModelMapper modelMapper, EntityManager entityManager) {
        this.employeeScheduleRepository = employeeScheduleRepository;
        this.establishmentService = establishmentService;
        this.scheduleExceptionRepository = scheduleExceptionRepository;
        this.appointmentRepository = appointmentRepository;
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
                throw new IllegalArgumentException("Employee already has a schedule for this day/hours");
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

    @Transactional(readOnly = true)
    public BasePageDTO<SchedulesDTO> getSchedulesMerged(ScheduleFilter filter, Pageable pageable) throws Exception {
        filter.parseDate(pageable);
        Map<LocalDate, List<ScheduleException>> exceptionsMap = null;
        if (filter.getFrom() != null) {
            exceptionsMap = getExceptionMapByDate(filter.getExceptionSpecification(true, true));
            if (filter.getEmployeeId() != null) {
                exceptionsMap.putAll(getExceptionMapByDate(filter.getExceptionSpecification(true, false)));
            }
            if (filter.getEstablishmentId() != null) {
                exceptionsMap.putAll(getExceptionMapByDate(filter.getExceptionSpecification(false, true)));
            }
        }
        List<EmployeeSchedule> schedules = employeeScheduleRepository.findAll(filter.getSpecification());
        Map<DAY_OF_WEEK, List<EmployeeSchedule>> schedulesMap = schedules.stream()
                .collect(Collectors.groupingBy(EmployeeSchedule::getDay));
        List<SchedulesDTO> content = new ArrayList<>();
        if (filter.getFrom() != null) {
            for (LocalDate _from = filter.getFrom(); _from.isBefore(filter.getTo()); _from = _from.plusDays(1)) {
                SchedulesDTO dayDTO = new SchedulesDTO();
                dayDTO.setDate(_from);
                dayDTO.setEmployeeId(filter.getEmployeeId());
                dayDTO.setEstablishmentId(filter.getEstablishmentId());
                DAY_OF_WEEK day = Utils.getDayOfWeek(_from);
                if (!schedulesMap.containsKey(day)) {
                    continue;
                }
                DAY_OF_WEEK _day = day;
                for (EmployeeSchedule schedule : schedulesMap.get(Utils.getDayOfWeek(_from))) {
                    if (schedule.getDay() != _day) {
                        _day = null;
                    }
                    dayDTO.addSchedule(schedule.toDTO());
                }
                if (exceptionsMap != null && exceptionsMap.containsKey(_from)) {
                    for (ScheduleException exception : exceptionsMap.get(_from)) {
                        dayDTO.applyException(exception);
                    }
                }
                dayDTO.setDayOfWeek(_day);
                content.add(dayDTO);
            }
        } else {
            for (DAY_OF_WEEK day : schedulesMap.keySet()) {
                SchedulesDTO dayDTO = new SchedulesDTO();
                dayDTO.setDayOfWeek(day);
                dayDTO.setEmployeeId(filter.getEmployeeId());
                dayDTO.setEstablishmentId(filter.getEstablishmentId());
                if (!schedulesMap.containsKey(day)) {
                    continue;
                }
                for (EmployeeSchedule schedule : schedulesMap.get(day)) {
                    dayDTO.addSchedule(schedule.toDTO());
                }
                content.add(dayDTO);
            }
        }

        return new BasePageDTO<SchedulesDTO>(new PageImpl<SchedulesDTO>(content, pageable, filter.numberOfDays()));
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
        for (ScheduleException _exception : _exceptions) {
            ids.add(scheduleExceptionRepository.save(_exception).getId());
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
}
