package com.teamsantos.easybarber.DTO.product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.teamsantos.easybarber.DTO.appointment.AppointmentUserInfoDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestsDTO {
    private Long id;
    private AppointmentUserInfoDTO appointment;
    private List<ProductDTO> products;

    public ProductRequestsDTO(Long id, Long appointmentId, LocalDate date, LocalTime time, Long userId, String userName,
            String mobileInformation, String establishmentName, List<ProductDTO> products) {
        this.id = id;
        this.appointment = new AppointmentUserInfoDTO(appointmentId, userId, userName, mobileInformation,
                establishmentName,
                LocalDateTime.of(date, time));
        this.products = products;
    }
}
