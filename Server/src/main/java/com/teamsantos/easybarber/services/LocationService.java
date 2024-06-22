package com.teamsantos.easybarber.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.DTO.LocationDTO;
import com.teamsantos.easybarber.entities.Location;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.repositories.LocationRepository;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationDTO> getUserLocations(User user) {
        return locationRepository.findLocationByUser(user).stream().map(location -> {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setLatitude(location.getLatitude());
            locationDTO.setLongitude(location.getLongitude());
            locationDTO.setAddress(location.getAddress());
            return locationDTO;
        }).collect(Collectors.toList());
    }

    public void addLocation(LocationDTO locationDTO, User user) {
        Location location = new Location();
        location.setLatitude(locationDTO.getLatitude());
        location.setLongitude(locationDTO.getLongitude());
        location.setAddress(locationDTO.getAddress());
        location.setUser(user);
        locationRepository.save(location);
    }
}
