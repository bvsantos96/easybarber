package com.teamsantos.easybarber.DTO.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

import org.locationtech.jts.geom.Point;

import com.teamsantos.easybarber.DTO.BaseDTO;

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
    private String entityName;
    private String establishmentName;
    private String establishmentAddress;
    private Double latitude;
    private Double longitude;
    private String photo;
    private LocalDate date;
    private LocalTime time;
    private boolean confirmed;

    public AppointmentListDTO(Long id, String serviceName, String entityName, String establishmentName, Point location,
            String address, String image,
            LocalDate date, LocalTime time, boolean confirmed) {
        super(id);
        this.serviceName = serviceName;
        this.entityName = entityName;
        this.establishmentName = establishmentName;
        this.establishmentAddress = address;
        setLocation(location);
        this.photo = image;
        this.date = date;
        this.time = time;
        this.confirmed = confirmed;
    }

    private void setLocation(final Point location) {
        this.latitude = location.getY();
        this.longitude = location.getX();
    }
}
