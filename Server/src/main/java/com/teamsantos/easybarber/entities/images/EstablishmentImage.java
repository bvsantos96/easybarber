package com.teamsantos.easybarber.entities.images;

import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.base.Image;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class EstablishmentImage extends Image<Establishment, EstablishmentImage> {
}
