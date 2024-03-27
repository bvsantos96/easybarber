package com.teamsantos.easybarber.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceTypeDTO;
import com.teamsantos.easybarber.entities.ServiceType;
import com.teamsantos.easybarber.repositories.ServiceRepository;
import com.teamsantos.easybarber.repositories.ServiceTypeRepository;

@Service
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, ServiceTypeRepository serviceTypeRepository,
            ModelMapper modelMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.modelMapper = modelMapper;
    }

    public void createService(ServiceDTO service) {
        serviceRepository.save(modelMapper.map(service, com.teamsantos.easybarber.entities.Service.class));
    }

    public void updateService(ServiceDTO service) {
        serviceRepository.save(modelMapper.map(service, com.teamsantos.easybarber.entities.Service.class));
    }

    public void createType(ServiceTypeDTO serviceDTO) {
        serviceTypeRepository.save(modelMapper.map(serviceDTO, ServiceType.class));
    }

    public void updateType(ServiceTypeDTO serviceDTO) {
        serviceTypeRepository.save(modelMapper.map(serviceDTO, ServiceType.class));
    }
}
