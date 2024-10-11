package com.teamsantos.easybarber.DTO.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchedulesDTO {
    private Long employeeId;
    private Long establishmentId;
    private Map<Long, List<EmployeeScheduleDTO>> schedulesMap;
    private LocalDate date;
    private DAY_OF_WEEK dayOfWeek;

    // TODO: Performance analysis
    public void addSchedule(EmployeeScheduleDTO newSchedule) {
        if (schedulesMap == null) {
            schedulesMap = new HashMap<>();
        }
        schedulesMap.computeIfAbsent(newSchedule.getEmployeeId(), k -> new ArrayList<>());
        List<EmployeeScheduleDTO> employeeSchedules = schedulesMap.get(newSchedule.getEmployeeId());

        for (int i = 0; i < employeeSchedules.size(); i++) {
            EmployeeScheduleDTO existingSchedule = employeeSchedules.get(i);
            if (existingSchedule.getDay() == newSchedule.getDay() &&
                    isTimeOverlap(existingSchedule.getStartHour(), existingSchedule.getEndHour(),
                            newSchedule.getStartHour(), newSchedule.getEndHour())) {
                mergeSchedules(existingSchedule, newSchedule);
                employeeSchedules.remove(i);
                addSchedule(newSchedule);
                return;
            }
        }

        employeeSchedules.add(newSchedule);
    }

    private boolean isTimeOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return (start1.isBefore(end2) && start2.isBefore(end1)) ||
                start1.equals(start2) || end1.equals(end2);
    }

    private void mergeSchedules(EmployeeScheduleDTO existing, EmployeeScheduleDTO newSchedule) {
        LocalTime mergedStart = earlier(existing.getStartHour(), newSchedule.getStartHour());
        LocalTime mergedEnd = later(existing.getEndHour(), newSchedule.getEndHour());
        newSchedule.setStartHour(mergedStart);
        newSchedule.setEndHour(mergedEnd);
    }

    private LocalTime earlier(LocalTime time1, LocalTime time2) {
        return time1.isBefore(time2) ? time1 : time2;
    }

    private LocalTime later(LocalTime time1, LocalTime time2) {
        return time1.isAfter(time2) ? time1 : time2;
    }

    public void applyExceptionDTO(ScheduleExceptionDTO exception) {
        if (schedulesMap == null)
            return;
        if (exception.getEmployeeId() == null) {
            schedulesMap.keySet().forEach(employeeId -> applyExceptionDTO(exception, employeeId));
        } else {
            applyExceptionDTO(exception, exception.getEmployeeId());
        }
    }

    // TODO: Performance analysis
    public void applyExceptionDTO(ScheduleExceptionDTO exception, Long employeeId) {
        List<EmployeeScheduleDTO> schedules = schedulesMap.get(employeeId);
        if (schedules == null)
            return;

        LocalTime exceptionStart = exception.getStartHour();
        LocalTime exceptionEnd = exception.getEndHour();

        schedules.removeIf(schedule -> isFullyOverlapped(schedule, exceptionStart, exceptionEnd));

        List<EmployeeScheduleDTO> newSchedules = new ArrayList<>();
        for (EmployeeScheduleDTO schedule : schedules) {
            adjustSchedule(schedule, exceptionStart, exceptionEnd, newSchedules);
        }

        schedules.addAll(newSchedules);
    }

    private boolean isFullyOverlapped(EmployeeScheduleDTO schedule, LocalTime exceptionStart, LocalTime exceptionEnd) {
        return exceptionStart.compareTo(schedule.getStartHour()) <= 0
                && exceptionEnd.compareTo(schedule.getEndHour()) >= 0;
    }

    private void adjustSchedule(EmployeeScheduleDTO schedule, LocalTime exceptionStart, LocalTime exceptionEnd,
            List<EmployeeScheduleDTO> newSchedules) {
        LocalTime scheduleStart = schedule.getStartHour();
        LocalTime scheduleEnd = schedule.getEndHour();

        if (exceptionStart.isAfter(scheduleStart) && exceptionEnd.isBefore(scheduleEnd)) {
            schedule.setEndHour(exceptionStart);
            newSchedules.add(new EmployeeScheduleDTO(null, schedule.getEmployeeId(), schedule.getEstablishmentId(),
                    schedule.getDay(), exceptionEnd, scheduleEnd));
        } else if (exceptionStart.compareTo(scheduleStart) <= 0 && exceptionEnd.isAfter(scheduleStart)
                && exceptionEnd.isBefore(scheduleEnd)) {
            schedule.setStartHour(exceptionEnd);
        } else if (exceptionStart.isAfter(scheduleStart) && exceptionStart.isBefore(scheduleEnd)
                && exceptionEnd.compareTo(scheduleEnd) >= 0) {
            schedule.setEndHour(exceptionStart);
        }
    }

    public void _addSchedule(EmployeeScheduleDTO item) {
        if (schedulesMap == null) {
            schedulesMap = new HashMap<>();
        }
        schedulesMap.computeIfAbsent(item.getEmployeeId(), k -> new ArrayList<>());
        schedulesMap.get(item.getEmployeeId()).add(item);
    }

    @JsonIgnore
    public List<EmployeeScheduleDTO> getSchedules() {
        return schedulesMap.values().stream().flatMap(List::stream).toList();
    }
}
