package com.teamsantos.easybarber.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO extends BaseDTO {
    private double latitude;
    private double longitude;
    private String address;
    private String country;
    private String city;
    private String name;
}
