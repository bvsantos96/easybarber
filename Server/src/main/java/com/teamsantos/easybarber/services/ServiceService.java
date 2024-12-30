package com.teamsantos.easybarber.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
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
import com.teamsantos.easybarber.repositories.establishmentServices.EstablishmentServiceEmployeeRepository;
import com.teamsantos.easybarber.repositories.establishmentServices.EstablishmentServiceRepository;
import com.teamsantos.easybarber.repositories.images.ServiceImageRepository;
import com.teamsantos.easybarber.repositories.services.ServiceDynamicPriceRepository;
import com.teamsantos.easybarber.repositories.services.ServiceRepository;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.utils.Triple;

import jakarta.persistence.EntityManager;

@Service
public class ServiceService extends
        ServiceWithImages<com.teamsantos.easybarber.entities.Service, com.teamsantos.easybarber.entities.images.ServiceImage> {
    private final ServiceRepository serviceRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final EstablishmentServiceRepository establishmentServiceRepository;
    private final ServiceDynamicPriceRepository serviceDynamicPriceRepository;
    private final EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository;
    private final ModelMapper modelMapper;
    private final EntityManager entityManager;

    @Autowired
    public ServiceService(ServiceRepository repository,
            EstablishmentServiceRepository establishmentServiceRepository,
            ServiceTypeRepository serviceTypeRepository,
            ServiceDynamicPriceRepository serviceDynamicPriceRepository,
            EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository,
            ServiceImageRepository imageRepository,
            ModelMapper modelMapper,
            EntityManager entityManager) {
        super(repository, imageRepository, modelMapper, entityManager);
        this.serviceRepository = repository;
        this.establishmentServiceRepository = establishmentServiceRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.serviceDynamicPriceRepository = serviceDynamicPriceRepository;
        this.establishmentServiceEmployeeRepository = establishmentServiceEmployeeRepository;
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

    public List<String> listDynamicPrices(int year, int month, long establishmentId,
            long establishmentServiceId,
            Long establishmentStaffId) {
        LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime to = from.plusMonths(1);
        Long establishmentServiceEmployeeId = establishmentStaffId == null ? null
                : establishmentServiceEmployeeRepository.getIdByEstablishmentServiceIdAndEstablishmentStaffId(
                        establishmentServiceId,
                        establishmentStaffId);
        return serviceDynamicPriceRepository.list(establishmentServiceId, establishmentServiceEmployeeId, from, to)
                .stream()
                .flatMap(dates -> Stream.of(
                        dates.getFirst().toLocalDate().toString(),
                        dates.getSecond().toLocalDate().toString()))
                .collect(Collectors.toList());
    }

    @Async
    public CompletableFuture<Triple<LocalDateTime, LocalDateTime, Double>> getPrice(long establishmentServiceId,
            Long establishmentServiceEmployeeId,
            LocalDateTime date) {
        return CompletableFuture.completedFuture(
                serviceDynamicPriceRepository
                        .findPriceByEstablishmentServiceIdAndEstablishmentServiceEmployeeIdAndDates(
                                establishmentServiceId,
                                establishmentServiceEmployeeId, date, null));
    }

    @Async
    public CompletableFuture<Triple<LocalDateTime, LocalDateTime, Double>> getPrices(long establishmentServiceId,
            Long establishmentServiceEmployeeId,
            LocalDateTime from, LocalDateTime to) {
        return CompletableFuture.completedFuture(
                serviceDynamicPriceRepository
                        .findPriceByEstablishmentServiceIdAndEstablishmentServiceEmployeeIdAndDates(
                                establishmentServiceId,
                                establishmentServiceEmployeeId, from, to));
    }
}
