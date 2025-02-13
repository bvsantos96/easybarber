package com.teamsantos.easybarber.DTO.schedule;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarDayInfoDTO {
    public enum AVAILABILITY {
        FREE(0),
        AVAILABLE(1),
        MEDIUM(2),
        FULL(3);

        private final int value;

        AVAILABILITY(int value) {
            this.value = value;
        }

        @JsonValue
        public int getValue() {
            return value;
        }
    }

    private boolean isDisabled;
    private boolean hasSchedules;
    private AVAILABILITY availability;
    private LocalDate date;

    public void setAvailability(Long appointmentsMinutesSum, Long availableDayMinutes) {
        int percentage = (availableDayMinutes == 0) ? (appointmentsMinutesSum > 0) ? 100
                : 0 : (int) ((appointmentsMinutesSum * 100) / availableDayMinutes);
        if (percentage == 0) {
            availability = AVAILABILITY.FREE;
        } else if (percentage <= 20) {
            availability = AVAILABILITY.AVAILABLE;
        } else if (percentage <= 80) {
            availability = AVAILABILITY.MEDIUM;
        } else {
            availability = AVAILABILITY.FULL;
        }
    }

    public CalendarDayInfoDTO(LocalDate date, AVAILABILITY availability) {
        this.date = date;
        this.availability = availability;
    }
}
