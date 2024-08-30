package com.teamsantos.easybarber.DTO;

import java.util.Set;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.teamsantos.easybarber.entities.images.EstablishmentImage;
import com.teamsantos.easybarber.utils.GeometryUtils;
import com.teamsantos.easybarber.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseEstablishmentDTO extends BaseResponseDTO {
    private String name;
    private String description;
    private String address;
    private Double latitude;
    private Double longitude;
    private Set<EstablishmentImage> images;

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

    public BaseEstablishmentDTO(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public BaseEstablishmentDTO(final Long id, final String name, final String description) {
        setId(id);
        this.name = name;
        this.description = description;
    }

    public BaseEstablishmentDTO(final Long id, final String name, final String description, final String address,
            final Point location, Set<EstablishmentImage> images) {
        setId(id);
        this.name = name;
        this.description = description;
        this.address = address;
        this.images = images;
        setLocation(location);
    }

    public BaseEstablishmentDTO(final Long id, final String name, final String description, final String address,
            final Point location) {
        setId(id);
        this.name = name;
        this.description = description;
        this.address = address;
        setLocation(location);
    }

    public void setLocation(final Point location) {
        this.latitude = location.getY();
        this.longitude = location.getX();
    }

    public BaseEstablishmentDTO(final Long id, final String name, final String description, final String address) {
        setId(id);
        this.name = name;
        this.description = description;
        this.address = address;
    }

    public BaseEstablishmentDTO(final Long id, final String name, final String description, final double latitude,
            final double longitude) {
        setId(id);
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public BaseEstablishmentDTO(final String name, final String description, final double latitude,
            final double longitude) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
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
        BaseEstablishmentDTO establishment = (BaseEstablishmentDTO) o;
        return Utils.equalsWithNull(this.name, establishment.getName()) &&
                Utils.equalsWithNull(this.description, establishment.getDescription()) &&
                Utils.equalsWithNull(this.address, establishment.getAddress()) &&
                Double.compare(establishment.latitude, latitude) == 0 &&
                Double.compare(establishment.longitude, longitude) == 0;
    }
}
