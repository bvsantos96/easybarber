package com.teamsantos.easybarber.DTO.appointment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CancelAppointmentDTO {
    private Long id;
    private String reason;

    @Override
    public String toString() {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error converting object to string.";
        }
    }
}
