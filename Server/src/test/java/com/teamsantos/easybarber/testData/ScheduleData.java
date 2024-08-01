package com.teamsantos.easybarber.testData;

import com.teamsantos.easybarber.DTO.ScheduleDTO;
import com.teamsantos.easybarber.DTO.ScheduleExceptionDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ScheduleData {

    public static final List<ScheduleDTO> schedules;
    public static final List<ScheduleExceptionDTO> scheduleExceptions;

    static {
        schedules = new ArrayList<>() {
            {
                add(
                        new ScheduleDTO(
                                1L,
                                2L,
                                1L,
                                Set.of(
                                        DAY_OF_WEEK.MONDAY,
                                        DAY_OF_WEEK.TUESDAY,
                                        DAY_OF_WEEK.WEDNESDAY,
                                        DAY_OF_WEEK.THURSDAY,
                                        DAY_OF_WEEK.FRIDAY),
                                "08:00",
                                "12:00"));
                add(
                        new ScheduleDTO(
                                1L,
                                2L,
                                2L,
                                Set.of(
                                        DAY_OF_WEEK.MONDAY,
                                        DAY_OF_WEEK.TUESDAY,
                                        DAY_OF_WEEK.WEDNESDAY,
                                        DAY_OF_WEEK.THURSDAY,
                                        DAY_OF_WEEK.FRIDAY),
                                "13:00",
                                "17:00"));
                add(
                        new ScheduleDTO(
                                1L,
                                3L,
                                1L,
                                Set.of(
                                        DAY_OF_WEEK.MONDAY,
                                        DAY_OF_WEEK.TUESDAY,
                                        DAY_OF_WEEK.WEDNESDAY,
                                        DAY_OF_WEEK.THURSDAY),
                                "08:00",
                                "17:00"));
                add(
                        new ScheduleDTO(
                                1L,
                                3L,
                                1L,
                                Set.of(
                                        DAY_OF_WEEK.MONDAY,
                                        DAY_OF_WEEK.TUESDAY,
                                        DAY_OF_WEEK.WEDNESDAY,
                                        DAY_OF_WEEK.THURSDAY),
                                "09:00",
                                "20:00"));
            }
        };
        schedules.sort(Comparator.comparing(ScheduleDTO::getId));

        scheduleExceptions = new ArrayList<>() {
            {
                add(new ScheduleExceptionDTO(
                        1L,
                        1L,
                        null,
                        Set.of(
                                DAY_OF_WEEK.FRIDAY),
                        "10:30",
                        "11:30",
                        LocalDate.parse("2024-09-01"),
                        LocalDate.parse("2024-09-30"),
                        true));
            }
        };
    }
}
