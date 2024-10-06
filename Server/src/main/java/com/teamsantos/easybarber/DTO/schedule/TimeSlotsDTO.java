package com.teamsantos.easybarber.DTO.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimeSlotsDTO {
    private String responseMessage;
    private List<TimeSlotDTO> slots;

    @JsonIgnore
    private Map<String, Integer> slotsMap;

    public TimeSlotsDTO(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public void addTimeSlot(TimeSlotDTO timeSlot, Long employeeId) {
        if (slots == null) {
            slots = new ArrayList<>();
            slotsMap = new HashMap<>();
        }
        String key = String.format("%s-%s", timeSlot.getStart(), timeSlot.getEnd());
        if (slotsMap.containsKey(key)) {
            if (employeeId != null) {
                slots.get(slotsMap.get(key)).setEmployeeId(employeeId);
            }
            return;
        }
        slotsMap.put(key, slots.size());
        timeSlot.setEmployeeId(employeeId);
        slots.add(timeSlot);
    }

    public TimeSlotsDTO sort() {
        if (slots == null) {
            return this;
        }
        slots.sort((s1, s2) -> s1.getStart().compareTo(s2.getStart()));
        return this;
    }
}
