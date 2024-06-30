package com.teamsantos.easybarber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.DTO.LocationDTO;
import com.teamsantos.easybarber.entities.Location;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.repositories.LocationRepository;

import jakarta.transaction.Transactional;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Page<LocationDTO> getUserLocations(User user, Pageable pageable) {
        return locationRepository.findLocationByUser(user, pageable).map(location -> {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setLatitude(location.getLatitude());
            locationDTO.setLongitude(location.getLongitude());
            locationDTO.setAddress(location.getAddress());
            locationDTO.setCity(location.getCity());
            locationDTO.setCountry(location.getCountry());
            locationDTO.setName(location.getName());
            return locationDTO;
        });
    }

    @Transactional
    public Long addLocation(LocationDTO locationDTO, User user) {
        locationRepository.deleteIfExist(locationDTO.getLatitude(), locationDTO.getLongitude(), user);
        Location location = new Location();
        location.setLatitude(locationDTO.getLatitude());
        location.setLongitude(locationDTO.getLongitude());
        location.setAddress(locationDTO.getAddress());
        location.setCountry(locationDTO.getCountry());
        location.setCity(locationDTO.getCity());
        location.setName(locationDTO.getName());
        location.setUser(user);
        return locationRepository.save(location).getId();
    }
}
