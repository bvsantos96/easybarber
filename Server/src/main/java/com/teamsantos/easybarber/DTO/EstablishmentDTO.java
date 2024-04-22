package com.teamsantos.easybarber.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Getter
@Setter
@AllArgsConstructor
public class EstablishmentDTO extends BaseEstablishmentDTO {
    private double distance;

    public EstablishmentDTO() {
        super();
    }

    public EstablishmentDTO(Long id, String name, String description, String address, Point location) {
        super(id, name, description, address, location);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        EstablishmentDTO establishment = (EstablishmentDTO) o;
        return Double.compare(establishment.distance, distance) == 0;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
