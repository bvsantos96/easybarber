package com.teamsantos.easybarber.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.locationtech.jts.geom.Point;

@Data
@EqualsAndHashCode(callSuper = true)
public class EstablishmentDTO extends BaseEstablishmentDTO {
    private double distance;

    public EstablishmentDTO() {
        super();
    }

    public EstablishmentDTO(Long id, String name, String description, String address, Point location, Object distance) {
        super(id, name, description, address, location);
        try {
            if (distance instanceof Double)
                this.distance = (Double) distance;
        } catch (Exception e) {
            System.err.println("Error parsing distance from: " + distance.toString());
        }
    }
}
