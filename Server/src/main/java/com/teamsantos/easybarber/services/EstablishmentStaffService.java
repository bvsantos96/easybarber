package com.teamsantos.easybarber.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.service.ServiceDTO;
import com.teamsantos.easybarber.DTO.service.ServiceTypeDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.ServiceType;
import com.teamsantos.easybarber.repositories.ServiceTypeRepository;
import com.teamsantos.easybarber.repositories.services.ServiceRepository;
import com.teamsantos.easybarber.security.utils.UserContext;

import jakarta.persistence.EntityManager;

@Service
public class EstablishmentStaffService {
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Autowired
    public EstablishmentStaffService(ServiceTypeRepository serviceTypeRepository,
            ServiceRepository serviceRepository,
            ModelMapper modelMapper, EntityManager entityManager) {
        this.serviceRepository = serviceRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;
    }

    @Transactional
    public void createService(ServiceDTO serviceDTO) {
        com.teamsantos.easybarber.entities.Service service = modelMapper.map(serviceDTO,
                com.teamsantos.easybarber.entities.Service.class);
        service.setEmployee(entityManager.getReference(Employee.class, UserContext.getEmployeeId()));
        service.setServiceType(entityManager.getReference(ServiceType.class, service.getServiceType()));
        serviceRepository.save(service);
    }

    @Transactional
    public void updateService(ServiceDTO serviceDTO) {
        com.teamsantos.easybarber.entities.Service service = modelMapper.map(serviceDTO,
                com.teamsantos.easybarber.entities.Service.class);
        service.setServiceType(serviceTypeRepository.findById(serviceDTO.getServiceTypeId()).orElseThrow());
        serviceRepository.save(service);
    }

    @Transactional
    public void createType(ServiceTypeDTO serviceDTO) {
        serviceTypeRepository.save(modelMapper.map(serviceDTO, ServiceType.class));
    }

    @Transactional
    public void updateType(ServiceTypeDTO serviceDTO) {
        serviceTypeRepository.save(modelMapper.map(serviceDTO, ServiceType.class));
    }
}
