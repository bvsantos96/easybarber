package com.teamsantos.easybarber.tests;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.ScheduleDTO;
import com.teamsantos.easybarber.DTO.ScheduleExceptionDTO;
import com.teamsantos.easybarber.DTO.SchedulesDTO;
import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.testData.ScheduleData;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.JSONToDTO;
import com.teamsantos.easybarber.utils.Pair;
import com.teamsantos.easybarber.utils.TestsState;
import com.teamsantos.easybarber.utils.Utils;

@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleTests {
    private final MockMvc mockMvc;

    @Autowired
    public ScheduleTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    private List<Long> create(String path, String jwt, String item) throws Exception {
        ResultActions result = CreateTest.createOrFoundWithResult(mockMvc, path, jwt, item);
        BaseResponseDTO response = JSONToDTO.toDTO(
                new JSONObject(result.andReturn().getResponse().getContentAsString()),
                BaseResponseDTO.class);
        return response.getIds();
    }

    private void createForbiden(String path, String jwt, String item) throws Exception {
        CreateTest.createForbidden(mockMvc, path, jwt, item);
    }

    @Test
    public void createSchedules() {
        createSchedules(true, true);
    }

    public void createSchedules(boolean initAuth, boolean initEmployee) {
        if (TestsState.ran(TestsState.SCHEDULE_CREATE_SCHEDULES)) {
            return;
        }
        TestsState.mark(TestsState.SCHEDULE_CREATE_SCHEDULES);
        try {
            new EstablishmentTests(mockMvc).testEmployees(initAuth, initEmployee);
            ScheduleData.schedules.forEach(schedule -> {
                try {
                    String jwt = new EmployeeTests(mockMvc).loginById(schedule.getEmployeeId(), false);
                    create("/schedule?replaceExisting=true", jwt, schedule.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    org.junit.jupiter.api.Assertions.fail(e.getMessage());
                }
            });

            ScheduleData.schedulesErrors.forEach(schedule -> {
                try {
                    String jwt = new EmployeeTests(mockMvc).loginById(schedule.getEmployeeId(), false);
                    createForbiden("/schedule", jwt, schedule.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    org.junit.jupiter.api.Assertions.fail(e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listSchedules() {
        listSchedules(true, true);
    }

    public void listSchedules(boolean initAuth, boolean initEmployee) {
        try {
            createSchedules(initAuth, initEmployee);
            long employeeId = ScheduleData.schedules.get(0).getEmployeeId();
            String jwt = new EmployeeTests(mockMvc).login(false);
            validateSchedulesWRequest(employeeId, false);
            long establishmentId = ScheduleData.schedules.get(0).getEstablishmentId();
            ResultActions result = CreateTest.get(mockMvc,
                    String.format("/establishment/%d/schedule", establishmentId), jwt);
            List<ScheduleDTO> schedules = JSONToDTO.fromPageDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), ScheduleDTO.class);
            assert schedules != null;
            List<ScheduleDTO> _schedules = getScheduleByEstablishmentId(employeeId, schedules);
            for (int i = 0; i < _schedules.size(); i++) {
                assert _schedules.get(i).equals(schedules.get(i++));
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    private List<ScheduleDTO> getScheduleByEmployeeId(long employeeId, List<ScheduleDTO> schedules) {
        return schedules.stream().filter(e -> e.getEmployeeId().equals(employeeId)).collect(Collectors.toList());
    }

    private List<ScheduleDTO> getScheduleByEstablishmentId(long establishmentId, List<ScheduleDTO> schedules) {
        return schedules.stream().filter(e -> e.getEstablishmentId().equals(establishmentId))
                .collect(Collectors.toList());
    }

    @Test
    public void createExceptions() {
        createExceptions(true, true);
    }

    public void createExceptions(boolean initAuth, boolean initEmployee) {
        if (TestsState.ran(TestsState.SCHEDULE_CREATE_EXCEPTIONS)) {
            return;
        }
        TestsState.mark(TestsState.SCHEDULE_CREATE_EXCEPTIONS);
        listSchedules(initAuth, initEmployee);
        try {
            ScheduleData.scheduleExceptions.forEach(exception -> {
                try {
                    String jwt = new EmployeeTests(mockMvc).loginById(exception.getEmployeeId(), false);
                    exception.setIds(create("/schedule/exception", jwt, exception.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    org.junit.jupiter.api.Assertions.fail(e.getMessage());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void listExceptions() {
        listExceptions(true, true);
    }

    public void listExceptions(boolean initAuth, boolean initEmployee) {
        try {
            createExceptions(initAuth, initEmployee);
            long employeeId = EmployeeData.employees.get(0).getId();
            String jwt = new EmployeeTests(mockMvc).login(false);
            ResultActions result = CreateTest.get(mockMvc, "/employee/schedule/exception", jwt);
            result.andExpect(MockMvcResultMatchers.status().isOk());
            List<ScheduleDTO> schedules = JSONToDTO.fromPageDTO(
                    new JSONObject(result.andReturn().getResponse().getContentAsString()), ScheduleDTO.class);
            assert schedules != null;
            List<ScheduleDTO> _schedules = getScheduleByEmployeeId(employeeId, schedules);
            for (int i = 0; i < _schedules.size(); i++) {
                assert schedules.get(i).equals(_schedules.get(i));
            }
            for (ScheduleExceptionDTO exception : ScheduleData.scheduleExceptions) {
                validateSchedulesWExceptions(exception.getEmployeeId(), exception.getDateFrom(), exception.getDateTo());
            }
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void disable() {
        disable(true, true);
    }

    public void disable(boolean initAuth, boolean initEmployee) {
        if (TestsState.ran(TestsState.SCHEDULE_DISABLE)) {
            return;
        }
        TestsState.mark(TestsState.SCHEDULE_DISABLE);
        createSchedules(initAuth, initEmployee);
        try {
            if (ScheduleData.schedulesDisabled.isEmpty()) {
                org.junit.jupiter.api.Assertions.fail("ScheduleData.schedulesDisabled.isEmpty()");
            }
            long employeeId = ScheduleData.schedulesDisabled.get(0).getEmployeeId();
            ScheduleData.schedulesDisabled.forEach(schedule -> {
                try {
                    String jwt = new EmployeeTests(mockMvc).loginById(schedule.getEmployeeId(), false);
                    schedule.setIds(create("/schedule?replaceExisting=false", jwt, schedule.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    org.junit.jupiter.api.Assertions.fail(e.getMessage());
                }
            });
            validateSchedulesWRequest(employeeId, true);
            ScheduleData.schedulesDisabled.forEach(schedule -> {
                try {
                    String jwt = new EmployeeTests(mockMvc).loginById(schedule.getEmployeeId(), false);
                    for (Long id : schedule.getIds()) {
                        assert CreateTest.delete(mockMvc, String.format("/schedule/%d", id), jwt).andReturn()
                                .getResponse().getStatus() == 200;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    org.junit.jupiter.api.Assertions.fail(e.getMessage());
                }
            });
            validateSchedulesWRequest(employeeId, false);
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    private void validateSchedulesWExceptions(long employeeId, LocalDate from, LocalDate to) throws Exception {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Invalid date");
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From date needs to be before or equal to date");
        }

        List<LocalDate> dates = from.datesUntil(to).toList();
        int maxIdx = dates.size() % 7;
        for (int i = 0; i < maxIdx; i += 7) {
            int toIdx = Math.min(dates.size() - 1, i + 7);
            ScheduleFilter filter = new ScheduleFilter();
            filter.setEmployeeId(employeeId);
            filter.setFrom(dates.get(i));
            filter.setTo(dates.get(toIdx));
            filter.setDayOfWeek(Utils.getDaysOfWeek(dates.get(i), dates.get(toIdx)));
            filter.setActive(true);
            _validateSchedulesWRequest(filter, false, true);
        }
    }

    private void validateSchedulesWRequest(long employeeId, boolean includeDisabled) throws Exception {
        ScheduleFilter filter = new ScheduleFilter();
        filter.setEmployeeId(employeeId);
        filter.setDayOfWeek(new HashSet<>(Arrays.asList(DAY_OF_WEEK.values())));
        filter.setStartHour(LocalTime.parse("00:01"));
        filter.setActive(true);
        _validateSchedulesWRequest(filter, includeDisabled, false);
    }

    private void _validateSchedulesWRequest(ScheduleFilter filter, boolean includeDisabled,
            boolean filterOutExceptions)
            throws Exception {
        ResultActions result = CreateTest.get(mockMvc,
                filter.generateURL("/schedules"));
        result.andExpect(MockMvcResultMatchers.status().isOk());
        List<SchedulesDTO> schedules = JSONToDTO.fromPageDTO(
                new JSONObject(result.andReturn().getResponse().getContentAsString()), SchedulesDTO.class);
        assert schedules != null;
        assert validateSchedules(schedules, filter.getEmployeeId(), includeDisabled, filterOutExceptions,
                filter.getFrom(), filter.getTo());
    }

    private boolean validateSchedules(List<SchedulesDTO> schedules, long employeeId,
            boolean includeDisabled,
            boolean filterOutExceptions, LocalDate from, LocalDate to) {
        Map<DAY_OF_WEEK, List<Pair<LocalTime, LocalTime>>> map = new HashMap<>();
        for (ScheduleDTO schedule : ScheduleData.schedules) {
            if (schedule.getEmployeeId() == employeeId) {
                for (DAY_OF_WEEK day : schedule.getDays()) {
                    if (map.containsKey(day)) {
                        map.get(day).add(new Pair<LocalTime, LocalTime>(schedule.getStartHour(),
                                schedule.getEndHour()));
                    } else {
                        map.put(day,
                                new ArrayList<>(Arrays
                                        .asList(new Pair<LocalTime, LocalTime>(schedule.getStartHour(),
                                                schedule.getEndHour()))));
                    }
                }
            }
        }
        if (includeDisabled) {
            for (ScheduleDTO schedule : ScheduleData.schedulesDisabled) {
                if (schedule.getEmployeeId() == employeeId) {
                    for (DAY_OF_WEEK day : schedule.getDays()) {
                        if (map.containsKey(day)) {
                            map.get(day).add(new Pair<LocalTime, LocalTime>(schedule.getStartHour(),
                                    schedule.getEndHour()));
                        } else {
                            map.put(day,
                                    new ArrayList<>(Arrays.asList(
                                            new Pair<LocalTime, LocalTime>(schedule.getStartHour(),
                                                    schedule.getEndHour()))));
                        }
                    }
                }
            }
        }
        if (filterOutExceptions) {
            removeExceptionFromMap(
                    ScheduleData.scheduleExceptions.stream()
                            .filter(exception -> exception.getEmployeeId() == employeeId).collect(Collectors.toList()),
                    employeeId, map, from, to);
        }

        for (SchedulesDTO _schedules : schedules) {
            try {
                removeScheduleFromMap(_schedules.getSchedules(), employeeId, map);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return map.size() == 0;
    }

    private void removeExceptionFromMap(List<ScheduleExceptionDTO> exceptions, long employeeId,
            Map<DAY_OF_WEEK, List<Pair<LocalTime, LocalTime>>> map, LocalDate from, LocalDate to) {
        List<ScheduleDTO> schedules = new ArrayList<>();
        for (ScheduleExceptionDTO exception : exceptions) {
            schedules.addAll(exception.toDTOs(from, to));
        }
        removeScheduleFromMap(schedules, employeeId, map);
    }

    private void removeScheduleFromMap(List<ScheduleDTO> _schedules, long employeeId,
            Map<DAY_OF_WEEK, List<Pair<LocalTime, LocalTime>>> map) {
        for (ScheduleDTO schedule : _schedules) {
            if (schedule.getEmployeeId() != employeeId) {
                throw new IllegalArgumentException("Invalid employeeId");
            }
            for (DAY_OF_WEEK day : schedule.getDays()) {
                if (!map.containsKey(day)) {
                    throw new IllegalArgumentException("Invalid day");
                }
                List<Pair<LocalTime, LocalTime>> list = map.get(day);
                for (int i = 0; i < list.size(); i++) {
                    LocalTime start = schedule.getStartHour();
                    LocalTime end = schedule.getEndHour();
                    Pair<LocalTime, LocalTime> pair = list.get(i);
                    if ((Utils.afterOrEqual(pair.getFirst(), start) && Utils.afterOrEqual(pair.getFirst(), end)
                            || (Utils.beforeOrEqual(pair.getSecond(), end)
                                    && Utils.beforeOrEqual(pair.getSecond(), start)))) {
                        continue;
                    }
                    if (Utils.afterOrEqual(pair.getFirst(), start)) {
                        if (!pair.getFirst().equals(start)) {
                            list.add(new Pair<LocalTime, LocalTime>(start, pair.getFirst()));
                        }
                        if (Utils.beforeOrEqual(pair.getSecond(), end)) {
                            list.remove(pair);
                            i--;
                        } else {
                            pair.setFirst(end);
                        }
                    } else {
                        if (pair.getSecond().isBefore(end)) {
                            list.add(new Pair<LocalTime, LocalTime>(pair.getSecond(), end));
                            pair.setSecond(start);
                        } else {
                            if (!pair.getSecond().equals(end)) {
                                list.add(new Pair<LocalTime, LocalTime>(end, pair.getSecond()));
                            }
                            pair.setSecond(start);
                        }
                    }
                }
                if (list.size() == 0) {
                    map.remove(day);
                }
            }
        }
    }
}
