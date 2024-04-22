package com.teamsantos.easybarber.repositories.images;

import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.images.EstablishmentImage;
import com.teamsantos.easybarber.repositories.base.ImageRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstablishmentImageRepository extends ImageRepository<Establishment, EstablishmentImage> {
}
