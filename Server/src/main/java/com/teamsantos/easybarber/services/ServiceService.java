package com.teamsantos.easybarber.services;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceTypeDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.ServiceType;
import com.teamsantos.easybarber.repositories.ServiceRepository;
import com.teamsantos.easybarber.repositories.ServiceTypeRepository;
import com.teamsantos.easybarber.utils.PageDTO;

import jakarta.transaction.Transactional;

@Service
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final UserTypeService userTypeService;
    private final ModelMapper modelMapper;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository, ServiceTypeRepository serviceTypeRepository,
            UserTypeService userTypeService,
            ModelMapper modelMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.userTypeService = userTypeService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void createService(ServiceDTO serviceDTO, Principal principal) {
        com.teamsantos.easybarber.entities.Service service = modelMapper.map(serviceDTO,
                com.teamsantos.easybarber.entities.Service.class);
        service.setEmployee(userTypeService.getEmployee(principal));
        serviceRepository.save(service);
    }

    public void updateService(ServiceDTO serviceDTO) {
        serviceRepository.save(modelMapper.map(serviceDTO, com.teamsantos.easybarber.entities.Service.class));
    }

    public void createType(ServiceTypeDTO serviceDTO, Principal principal) {
        serviceTypeRepository.save(modelMapper.map(serviceDTO, ServiceType.class));
    }

    public void updateType(ServiceTypeDTO serviceDTO) {
        serviceTypeRepository.save(modelMapper.map(serviceDTO, ServiceType.class));
    }

    public Page<ServiceDTO> getServices(Principal principal, Pageable pageable) {
        Employee employee = userTypeService.getEmployee(principal);
        return getServices(employee.getId(), pageable);
    }

    public Page<ServiceDTO> getServices(Long id, Pageable pageable) {
        return PageDTO.toDTO(modelMapper, serviceRepository.findByEmployeeId(id, pageable), ServiceDTO.class, pageable);
    }
}
