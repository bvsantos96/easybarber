package com.teamsantos.easybarber.testData;

import com.teamsantos.easybarber.DTO.ScheduleDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.testDTOs.ScheduleExceptionTestDTO;
import com.teamsantos.easybarber.testDTOs.ScheduleTestDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ScheduleData {

    public static final List<ScheduleDTO> schedules;
    public static final List<ScheduleTestDTO> schedulesDisabled;
    public static final List<ScheduleDTO> schedulesErrors;
    public static final List<ScheduleExceptionTestDTO> scheduleExceptions;

    static {
        schedules = new ArrayList<>() {
            {
                add(
                        new ScheduleDTO(
                                1L,
                                EmployeeData.employees.get(0).getId(),
                                EstablishmentData.establishments.get(0).getId(),
                                Set.of(
                                        DAY_OF_WEEK.MONDAY,
                                        DAY_OF_WEEK.TUESDAY,
                                        DAY_OF_WEEK.WEDNESDAY,
                                        DAY_OF_WEEK.THURSDAY,
                                        DAY_OF_WEEK.FRIDAY),
                                LocalTime.parse("08:00"),
                                LocalTime.parse("12:00")));
                add(
                        new ScheduleDTO(
                                2L,
                                EmployeeData.employees.get(0).getId(),
                                EstablishmentData.establishments.get(1).getId(),
                                Set.of(
                                        DAY_OF_WEEK.MONDAY,
                                        DAY_OF_WEEK.TUESDAY,
                                        DAY_OF_WEEK.WEDNESDAY,
                                        DAY_OF_WEEK.THURSDAY,
                                        DAY_OF_WEEK.FRIDAY),
                                LocalTime.parse("13:00"),
                                LocalTime.parse("17:00")));
                add(
                        new ScheduleDTO(
                                3L,
                                EmployeeData.employees.get(1).getId(),
                                EstablishmentData.establishments.get(1).getId(),
                                Set.of(
                                        DAY_OF_WEEK.MONDAY,
                                        DAY_OF_WEEK.TUESDAY,
                                        DAY_OF_WEEK.WEDNESDAY,
                                        DAY_OF_WEEK.THURSDAY),
                                LocalTime.parse("08:00"),
                                LocalTime.parse("17:00")));
                add(
                        new ScheduleDTO(
                                4L,
                                EmployeeData.employees.get(1).getId(),
                                EstablishmentData.establishments.get(1).getId(),
                                Set.of(
                                        DAY_OF_WEEK.FRIDAY,
                                        DAY_OF_WEEK.SATURDAY),
                                LocalTime.parse("09:00"),
                                LocalTime.parse("20:00")));
            }
        };
        schedules.sort(Comparator.comparing(ScheduleDTO::getId));

        schedulesDisabled = new ArrayList<>() {
            {
                add(
                        new ScheduleTestDTO(
                                null,
                                EmployeeData.employees.get(1).getId(),
                                EstablishmentData.establishments.get(1).getId(),
                                Set.of(DAY_OF_WEEK.FRIDAY, DAY_OF_WEEK.SATURDAY),
                                LocalTime.parse("21:00"),
                                LocalTime.parse("23:00")));
            };
        };

        schedulesErrors = new ArrayList<>() {
            {
                add(new ScheduleDTO(
                        5L,
                        EmployeeData.employees.get(1).getId(),
                        EstablishmentData.establishments.get(0).getId(),
                        Set.of(
                                DAY_OF_WEEK.MONDAY,
                                DAY_OF_WEEK.TUESDAY,
                                DAY_OF_WEEK.WEDNESDAY,
                                DAY_OF_WEEK.THURSDAY,
                                DAY_OF_WEEK.FRIDAY),
                        LocalTime.parse("08:00"),
                        LocalTime.parse("12:00")));
            }
        };

        scheduleExceptions = new ArrayList<>() {
            {
                add(new ScheduleExceptionTestDTO(
                        1L,
                        EmployeeData.employees.get(0).getId(),
                        null,
                        Set.of(DAY_OF_WEEK.FRIDAY),
                        LocalTime.parse("10:30"),
                        LocalTime.parse("11:30"),
                        LocalDate.parse("2124-09-01"),
                        LocalDate.parse("2124-09-30"),
                        true));
            }
        };
    }
}
