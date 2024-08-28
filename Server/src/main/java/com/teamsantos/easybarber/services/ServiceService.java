package com.teamsantos.easybarber.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.CreateServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceBaseDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceTypeDTO;
import com.teamsantos.easybarber.DTO.ServiceWithEmployeeDTO;
import com.teamsantos.easybarber.DTO.filters.ServiceFilter;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.ServiceType;
import com.teamsantos.easybarber.exceptions.AlreadyExistsException;
import com.teamsantos.easybarber.exceptions.GenericNotFoundException;
import com.teamsantos.easybarber.repositories.EstablishmentServiceRepository;
import com.teamsantos.easybarber.repositories.ServiceRepository;
import com.teamsantos.easybarber.repositories.ServiceTypeRepository;
import com.teamsantos.easybarber.repositories.images.ServiceImageRepository;
import com.teamsantos.easybarber.security.utils.UserContext;

import jakarta.persistence.EntityManager;

@Service
public class ServiceService extends
        ServiceWithImages<com.teamsantos.easybarber.entities.Service, com.teamsantos.easybarber.entities.images.ServiceImage> {

    private final ServiceRepository serviceRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final EstablishmentServiceRepository establishmentServiceRepository;
    private final UserTypeService userTypeService;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Autowired
    public ServiceService(ServiceRepository repository,
            ServiceTypeRepository serviceTypeRepository,
            UserTypeService userTypeService,
            EstablishmentServiceRepository establishmentServiceRepository,
            ServiceImageRepository imageRepository,
            ModelMapper modelMapper,
            EntityManager entityManager) {
        super(repository, imageRepository, modelMapper);
        this.serviceRepository = repository;
        this.establishmentServiceRepository = establishmentServiceRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.userTypeService = userTypeService;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;
    }

    public void createService(CreateServiceDTO serviceDTO) throws GenericNotFoundException {
        long employeeId = UserContext.getEmployeeId();
        if (serviceTypeRepository.existsById(serviceDTO.getServiceTypeId())) {
            throw new GenericNotFoundException("Service type not found");
        }
        if (serviceRepository.existsByEmployeeIdServiceTypeIdNameAndDescription(employeeId,
                serviceDTO.getServiceTypeId(), serviceDTO.getName(), serviceDTO.getDescription())) {
            throw new AlreadyExistsException("Service already exists");
        }
        com.teamsantos.easybarber.entities.Service service = modelMapper.map(serviceDTO,
                com.teamsantos.easybarber.entities.Service.class);
        service.setEmployee(entityManager.getReference(Employee.class, employeeId));
        service.setServiceType(entityManager.getReference(ServiceType.class, serviceDTO.getServiceTypeId()));
        serviceRepository.save(service);
    }

    public void updateService(ServiceDTO serviceDTO) throws GenericNotFoundException {
        com.teamsantos.easybarber.entities.Service service = serviceRepository.findById(serviceDTO.getId())
                .orElseThrow();
        service.update(serviceDTO);
        Long serviceTypeId = serviceDTO.getServiceTypeId();
        if (null != serviceTypeId && !serviceTypeId.equals(0L)
                && !serviceTypeId.equals(service.getServiceType().getId())) {
            if (serviceTypeRepository.existsById(serviceTypeId)) {
                throw new GenericNotFoundException("Service type not found");
            }
            service.setServiceType(entityManager.getReference(ServiceType.class, serviceTypeId));
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

    @Transactional(readOnly = true)
    public Page<ServiceBaseDTO> listServices(ServiceFilter filter, Pageable pageable) {
        filter.parseName();
        if (filter.isIncludeServiceImage()) {
            return serviceRepository.findAllBaseWImage(filter, pageable);
        }
        return serviceRepository.findAllBase(filter, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ServiceWithEmployeeDTO> listServicesWithEmployee(ServiceFilter filter, Pageable pageable) {
        filter.parseName();
        if (filter.isIncludeServiceImage()) {
            if (filter.isIncludeEmployeeImage()) {
                return serviceRepository.findAllWImagesAndEmployeeWImages(filter, pageable)
                        .map(e -> modelMapper.map(e, ServiceWithEmployeeDTO.class));
            }
            return serviceRepository.findAllBaseWImageAndEmployee(filter, pageable)
                    .map(e -> modelMapper.map(e, ServiceWithEmployeeDTO.class));
        } else {
            if (filter.isIncludeEmployeeImage()) {
                return serviceRepository.findAllBaseAndEmployeeWImages(filter, pageable)
                        .map(e -> modelMapper.map(e, ServiceWithEmployeeDTO.class));
            }
            return serviceRepository.findAllBaseAndEmployee(filter, pageable)
                    .map(e -> modelMapper.map(e, ServiceWithEmployeeDTO.class));
        }
    }

    @Transactional(readOnly = true)
    public List<ServiceTypeDTO> listTypes() {
        return serviceTypeRepository.list().stream().map(e -> modelMapper.map(e, ServiceTypeDTO.class))
                .toList();
    }
}
