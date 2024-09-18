package com.teamsantos.easybarber.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentListDTO extends BaseDTO {
    private String serviceName;
    private String name;
    private String photo;
    private LocalDate date;
    private LocalTime time;
    private boolean confirmed;

    public AppointmentListDTO(Long id, String serviceName, String name, String image,
            LocalDate date, LocalTime time, boolean confirmed) {
        super(id);
        this.serviceName = serviceName;
        this.name = name;
        this.photo = image;
        this.date = date;
        this.time = time;
        this.confirmed = confirmed;
    }
}
