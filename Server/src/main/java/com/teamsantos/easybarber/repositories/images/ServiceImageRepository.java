package com.teamsantos.easybarber.repositories.images;

import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Service;
import com.teamsantos.easybarber.entities.images.ServiceImage;
import com.teamsantos.easybarber.repositories.base.ImageRepository;

@Repository
public interface ServiceImageRepository extends ImageRepository<Service, ServiceImage> {
}
