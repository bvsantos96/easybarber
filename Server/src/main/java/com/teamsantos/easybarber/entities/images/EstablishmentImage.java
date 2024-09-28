package com.teamsantos.easybarber.entities.images;

import com.teamsantos.easybarber.DTO.image.ImageDTO;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.base.Image;

import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(indexes = { @Index(columnList = "entity_id, is_main"), @Index(columnList = "entity_id, data") })
public class EstablishmentImage extends Image<Establishment, EstablishmentImage> {
    public ImageDTO convertToDto() {
        return new ImageDTO(this.getId(), this.getData(), this.isMain());
    }
}
