package com.teamsantos.easybarber.DTO.schedule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimeSlotsDTO {
    private String responseMessage;
    private List<TimeSlotDTO> availableTimes;
    private List<TimeSlotDTO> slots;

    @JsonIgnore
    private Set<String> availableTimesSet;
    @JsonIgnore
    private Set<String> slotsSet;

    public TimeSlotsDTO(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public void addAvailableTime(TimeSlotDTO timeSlot) {
        if (availableTimes == null) {
            availableTimes = new ArrayList<>();
            availableTimesSet = new HashSet<>();
        }
        String key = String.format("%s-%s", timeSlot.getStart(), timeSlot.getEnd());
        if (availableTimesSet.contains(key)) {
            return;
        }
        availableTimesSet.add(key);
        availableTimes.add(timeSlot);
    }

    public void addTimeSlot(TimeSlotDTO timeSlot) {
        if (slots == null) {
            slots = new ArrayList<>();
            slotsSet = new HashSet<>();
        }
        String key = String.format("%s-%s", timeSlot.getStart(), timeSlot.getEnd());
        if (slotsSet.contains(key)) {
            return;
        }
        slotsSet.add(key);
        slots.add(timeSlot);
    }
}
