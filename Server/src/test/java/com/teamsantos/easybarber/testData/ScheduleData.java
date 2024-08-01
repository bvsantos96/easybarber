package com.teamsantos.easybarber.testData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.teamsantos.easybarber.DTO.ScheduleDTO;
import com.teamsantos.easybarber.DTO.ScheduleExceptionDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

public class ScheduleData {
    public static final List<ScheduleDTO> schedules;
    public static final List<ScheduleExceptionDTO> scheduleExceptions;

    static {
        schedules = new ArrayList<>() {
            {
                add(new ScheduleDTO(1L, 2L, 1L, Set.of(DAY_OF_WEEK.MONDAY, DAY_OF_WEEK.TUESDAY, DAY_OF_WEEK.WEDNESDAY,
                        DAY_OF_WEEK.THURSDAY, DAY_OF_WEEK.FRIDAY), "08:00", "12:00"));
                add(new ScheduleDTO(1L, 2L, 2L, Set.of(DAY_OF_WEEK.MONDAY, DAY_OF_WEEK.TUESDAY, DAY_OF_WEEK.WEDNESDAY,
                        DAY_OF_WEEK.THURSDAY, DAY_OF_WEEK.FRIDAY), "13:00", "17:00"));
                add(new ScheduleDTO(1L, 3L, 1L, Set.of(DAY_OF_WEEK.MONDAY, DAY_OF_WEEK.TUESDAY, DAY_OF_WEEK.WEDNESDAY,
                        DAY_OF_WEEK.THURSDAY), "08:00", "17:00"));
                add(new ScheduleDTO(1L, 3L, 1L, Set.of(DAY_OF_WEEK.MONDAY, DAY_OF_WEEK.TUESDAY, DAY_OF_WEEK.WEDNESDAY,
                        DAY_OF_WEEK.THURSDAY), "09:00", "20:00"));
            }
        };
        schedules.sort(Comparator.comparing(ScheduleDTO::getId));

        scheduleExceptions = new ArrayList<>() {
            {

            }
        };
        scheduleExceptions.sort(Comparator.comparing(ScheduleExceptionDTO::getId));
    }
}
