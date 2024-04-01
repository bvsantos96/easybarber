package com.teamsantos.easybarber.DTO;

import org.locationtech.jts.geom.Point;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamsantos.easybarber.utils.GeometryUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.locationtech.jts.io.ParseException;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseEstablishmentDTO extends BaseResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String address;
    private double latitude;
    private double longitude;

    // Note the serialization of Point class is not implemented by default, to send
    // this in the JSON we would need to implement a custom serializer.
    // https://chat.openai.com/share/724876ff-1846-4be0-8a49-f4199b3eb7a2
    @JsonIgnore
    public Point getLocation() throws ParseException {
        return GeometryUtils.parseLocation(latitude, longitude);
    }

    public BaseEstablishmentDTO() {
        super();
    }

    public BaseEstablishmentDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public BaseEstablishmentDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public BaseEstablishmentDTO(Long id, String name, String description, String address, Point location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.latitude = location.getY();
        this.longitude = location.getX();
    }

    public BaseEstablishmentDTO(Long id, String name, String description, String address) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
    }
}
