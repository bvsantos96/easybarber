package com.teamsantos.easybarber.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.establishment.service.EstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.filters.EstablishmentServiceFilter;
import com.teamsantos.easybarber.DTO.filters.ServiceFilter;
import com.teamsantos.easybarber.DTO.filters.ServiceWithEmployeeFilter;
import com.teamsantos.easybarber.DTO.service.CreateServiceDTO;
import com.teamsantos.easybarber.DTO.service.ServiceBaseDTO;
import com.teamsantos.easybarber.DTO.service.ServiceDTO;
import com.teamsantos.easybarber.DTO.service.ServiceTypeDTO;
import com.teamsantos.easybarber.DTO.service.ServiceWithImagesDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.ServiceType;
import com.teamsantos.easybarber.exceptions.AlreadyExistsException;
import com.teamsantos.easybarber.exceptions.GenericNotFoundException;
import com.teamsantos.easybarber.repositories.ServiceTypeRepository;
import com.teamsantos.easybarber.repositories.establishmentServices.EstablishmentServiceRepository;
import com.teamsantos.easybarber.repositories.images.ServiceImageRepository;
import com.teamsantos.easybarber.repositories.services.ServiceRepository;
import com.teamsantos.easybarber.security.utils.UserContext;

import jakarta.persistence.EntityManager;

@Service
public class ServiceService extends
        ServiceWithImages<com.teamsantos.easybarber.entities.Service, com.teamsantos.easybarber.entities.images.ServiceImage> {
    private final ServiceRepository serviceRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final EstablishmentServiceRepository establishmentServiceRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Autowired
    public ServiceService(ServiceRepository repository,
            EstablishmentServiceRepository establishmentServiceRepository,
            ServiceTypeRepository serviceTypeRepository,
            ServiceImageRepository imageRepository,
            ModelMapper modelMapper,
            EntityManager entityManager) {
        super(repository, imageRepository, modelMapper, entityManager);
        this.serviceRepository = repository;
        this.establishmentServiceRepository = establishmentServiceRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;
    }

    @Transactional
    public long createService(CreateServiceDTO serviceDTO) throws GenericNotFoundException {
        long employeeId = UserContext.getEmployeeId();
        if (!serviceTypeRepository.existsById(serviceDTO.getServiceTypeId())) {
            throw new GenericNotFoundException("Service type not found");
        }
        if (serviceRepository.existsByEmployeeIdServiceTypeIdNameAndDescription(employeeId,
                serviceDTO.getServiceTypeId(), serviceDTO.getName(), serviceDTO.getDescription())) {
            throw new AlreadyExistsException("Service already exists");
        }
        com.teamsantos.easybarber.entities.Service service = modelMapper.map(serviceDTO,
                com.teamsantos.easybarber.entities.Service.class);
        service.setId(null);
        service.setEmployee(entityManager.getReference(Employee.class, employeeId));
        service.setServiceType(entityManager.getReference(ServiceType.class, serviceDTO.getServiceTypeId()));
        return serviceRepository.save(service).getId();
    }

    @Transactional
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

    @Transactional
    public long createType(ServiceTypeDTO serviceDTO) {
        if (serviceDTO.getId() != null && serviceTypeRepository.existsById(serviceDTO.getId())) {
            serviceDTO.setId(null);
        }
        return serviceTypeRepository.save(modelMapper.map(serviceDTO, ServiceType.class)).getId();
    }

    @Transactional
    public void updateType(ServiceTypeDTO serviceDTO) throws NotFoundException {
        serviceTypeRepository.save(modelMapper.map(serviceDTO, ServiceType.class));
    }

    @Transactional(readOnly = true)
    public Page<EstablishmentServiceDTO> listEstablishmentServices(EstablishmentServiceFilter filter,
            Pageable pageable) {
        return establishmentServiceRepository.findAll(filter, pageable)
                .map(e -> modelMapper.map(e, EstablishmentServiceDTO.class));
    }

    @Transactional(readOnly = true)
    public Page<ServiceBaseDTO> listServices(ServiceFilter filter, Pageable pageable) {
        return serviceRepository.findAllBase(filter, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ServiceWithImagesDTO> listServicesWithEmployee(ServiceWithEmployeeFilter filter, Pageable pageable) {
        return serviceRepository.findAllWEmployee(filter, pageable);
    }

    @Transactional(readOnly = true)
    public List<ServiceTypeDTO> listTypes() {
        return serviceTypeRepository.findAll().stream().map(e -> modelMapper.map(e, ServiceTypeDTO.class))
                .toList();
    }

    public boolean checkIfEmployeeIsServiceOwner(Long serviceId, Long id) {
        if (id != null && serviceRepository.checkIfEmployeeIsServiceOwner(serviceId, id)) {
            return true;
        }
        return false;
    }
}
