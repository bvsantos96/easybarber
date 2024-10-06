package com.teamsantos.easybarber.DTO.appointment;

import java.math.BigDecimal;

import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCountDTO {
    private String responseMessage;
    private int upcomming;
    private int past;

    public AppointmentCountDTO(AppointmentCountDTO appointmentCountDTO) {
        this(appointmentCountDTO.getUpcomming(), appointmentCountDTO.getPast());
    }

    public AppointmentCountDTO(Tuple tuple) {
        this(tuple.get(0, BigDecimal.class), tuple.get(1, BigDecimal.class));
    }

    public AppointmentCountDTO(BigDecimal upcomming, BigDecimal past) {
        this(upcomming.intValue(), past.intValue());
    }

    public AppointmentCountDTO(int upcomming, int past) {
        this.upcomming = upcomming;
        this.past = past;
    }

    public AppointmentCountDTO(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
