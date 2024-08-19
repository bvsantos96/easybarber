package com.teamsantos.easybarber.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.ScheduleException;

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
    private List<ScheduleDTO> schedules;
    private LocalDate date;
    private DAY_OF_WEEK dayOfWeek;

    // TODO: Performance analysis
    public void addSchedule(ScheduleDTO newSchedule) {
        if (schedules == null) {
            schedules = new ArrayList<>();
        }

        LocalTime newStart = newSchedule.getStartHour();
        LocalTime newEnd = newSchedule.getEndHour();
        boolean merged = false;

        Iterator<ScheduleDTO> iterator = schedules.iterator();
        while (iterator.hasNext()) {
            ScheduleDTO existingSchedule = iterator.next();
            if (existingSchedule.getDays() == newSchedule.getDays()) {
                LocalTime existingStart = existingSchedule.getStartHour();
                LocalTime existingEnd = existingSchedule.getEndHour();
                if (isTimeOverlap(existingStart, existingEnd, newStart, newEnd)) {
                    LocalTime mergedStart = existingStart.isBefore(newStart) ? existingStart : newStart;
                    LocalTime mergedEnd = existingEnd.isAfter(newEnd) ? existingEnd : newEnd;

                    newSchedule.setStartHour(mergedStart);
                    newSchedule.setEndHour(mergedEnd);

                    iterator.remove();
                    merged = true;
                }
            }
        }

        if (!merged) {
            schedules.add(newSchedule);
        } else {
            addSchedule(newSchedule);
        }
    }

    private boolean isTimeOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return (start1.isBefore(end2) && start2.isBefore(end1)) ||
                start1.equals(start2) || end1.equals(end2);
    }

    // TODO: Performance analysis
    public void applyException(ScheduleException exception) {
        if (schedules == null || schedules.isEmpty()) {
            return;
        }

        for (int i = 0; i < schedules.size(); i++) {
            ScheduleDTO schedule = schedules.get(i);
            LocalTime scheduleStart = schedule.getStartHour();
            LocalTime scheduleEnd = schedule.getEndHour();
            LocalTime exceptionStart = exception.getStartHour();
            LocalTime exceptionEnd = exception.getEndHour();

            if (exceptionStart.compareTo(scheduleStart) <= 0 && exceptionEnd.compareTo(scheduleEnd) >= 0) {
                schedules.remove(i);
                i--;
            } else if (exceptionStart.isAfter(scheduleStart) && exceptionEnd.isBefore(scheduleEnd)) {
                schedule.setEndHour(exceptionStart);
                ScheduleDTO newSchedule = new ScheduleDTO(
                        null,
                        schedule.getEmployeeId(),
                        schedule.getEstablishmentId(),
                        schedule.getDays(),
                        exceptionEnd,
                        scheduleEnd);
                schedules.add(newSchedule);
            } else if (exceptionStart.compareTo(scheduleStart) <= 0 && exceptionEnd.isAfter(scheduleStart)
                    && exceptionEnd.isBefore(scheduleEnd)) {
                schedule.setStartHour(exceptionEnd);
            } else if (exceptionStart.isAfter(scheduleStart) && exceptionStart.isBefore(scheduleEnd)
                    && exceptionEnd.compareTo(scheduleEnd) >= 0) {
                schedule.setEndHour(exceptionStart);
            }
        }
    }
}
