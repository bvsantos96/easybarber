package com.teamsantos.easybarber.services;

import java.security.Principal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceTypeDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.ServiceType;
import com.teamsantos.easybarber.exceptions.AlreadyExistsException;
import com.teamsantos.easybarber.repositories.EstablishmentServiceRepository;
import com.teamsantos.easybarber.repositories.ServiceRepository;
import com.teamsantos.easybarber.repositories.ServiceTypeRepository;
import com.teamsantos.easybarber.repositories.images.ServiceImageRepository;
import com.teamsantos.easybarber.utils.PageDTO;

import jakarta.transaction.Transactional;

@Service
public class ServiceService extends
        ServiceWithImages<com.teamsantos.easybarber.entities.Service, com.teamsantos.easybarber.entities.images.ServiceImage> {
    private final ServiceRepository serviceRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final EstablishmentServiceRepository establishmentServiceRepository;
    private final UserTypeService userTypeService;
    private final ModelMapper modelMapper;

    @Autowired
    public ServiceService(ServiceRepository repository,
            ServiceTypeRepository serviceTypeRepository,
            UserTypeService userTypeService,
            EstablishmentServiceRepository establishmentServiceRepository,
            ServiceImageRepository imageRepository,
            ModelMapper modelMapper) {
        super(repository, imageRepository, modelMapper);
        this.modelMapper = modelMapper;
        this.serviceRepository = repository;
        this.establishmentServiceRepository = establishmentServiceRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.userTypeService = userTypeService;
    }

    public void createService(ServiceDTO serviceDTO, Principal principal) {
        Employee employee = userTypeService.getEmployee(principal);
        ServiceType serviceType = serviceTypeRepository.findById(serviceDTO.getServiceTypeId()).orElseThrow();
        if (serviceRepository.existsById(serviceDTO.getId())) {
            throw new AlreadyExistsException("Service already exists");
        }
        if (serviceRepository.existsByEmployeeIdServiceTypeIdNameAndDescription(employee.getId(),
                serviceType.getId(), serviceDTO.getName(), serviceDTO.getDescription())) {
            throw new AlreadyExistsException("Service already exists");
        }
        com.teamsantos.easybarber.entities.Service service = modelMapper.map(serviceDTO,
                com.teamsantos.easybarber.entities.Service.class);
        service.setEmployee(employee);
        service.setServiceType(serviceType);
        serviceRepository.save(service);
    }

    public void updateService(ServiceDTO serviceDTO) {
        com.teamsantos.easybarber.entities.Service service = serviceRepository.findById(serviceDTO.getId())
                .orElseThrow();
        service.update(serviceDTO);
        Long serviceTypeId = serviceDTO.getServiceTypeId();
        if (null != serviceTypeId && !serviceTypeId.equals(0L)
                && !serviceTypeId.equals(service.getServiceType().getId())) {
            service.setServiceType(serviceTypeRepository.findById(serviceDTO.getServiceTypeId()).orElseThrow());
        }
        serviceRepository.save(service);
    }

    @Transactional
    public void deleteService(Long id) {
        establishmentServiceRepository.deleteByServiceId(id);
        serviceRepository.deleteById(id);
    }

    public void createType(ServiceTypeDTO serviceDTO) {
        if (serviceDTO != null) {
            serviceTypeRepository.save(modelMapper.map(serviceDTO, ServiceType.class));
        }
    }

    public void updateType(ServiceTypeDTO serviceDTO) throws NotFoundException {
        serviceTypeRepository.save(modelMapper.map(serviceDTO, ServiceType.class));
    }

    public Page<ServiceDTO> getServices(Principal principal, Pageable pageable) {
        Employee employee = userTypeService.getEmployee(principal);
        return getServices(employee.getId(), pageable);
    }

    public Page<ServiceDTO> getServices(Long employeeId, Pageable pageable) {
        return PageDTO.toDTO(modelMapper, serviceRepository.findByEmployeeId(employeeId, pageable), ServiceDTO.class,
                pageable);
    }

    public Page<ServiceDTO> list(Long serviceTypeId, Pageable pageable) {
        if (serviceTypeId == null || serviceTypeId.equals(0L)) {
            return PageDTO.toDTO(modelMapper, serviceRepository.findAll(pageable), ServiceDTO.class, pageable);
        }
        return PageDTO.toDTO(modelMapper, serviceRepository.listByServiceTypeId(serviceTypeId, pageable),
                ServiceDTO.class, pageable);
    }

    public List<ServiceTypeDTO> listTypes() {
        return serviceTypeRepository.list().stream().map(e -> modelMapper.map(e, ServiceTypeDTO.class))
                .toList();
    }
}
