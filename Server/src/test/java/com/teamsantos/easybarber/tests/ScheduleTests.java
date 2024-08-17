package com.teamsantos.easybarber.tests;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

import com.teamsantos.easybarber.DTO.BaseResponseDTOs;
import com.teamsantos.easybarber.DTO.ScheduleDTO;
import com.teamsantos.easybarber.DTO.SchedulesDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.testData.EmployeeData;
import com.teamsantos.easybarber.testData.ScheduleData;
import com.teamsantos.easybarber.utils.CreateTest;
import com.teamsantos.easybarber.utils.JSONToDTO;
import com.teamsantos.easybarber.utils.Pair;

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
        BaseResponseDTOs response = JSONToDTO.toDTO(
                new JSONObject(result.andReturn().getResponse().getContentAsString()),
                BaseResponseDTOs.class);
        return response.getIds();
    }

    private void createForbiden(String path, String jwt, String item) throws Exception {
        CreateTest.createForbidden(mockMvc, path, jwt, item);
    }

    @Test
    public void createSchedules() {
        createSchedules(true);
    }

    public void createSchedules(boolean init) {
        try {
            new EstablishmentTests(mockMvc).testEmployees(init, init);
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
        listSchedules(true);
    }

    public void listSchedules(boolean init) {
        try {
            createSchedules(true);
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
        createExceptions(true);
    }

    private void createExceptions(boolean init) {
        listSchedules(init);
        try {
            ScheduleData.scheduleExceptions.forEach(exception -> {
                try {
                    String jwt = new EmployeeTests(mockMvc).loginById(exception.getEmployeeId(), false);
                    create("/schedule/exception", jwt, exception.toString());
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
        listExceptions(true);
    }

    public void listExceptions(boolean init) {
        try {
            createExceptions(true);
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
        } catch (Exception e) {
            e.printStackTrace();
            org.junit.jupiter.api.Assertions.fail(e.getMessage());
        }
    }

    @Test
    public void disable() {
        disable(true);
    }

    public void disable(boolean init) {
        createSchedules(init);
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

    private void validateSchedulesWRequest(long employeeId, boolean includeDisabled) throws Exception {
        ResultActions result = CreateTest.get(mockMvc,
                String.format(
                        "/schedules?active=true&employeeId=%d&dayOfWeek=SUNDAY,MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY&startHour=00:01:00",
                        employeeId));
        result.andExpect(MockMvcResultMatchers.status().isOk());
        List<SchedulesDTO> schedules = JSONToDTO.fromPageDTO(
                new JSONObject(result.andReturn().getResponse().getContentAsString()), SchedulesDTO.class);
        assert schedules != null;
        assert validateSchedules(schedules, employeeId, includeDisabled);
    }

    private boolean validateSchedules(List<SchedulesDTO> schedules, long employeeId, boolean includeDisabled) {
        Map<DAY_OF_WEEK, List<Pair<LocalTime, LocalTime>>> map = new HashMap<>();
        for (ScheduleDTO schedule : ScheduleData.schedules) {
            if (schedule.getEmployeeId() == employeeId) {
                for (DAY_OF_WEEK day : schedule.getDays()) {
                    if (map.containsKey(day)) {
                        map.get(day).add(new Pair<LocalTime, LocalTime>(LocalTime.parse(schedule.getStartHour()),
                                LocalTime.parse(schedule.getEndHour())));
                    } else {
                        map.put(day,
                                new ArrayList<>(Arrays
                                        .asList(new Pair<LocalTime, LocalTime>(LocalTime.parse(schedule.getStartHour()),
                                                LocalTime.parse(schedule.getEndHour())))));
                    }
                }
            }
        }
        if (includeDisabled) {
            for (ScheduleDTO schedule : ScheduleData.schedulesDisabled) {
                if (schedule.getEmployeeId() == employeeId) {
                    for (DAY_OF_WEEK day : schedule.getDays()) {
                        if (map.containsKey(day)) {
                            map.get(day).add(new Pair<LocalTime, LocalTime>(LocalTime.parse(schedule.getStartHour()),
                                    LocalTime.parse(schedule.getEndHour())));
                        } else {
                            map.put(day,
                                    new ArrayList<>(Arrays.asList(
                                            new Pair<LocalTime, LocalTime>(LocalTime.parse(schedule.getStartHour()),
                                                    LocalTime.parse(schedule.getEndHour())))));
                        }
                    }
                }
            }
        }

        for (SchedulesDTO _schedules : schedules) {
            for (ScheduleDTO schedule : _schedules.getSchedules()) {
                if (schedule.getEmployeeId() != employeeId) {
                    return false;
                }
                for (DAY_OF_WEEK day : schedule.getDays()) {
                    if (!map.containsKey(day)) {
                        return false;
                    }
                    List<Pair<LocalTime, LocalTime>> list = map.get(day);
                    for (int i = 0; i < list.size(); i++) {
                        LocalTime start = LocalTime.parse(schedule.getStartHour());
                        LocalTime end = LocalTime.parse(schedule.getEndHour());
                        Pair<LocalTime, LocalTime> pair = list.get(i);
                        if (pair.getFirst().isAfter(start) || pair.getSecond().isBefore(end)) {
                            continue;
                        }
                        if (pair.getFirst().isAfter(start) || pair.getFirst() == start) {
                            if (pair.getSecond().isBefore(end) || pair.getSecond() == end) {
                                list.remove(pair);
                                i--;
                            } else {
                                pair.setFirst(end);
                            }
                        } else {
                            if (pair.getSecond().isBefore(end)) {
                                pair.setSecond(start);
                            } else {
                                list.add(new Pair<LocalTime, LocalTime>(end, pair.getSecond()));
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
        return map.size() == 0;
    }

    private int numberOfEmployeeSchedules(long employeeId) {
        return numberOfEmployeeSchedules(employeeId, false);
    }

    private int numberOfEmployeeSchedules(long employeeId, boolean includeDisabled) {
        int count = 0;
        for (ScheduleDTO scheduleDTO : ScheduleData.schedules) {
            if (scheduleDTO.getEmployeeId() == employeeId) {
                count += scheduleDTO.getDays().size();
            }
        }
        if (includeDisabled) {
            for (ScheduleDTO scheduleDTO : ScheduleData.schedulesDisabled) {
                if (scheduleDTO.getEmployeeId() == employeeId) {
                    count += scheduleDTO.getDays().size();
                }
            }
        }
        return count;
    }
}
