package com.teamsantos.easybarber.services.helper;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.DTO.schedule.EmployeeScheduleDTO;
import com.teamsantos.easybarber.DTO.schedule.ScheduleExceptionDTO;
import com.teamsantos.easybarber.DTO.schedule.SchedulesDTO;
import com.teamsantos.easybarber.DTO.schedule.TimeSlotDTO;
import com.teamsantos.easybarber.DTO.schedule.TimeSlotsDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.repositories.AppointmentRepository;
import com.teamsantos.easybarber.repositories.EmployeeScheduleRepository;
import com.teamsantos.easybarber.repositories.ScheduleExceptionsRepository;
import com.teamsantos.easybarber.repositories.services.ServiceRepository;
import com.teamsantos.easybarber.utils.Utils;

public class AvailabilityCalculation {
    private Map<DAY_OF_WEEK, List<EmployeeScheduleDTO>> schedules;
    private Set<Long> employees;
    private List<ScheduleExceptionDTO> _exceptions;
    private Map<LocalDate, List<ScheduleExceptionDTO>> exceptions;
    private int serviceDuration;
    private ScheduleFilter filter;
    private List<String> unavailableDates;
    private List<SchedulesDTO> schedulesMerged;
    private TimeSlotsDTO timeSlots;

    public AvailabilityCalculation(ScheduleFilter filter, EmployeeScheduleRepository employeeScheduleRepository,
            ScheduleExceptionsRepository scheduleExceptionRepository, AppointmentRepository appointmentRepository,
            ServiceRepository serviceRepository) {
        this.filter = filter;
        schedules = new HashMap<>();
        employees = new HashSet<>();

        for (EmployeeScheduleDTO schedule : employeeScheduleRepository.findAllDTO(filter)) {
            employees.add(schedule.getEmployeeId());
            if (!schedules.containsKey(schedule.getDay())) {
                ArrayList<EmployeeScheduleDTO> list = new ArrayList<>();
                list.add(schedule);
                schedules.put(schedule.getDay(), list);
            } else {
                schedules.get(schedule.getDay()).add(schedule);
            }
        }
        _exceptions = scheduleExceptionRepository
                .findAllByEstablishmentIdEmployeeSetFromAndToDTO(filter.getEstablishmentId(), employees,
                        filter.getFrom(),
                        filter.getTo());
        _exceptions.addAll(appointmentRepository.findAppointmentsByDateEmployeesEstablishmentDTO(
                filter.getFrom(), employees, filter.getEstablishmentId()));
        exceptions = new HashMap<>();

        for (ScheduleExceptionDTO exception : _exceptions) {
            if (!exceptions.containsKey(exception.getDateFrom())) {
                ArrayList<ScheduleExceptionDTO> list = new ArrayList<>();
                list.add(exception);
                exceptions.put(exception.getDateFrom(), list);
            } else {
                exceptions.get(exception.getDateFrom()).add(exception);
            }
        }

        serviceDuration = filter.getServiceId() == null ? 1 : serviceRepository.getDuration(filter.getServiceId());
    }

    public static enum RETURN_TYPE {
        UNAVAILABLE_DATES,
        SCHEDULES_MERGED,
        TIME_SLOTS
    }

    public List<String> getUnavailableDates() throws Exception {
        unavailableDates = new ArrayList<>();
        calculateAvailability(RETURN_TYPE.UNAVAILABLE_DATES);
        return unavailableDates;
    }

    public TimeSlotsDTO getTimeSlots() throws Exception {
        timeSlots = new TimeSlotsDTO();
        calculateAvailability(RETURN_TYPE.TIME_SLOTS);
        return timeSlots;
    }

    public BasePageDTO<SchedulesDTO> getSchedulesMerged(Pageable pageable) throws Exception {
        schedulesMerged = new ArrayList<>();
        if (filter.getFrom() == null) {
            for (DAY_OF_WEEK day : schedules.keySet()) {
                SchedulesDTO dayDTO = new SchedulesDTO();
                dayDTO.setDayOfWeek(day);
                dayDTO.setEmployeeId(filter.getEmployeeId());
                dayDTO.setEstablishmentId(filter.getEstablishmentId());
                if (!schedules.containsKey(day)) {
                    continue;
                }
                for (EmployeeScheduleDTO schedule : schedules.get(day)) {
                    dayDTO.addSchedule(schedule);
                }
                schedulesMerged.add(dayDTO);
            }
        } else {
            calculateAvailability(RETURN_TYPE.SCHEDULES_MERGED);
        }
        return new BasePageDTO<SchedulesDTO>(new PageImpl<SchedulesDTO>(schedulesMerged, pageable,
                filter.getDayOfWeek() == null ? schedules.size() : filter.numberOfDays()));
    }

    private void calculateAvailability(RETURN_TYPE returnType) throws Exception {
        LocalDate _to = (filter.getTo() == null ? filter.getFrom() : filter.getTo()).plusDays(1);
        for (LocalDate _from = filter.getFrom(); _from
                .isBefore(_to); _from = _from.plusDays(1)) {
            DAY_OF_WEEK day = Utils.getDayOfWeek(_from);
            if (!schedules.containsKey(day)) {
                if (returnType == RETURN_TYPE.UNAVAILABLE_DATES) {
                    unavailableDates.add(_from.toString());
                }
                continue;
            }
            SchedulesDTO dayDTO = new SchedulesDTO();
            dayDTO.setDate(_from);
            dayDTO.setDayOfWeek(day);
            for (EmployeeScheduleDTO schedule : schedules.get(day)) {
                dayDTO.addSchedule(schedule);
            }

            if (exceptions.containsKey(_from)) {
                for (ScheduleExceptionDTO exception : exceptions.get(_from)) {
                    dayDTO.applyExceptionDTO(exception);
                }
            }

            boolean merged = false;
            for (EmployeeScheduleDTO s : dayDTO.getSchedules()) {
                LocalTime start = s.getStartHour();
                if (filter.getStartHour() != null) {
                    if (s.getEndHour().isBefore(filter.getStartHour())) {
                        continue;
                    }
                    start = s.getStartHour().isBefore(filter.getStartHour()) ? filter.getStartHour() : s.getStartHour();
                }

                switch (returnType) {
                    case UNAVAILABLE_DATES:
                        if (s.getEndHour().isAfter(start.plusMinutes(serviceDuration))) {
                            continue;
                        }
                        break;
                    default:
                        if (s.getEndHour().isBefore(start.plusMinutes(serviceDuration))) {
                            continue;
                        }
                    case TIME_SLOTS:
                        long numberOfSlots = Duration.between(start, s.getEndHour()).toMinutes()
                                / serviceDuration;
                        for (int i = 0; i < numberOfSlots; i++) {
                            LocalTime _start = start.plusMinutes(i * serviceDuration);
                            LocalTime end = start.plusMinutes((i + 1) * serviceDuration);
                            if (filter.getStartHour() == null
                                    || !(_start.isBefore(filter.getStartHour())
                                            || end.isBefore(filter.getStartHour()))) {
                                timeSlots.addTimeSlot(new TimeSlotDTO(_start, end),
                                        s.getEmployeeId());
                            }
                        }
                        break;
                    case SCHEDULES_MERGED:
                        if (!merged) {
                            merged = true;
                            schedulesMerged.add(dayDTO);
                        }
                        break;
                }
            }
        }
    }
}
