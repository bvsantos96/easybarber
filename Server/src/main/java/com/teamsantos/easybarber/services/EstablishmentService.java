package com.teamsantos.easybarber.services;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import org.locationtech.jts.io.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.CreateEstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.EmployeeDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.EstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceFullDTO;
import com.teamsantos.easybarber.DTO.filters.EstablishmentFilter;
import com.teamsantos.easybarber.DTO.filters.EstablishmentServiceFilter;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.EstablishmentStaff;
import com.teamsantos.easybarber.entities.images.EstablishmentImage;
import com.teamsantos.easybarber.exceptions.AlreadyExistsException;
import com.teamsantos.easybarber.exceptions.GenericNotFoundException;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.EstablishmentRepository;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.base.ImageRepository;
import com.teamsantos.easybarber.repositories.establishmentServices.EstablishmentServiceRepository;
import com.teamsantos.easybarber.repositories.services.ServiceRepository;
import com.teamsantos.easybarber.security.utils.UserContext;
import com.teamsantos.easybarber.utils.GeometryUtils;
import com.teamsantos.easybarber.utils.PageDTO;
import com.teamsantos.easybarber.utils.Pair;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.EntityManager;

@Service
public class EstablishmentService extends ServiceWithImages<Establishment, EstablishmentImage> {
    private final ModelMapper modelMapper;
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;
    private final EstablishmentServiceRepository establishmentServiceRepository;
    private final EstablishmentStaffRepository establishmentStaffRepository;
    private final EstablishmentRepository establishmentRepository;

    @Autowired
    public EstablishmentService(EstablishmentRepository repository,
            EmployeeRepository employeeRepository, ServiceRepository serviceRepository,
            ImageRepository<Establishment, EstablishmentImage> imageRepository,
            EstablishmentStaffRepository establishmentStaffRepository,
            EstablishmentServiceRepository establishmentServiceRepository,
            ModelMapper modelMapper, EntityManager entityManager) {
        super(repository, imageRepository, modelMapper, entityManager);
        this.establishmentRepository = repository;
        this.modelMapper = modelMapper;
        this.establishmentServiceRepository = establishmentServiceRepository;
        this.employeeRepository = employeeRepository;
        this.serviceRepository = serviceRepository;
        this.establishmentStaffRepository = establishmentStaffRepository;
    }

    @Transactional(readOnly = true)
    private Establishment getEstablishment(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public EstablishmentDTO getEstablishmentDTO(Long id) throws NotFoundException {
        return establishmentRepository.findByIdDTO(id).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<EstablishmentDTO> listEstablishmentStaff(Long id, Pageable pageable) {
        return employeeRepository.findOwnedEstablishmentsById(id, pageable).stream()
                .map((element) -> modelMapper.map(element, EstablishmentDTO.class)).toList();
    }

    public void create(BaseEstablishmentDTO establishmentDTO) throws Exception {
        create(establishmentDTO, UserContext.getEmployeeId());
    }

    @Transactional
    private void create(BaseEstablishmentDTO establishmentDTO, long employeeId) throws Exception {
        try {
            Establishment establishment = modelMapper.map(establishmentDTO, Establishment.class);
            if (establishment != null) {
                if (establishmentRepository.existsByName(establishment.getName()))
                    throw new AlreadyExistsException("Establishment name already exists");
                establishment = repository.save(establishment);
                establishmentDTO.setId(establishment.getId());
                Employee owner = employeeRepository.findById(UserContext.getEmployeeId())
                        .orElseThrow(() -> new GenericNotFoundException("Employee"));
                EstablishmentStaff establishmentOwned = new EstablishmentStaff(true, true, false,
                        owner, establishment);
                if (owner.getEstablishments() == null)
                    owner.setEstablishments(new HashSet<>());
                owner.getEstablishments().add(establishmentOwned);
                employeeRepository.save(owner);
            } else
                throw new IllegalArgumentException("Establishment cannot be null");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public void delete(long id) {
        establishmentServiceRepository.deleteByEstablishmentId(id);
        imageRepository.deleteByEntityId(id);
        establishmentStaffRepository.deleteByEstablishmentId(id);
        repository.deleteById(id);
    }

    @Transactional
    public void addEmployee(long establishmentId)
            throws NotFoundException, UnsupportedOperationException, UserAlreadyExistsException {
        addEmployee(establishmentId, UserContext.getEmployeeId());
    }

    @Transactional
    public void addEmployee(long establishmentId, long employeeId)
            throws NotFoundException, UnsupportedOperationException, UserAlreadyExistsException {
        Establishment establishment = establishmentRepository.findByIdWithStaff(establishmentId)
                .orElseThrow(NotFoundException::new);
        if (employeeRepository.existsById(employeeId)) {
            if (establishment.getStaff() == null)
                establishment.setStaff(new HashSet<>());
            if (establishment.getStaff().stream().anyMatch((staff) -> staff.getEmployee().getId().equals(employeeId)))
                throw new UserAlreadyExistsException("User is already an employee");
            establishment.getStaff()
                    .add(new EstablishmentStaff(false, true, false,
                            entityManager.getReference(Employee.class, employeeId),
                            establishment));
            repository.save(establishment);
            // TODO: note that the we might want to start an employee approval process here,
            // so we might want to set approved to false
        } else {
            throw new UnsupportedOperationException("User is not an employee!");
        }
    }

    @Transactional(readOnly = true)
    public Page<EstablishmentDTO> list(EstablishmentFilter filter, Pageable pageable)
            throws ParseException {
        if (filter.getLatitude() != null && filter.getLongitude() != null) {
            filter.setLocation(GeometryUtils.parseLocation(filter.getLatitude(), filter.getLongitude()));
            return establishmentRepository.findClosestEstablishments(
                    filter.getLocation(),
                    filter.getServiceType(),
                    filter.getPartialName(),
                    filter.getRating(),
                    pageable);
        }
        return establishmentRepository.list(
                filter.getServiceType(),
                filter.getRating(),
                pageable);
    }

    @Transactional(readOnly = true)
    public Page<ServiceDTO> listServices(long id, Pageable pageable) {
        EstablishmentServiceFilter filter = new EstablishmentServiceFilter();
        filter.setEstablishmentId(id);
        filter.setIncludeEstablishmentImage(false);
        return establishmentServiceRepository.findAllServiceDTO(filter, pageable);
    }

    public void addService(Long id, CreateEstablishmentServiceDTO serviceDTO)
            throws NotFoundException, AlreadyExistsException {
        if (id == null) {
            throw new IllegalArgumentException("Establishment id cannot be null");
        }
        if (serviceDTO == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        if (serviceDTO.getServiceId() == null || serviceDTO.getServiceId() <= 0) {
            throw new IllegalArgumentException("Service id cannot be null or less than 0");
        }
        if (serviceDTO.getPrice() == null) {
            throw new IllegalArgumentException("Service price cannot be null");
        }
        addService(id, serviceDTO.getServiceId(), serviceDTO.getPrice());
    }

    @Transactional
    public void addService(long id, Long serviceId, double price) throws NotFoundException, AlreadyExistsException {
        if (serviceId != null) {
            Establishment establishment = establishmentRepository.findByIdWithStaff(id)
                    .orElseThrow(NotFoundException::new);
            com.teamsantos.easybarber.entities.Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(NotFoundException::new);
            if (establishment.getStaff().stream()
                    .noneMatch((staff) -> Objects.equals(staff.getEmployee().getId(), service.getEmployee().getId())))
                throw new UnsupportedOperationException("User is not an employee of this establishment");
            if (establishmentServiceRepository.existsByServiceIdAndEstablishmentId(serviceId, id))
                throw new AlreadyExistsException("Service already registered in establishment");
            com.teamsantos.easybarber.entities.EstablishmentService serviceEntity = modelMapper.map(service,
                    com.teamsantos.easybarber.entities.EstablishmentService.class);
            serviceEntity.setId(null);
            serviceEntity.setPrice(price);
            serviceEntity.setEstablishment(establishment);
            serviceEntity.setService(service);
            establishmentServiceRepository.save(serviceEntity);
        }
    }

    @Transactional
    public void updateService(long establishmentId, CreateEstablishmentServiceDTO serviceDTO) throws NotFoundException {
        if (serviceDTO != null) {
            Establishment establishment = establishmentRepository.findByIdWithStaff(establishmentId).orElseThrow();
            com.teamsantos.easybarber.entities.Service service = serviceRepository.findById(serviceDTO.getServiceId())
                    .orElseThrow(NotFoundException::new);
            if (establishment.getStaff().stream().noneMatch((staff) -> staff.getEmployee().getUser()
                    .getMobileInformation().equals(service.getEmployee().getUser().getMobileInformation())))
                throw new UnsupportedOperationException("User is not an employee");
            com.teamsantos.easybarber.entities.EstablishmentService establishmentService = establishment.getServices()
                    .stream()
                    .filter((_service) -> _service.getId().equals(serviceDTO.getId()))
                    .findFirst().orElseThrow(NotFoundException::new);
            boolean changed = false;
            if (serviceDTO.getPrice() != null) {
                establishmentService.setPrice(serviceDTO.getPrice());
                changed = true;
            }
            if (serviceDTO.getActive() != null) {
                establishmentService.setActive(serviceDTO.getActive());
                changed = true;
            }
            if (changed) {
                repository.save(establishment);
            }
        }
    }

    @Transactional
    public void removeService(long id, long serviceId) {
        Establishment establishment = repository.findById(id).orElseThrow();
        establishment.getServices().removeIf((service) -> service.getId().equals(serviceId));
        repository.save(establishment);
    }

    @Transactional(readOnly = true)
    public ServiceFullDTO getService(long establishmentId, long serviceId) {
        return establishmentServiceRepository.findByEstablishmentIdAndServiceId(establishmentId, serviceId);
    }

    @Transactional(readOnly = true)
    public EstablishmentServiceDTO getService(long id) throws GenericNotFoundException {
        return Utils.getModelMapper().map(establishmentServiceRepository.findById(id)
                .orElseThrow(() -> new GenericNotFoundException("Establishment service")),
                EstablishmentServiceDTO.class);
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployees(long establishmentId, boolean onlyActive) throws NotFoundException {
        if (!repository.existsById(establishmentId)) {
            throw new NotFoundException();
        }
        return establishmentStaffRepository.findEmployeeByEstablishmentIdAndActiveFilter(establishmentId, onlyActive);
    }

    @Transactional(readOnly = true)
    public boolean isAdmin(long establishmentId, long employeeId) {
        return establishmentStaffRepository.isAdminOfEstablishment(employeeId, establishmentId);
    }

    @Transactional(readOnly = true)
    public boolean isStaff(long establishmentId, long employeeId) {
        return establishmentStaffRepository.isEmployeeOfEstablishment(employeeId, establishmentId);
    }

    @Transactional(readOnly = true)
    public Pair<Long, Integer> getDurationOfService(long establishmentId, long serviceId)
            throws GenericNotFoundException {
        Pair<Long, Integer> establishmentService = establishmentServiceRepository.getIdAndDuration(establishmentId,
                serviceId);
        if (establishmentService == null) {
            throw new GenericNotFoundException("Establishment service");
        }
        return establishmentService;
    }

    @Transactional
    public Page<EstablishmentDTO> getEstablishmentsByEmployeeId(Long employeeId, boolean admin, Pageable pageable) {
        return PageDTO.toDTO(modelMapper,
                establishmentRepository.findEstablishmentsByEmployeeId(employeeId, admin, pageable),
                EstablishmentDTO.class, pageable);
    }
}
