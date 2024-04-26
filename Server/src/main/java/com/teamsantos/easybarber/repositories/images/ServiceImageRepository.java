package com.teamsantos.easybarber.repositories.images;

import com.teamsantos.easybarber.entities.Service;
import com.teamsantos.easybarber.entities.images.ServiceImage;
import com.teamsantos.easybarber.repositories.base.ImageRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceImageRepository extends ImageRepository<Service, ServiceImage> {
}
