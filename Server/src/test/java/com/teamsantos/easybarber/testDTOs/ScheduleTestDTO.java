package com.teamsantos.easybarber.testDTOs;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.teamsantos.easybarber.DTO.schedule.ScheduleDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

import lombok.Getter;

@Getter
public class ScheduleTestDTO extends ScheduleDTO {
    private List<Long> ids;

    public ScheduleTestDTO() {
        super();
    }

    public ScheduleTestDTO(Long id, Long employeeId, Long establishmentId, Set<DAY_OF_WEEK> day, LocalTime startHour,
            LocalTime endHour) {
        super(id, employeeId, establishmentId, day, startHour, endHour);
    }

    public void setIds(List<Long> ids) {
        if (ids.size() == 0) {
            return;
        }
        setId(ids.get(0));
        this.ids = ids;
    }

    public void _setId(Long id) {
        super.setId(id);
        if (ids == null) {
            ids = new ArrayList<>();
        }
        ids.add(id);
    }
}
