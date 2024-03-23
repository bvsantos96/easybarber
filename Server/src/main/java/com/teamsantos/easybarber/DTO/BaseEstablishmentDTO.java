package com.teamsantos.easybarber.DTO;

import org.locationtech.jts.geom.Point;

import com.teamsantos.easybarber.utils.GeometryUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BaseEstablishmentDTO extends BaseResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String latitude;
    private String longitude;

    public Point getPoint() {
        return GeometryUtils.parseLocation(Double.parseDouble(longitude), Double.parseDouble(latitude));
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

}
