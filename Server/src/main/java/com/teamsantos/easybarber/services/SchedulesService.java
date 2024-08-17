package com.teamsantos.easybarber.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.DTO.BaseListDTO;
import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.ScheduleDTO;
import com.teamsantos.easybarber.DTO.ScheduleExceptionDTO;
import com.teamsantos.easybarber.DTO.SchedulesDTO;
import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.ScheduleException;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.repositories.EmployeeScheduleRepository;
import com.teamsantos.easybarber.repositories.EstablishmentRepository;
import com.teamsantos.easybarber.repositories.ScheduleExceptionsRepository;
import com.teamsantos.easybarber.utils.PageDTO;
import com.teamsantos.easybarber.utils.Pair;
import com.teamsantos.easybarber.utils.Utils;

@Service
public class SchedulesService {
    private final EmployeeScheduleRepository employeeScheduleRepository;
    private final EstablishmentRepository establishmentRepository;
    private final EstablishmentService establishmentService;
    private final ScheduleExceptionsRepository scheduleExceptionRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public SchedulesService(EmployeeScheduleRepository employeeScheduleRepository,
            EstablishmentRepository establishmentRepository, EstablishmentService establishmentService,
            ScheduleExceptionsRepository scheduleExceptionRepository, UserService userService,
            ModelMapper modelMapper) {
        this.employeeScheduleRepository = employeeScheduleRepository;
        this.establishmentRepository = establishmentRepository;
        this.establishmentService = establishmentService;
        this.scheduleExceptionRepository = scheduleExceptionRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    private Employee canChangeSchedule(ScheduleDTO exception, Employee principal) {
        boolean authorized = false;
        Boolean isOwner = null;
        Boolean isStaff = null;
        Employee employee = null;
        if (exception.getEmployeeId() == null && exception.getEstablishmentId() == null) {
            throw new IllegalArgumentException("Employee or Establishment must be informed");
        }
        if (exception.getEmployeeId() != null && exception.getEstablishmentId() != null) {
            isOwner = establishmentService.isAdmin(exception.getEstablishmentId(), principal.getId());
            isStaff = establishmentService.isStaff(exception.getEstablishmentId(), principal.getId());
            if (exception.getEmployeeId() == principal.getId()) {
                if (isStaff) {
                    employee = userService.getEmployee(exception.getEmployeeId());
                    authorized = true;
                }
            } else {
                employee = userService.getEmployee(exception.getEmployeeId());
                authorized = isOwner && isStaff;
            }
        } else if (exception.getEmployeeId() != null) {
            employee = principal;
            authorized = exception.getEmployeeId() == principal.getId();
        } else {
            if (isOwner == null) {
                isOwner = establishmentService.isAdmin(exception.getEstablishmentId(), principal.getId());
            }
            authorized = isOwner;
        }

        if (!authorized) {
            throw new IllegalArgumentException("Employee is not authorized to create this exception");
        }

        return employee;
    }

    public Pair<List<Long>, String> create(ScheduleDTO schedule, Employee principal, Boolean forceSave,
            Boolean replaceExisting) {
        String response = "";
        Employee employee = canChangeSchedule(schedule, principal);
        if (employeeScheduleRepository
                .hasOverlappingSchedule(
                        schedule.getEmployeeId(),
                        schedule.getDays(), schedule.getStartHour(), schedule.getEndHour(), true)) {
            if (!forceSave) {
                throw new IllegalArgumentException("Employee already has a schedule for this day/hours");
            } else {
                response = "Employee already has a schedule for this day/hours;";
                if (replaceExisting) {
                    Optional<List<EmployeeSchedule>> oSchedule = employeeScheduleRepository
                            .findByEmployeeIdAndDayInAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndActive(
                                    schedule.getEmployeeId(),
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
                schedule.toEntities(employee, establishmentRepository.findById(schedule.getEstablishmentId()).get()));
        return new Pair<List<Long>, String>(
                schedules.stream().map(EmployeeSchedule::getId).collect(Collectors.toList()), response);
    }

    public BaseListDTO<ScheduleDTO> getSchedulesMerged(ScheduleFilter filter) throws Exception {
        filter.parseDate();
        return new BaseListDTO<ScheduleDTO>(employeeScheduleRepository.findAll(filter.getSpecification()).stream()
                .map(EmployeeSchedule::toDTO).collect(Collectors.toList()));
    }

    public BasePageDTO<SchedulesDTO> getSchedulesMerged(ScheduleFilter filter, Pageable pageable) throws Exception {
        filter.parseDate(pageable);
        Map<LocalDate, List<ScheduleException>> exceptionsMap = null;
        if (filter.getFrom() != null) {
            List<ScheduleException> exceptions = scheduleExceptionRepository
                    .findAll(filter.getExceptionSpecification());
            exceptionsMap = exceptions.stream()
                    .collect(Collectors.groupingBy(ScheduleException::getDate));
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
                for (EmployeeSchedule schedule : schedulesMap.get(Utils.getDayOfWeek(_from))) {
                    dayDTO.addSchedule(schedule.toDTO());
                }
                if (exceptionsMap != null && exceptionsMap.containsKey(_from)) {
                    for (ScheduleException exception : exceptionsMap.get(_from)) {
                        dayDTO.applyException(exception);
                    }
                }
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

    public void disable(Long id, Employee principal) {
        EmployeeSchedule schedule = employeeScheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));
        ScheduleDTO dto = schedule.toDTO();
        canChangeSchedule(dto, principal);
        schedule.setActive(false);
        employeeScheduleRepository.save(schedule);
    }

    public List<Long> createException(ScheduleExceptionDTO exception, Employee principal) {
        Employee employee = canChangeSchedule(exception, principal);
        Establishment establishment = null;
        if (exception.getEstablishmentId() != null) {
            establishment = establishmentRepository.findById(exception.getEstablishmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Establishment not found"));
        }
        return scheduleExceptionRepository.saveAll(
                exception.toEntitiesExceptions(employee,
                        establishment))
                .stream().map(ScheduleException::getId)
                .collect(Collectors.toList());
    }

    public BasePageDTO<ScheduleExceptionDTO> getExceptions(ScheduleFilter filter, Pageable pageable) throws Exception {
        filter.parseDate(pageable);
        return new BasePageDTO<ScheduleExceptionDTO>(
                PageDTO.toDTO(modelMapper,
                        scheduleExceptionRepository.findAll(filter.getExceptionSpecification(), pageable),
                        ScheduleExceptionDTO.class, pageable));
    }

    public BasePageDTO<ScheduleDTO> getSchedules(ScheduleFilter filter, Pageable pageable) throws Exception {
        filter.parseDate(pageable);
        return new BasePageDTO<ScheduleDTO>(
                PageDTO.toDTO(modelMapper,
                        employeeScheduleRepository.findAll(filter.getSpecification(), pageable),
                        ScheduleDTO.class, pageable));
    }
}
