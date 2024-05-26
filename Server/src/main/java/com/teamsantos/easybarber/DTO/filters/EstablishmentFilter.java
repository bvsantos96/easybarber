package com.teamsantos.easybarber.DTO.filters;

import java.time.LocalTime;

import org.locationtech.jts.geom.Point;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstablishmentFilter {
    private String name;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;
    private Point location;
    private String partialName;
    private Double rating;
    private Long serviceType;
    private LocalTime from;
    private LocalTime to;
}
