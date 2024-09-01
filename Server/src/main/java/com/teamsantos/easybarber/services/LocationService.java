package com.teamsantos.easybarber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.LocationDTO;
import com.teamsantos.easybarber.entities.Location;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.repositories.LocationRepository;

import jakarta.persistence.EntityManager;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final EntityManager entityManager;

    @Autowired
    public LocationService(LocationRepository locationRepository, EntityManager entityManager) {
        this.locationRepository = locationRepository;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public Page<LocationDTO> getUserLocations(Long userId, Pageable pageable) {
        return locationRepository.findLocationByUserId(userId, pageable).map(location -> {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setId(location.getId());
            locationDTO.setLatitude(location.getLatitude());
            locationDTO.setLongitude(location.getLongitude());
            locationDTO.setAddress(location.getAddress());
            locationDTO.setCity(location.getCity());
            locationDTO.setCountry(location.getCountry());
            locationDTO.setName(location.getName());
            locationDTO.setSelected(location.isSelected());
            return locationDTO;
        });
    }

    @Transactional
    public Long addLocation(LocationDTO locationDTO, long userId) {
        locationRepository.deleteIfExist(locationDTO.getLatitude(), locationDTO.getLongitude(), userId);
        locationRepository.unSelectAll(userId);
        Location location = new Location();
        location.setSelected(true);
        location.setLatitude(locationDTO.getLatitude());
        location.setLongitude(locationDTO.getLongitude());
        location.setAddress(locationDTO.getAddress());
        location.setCountry(locationDTO.getCountry());
        location.setCity(locationDTO.getCity() == null ? "" : locationDTO.getCity());
        location.setName(locationDTO.getName());
        location.setUser(entityManager.getReference(User.class, userId));
        return locationRepository.save(location).getId();
    }
}
