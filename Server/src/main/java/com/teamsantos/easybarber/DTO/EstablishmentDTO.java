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
    private Long nVotes;
    private Long sumVotes;

    public EstablishmentDTO() {
        super();
    }

    public EstablishmentDTO(Long id, String name, String description, String address, Point location) {
        super(id, name, description, address, location);
    }

    public EstablishmentDTO(Long id, String name, String description, String address, Point location, Object distance) {
        super(id, name, description, address, location);
        try {
            if (distance instanceof Double) {
                Double _distance = (Double) distance;
                this.distance = _distance / 1000;
            }
        } catch (Exception e) {
            System.err.println("Error parsing distance from: " + distance.toString());
        }
    }

    public EstablishmentDTO(Long id, String name, String description, String address, Point location, Long nVotes,
            Long sumVotes) {
        super(id, name, description, address, location);
        this.nVotes = nVotes;
        this.sumVotes = sumVotes;
    }

    public EstablishmentDTO(Long id, String name, String description, String address, Point location, Object distance,
            Long nVotes, Long sumVotes) {
        this(id, name, description, address, location, distance);
        this.nVotes = nVotes;
        this.sumVotes = sumVotes;
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
