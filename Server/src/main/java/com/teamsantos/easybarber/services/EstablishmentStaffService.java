package com.teamsantos.easybarber.services;

import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceTypeDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.ServiceType;
import com.teamsantos.easybarber.repositories.ServiceRepository;
import com.teamsantos.easybarber.repositories.ServiceTypeRepository;
import com.teamsantos.easybarber.utils.PageDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class EstablishmentStaffService {
    private final UserTypeService userTypeService;
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceRepository serviceRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public EstablishmentStaffService(ServiceTypeRepository serviceTypeRepository,
            UserTypeService userTypeService, ServiceRepository serviceRepository,
            ModelMapper modelMapper) {
        this.serviceRepository = serviceRepository;
        this.userTypeService = userTypeService;
        this.serviceTypeRepository = serviceTypeRepository;
        this.modelMapper = modelMapper;
    }

    public void createService(ServiceDTO serviceDTO, Principal principal) {
        com.teamsantos.easybarber.entities.Service service = modelMapper.map(serviceDTO,
                com.teamsantos.easybarber.entities.Service.class);
        service.setEmployee(userTypeService.getEmployee(principal));
        service.setServiceType(serviceTypeRepository.findById(serviceDTO.getServiceTypeId()).orElseThrow());
        serviceRepository.save(service);
    }

    public void updateService(ServiceDTO serviceDTO) {
        com.teamsantos.easybarber.entities.Service service = modelMapper.map(serviceDTO,
                com.teamsantos.easybarber.entities.Service.class);
        service.setServiceType(serviceTypeRepository.findById(serviceDTO.getServiceTypeId()).orElseThrow());
        serviceRepository.save(service);
    }

    public void createType(ServiceTypeDTO serviceDTO) {
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
