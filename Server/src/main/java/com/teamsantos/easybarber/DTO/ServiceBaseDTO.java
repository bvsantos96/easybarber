package com.teamsantos.easybarber.DTO;

import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiceBaseDTO extends BaseDTO {
    private String name;
    private String description;
    private int duration;
    private long serviceTypeId;
    private String image;

    public ServiceBaseDTO(Long id, String name, String description, int duration, long serviceTypeId) {
        super(id);
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.serviceTypeId = serviceTypeId;
    }

    public ServiceBaseDTO(Long id, String name, String description, int duration, long serviceTypeId, String image) {
        this(id, name, description, duration, serviceTypeId);
        this.image = image;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ServiceBaseDTO service)) {
            return false;
        }
        return getId().equals(service.getId())
                && name.equals(service.getName())
                && description.equals(service.getDescription())
                && Objects.equals(duration, service.getDuration())
                && serviceTypeId == service.getServiceTypeId()
                && Objects.equals(image, service.getImage());
    }
}
