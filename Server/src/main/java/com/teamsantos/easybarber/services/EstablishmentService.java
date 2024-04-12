package com.teamsantos.easybarber.services;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.EmployeeDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.CreateEstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.EstablishmentStaff;
import com.teamsantos.easybarber.exceptions.AlreadyExistsException;
import com.teamsantos.easybarber.exceptions.UserAlreadyExistsException;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.EstablishmentRepository;
import com.teamsantos.easybarber.repositories.EstablishmentServiceRepository;
import com.teamsantos.easybarber.repositories.ServiceRepository;
import com.teamsantos.easybarber.utils.GeometryUtils;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.transaction.Transactional;
import org.locationtech.jts.io.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class EstablishmentService {
    private final ModelMapper modelMapper;
    private final EstablishmentRepository establishmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;
    private final EstablishmentServiceRepository establishmentServiceRepository;
    private final UserService userService;

    @Autowired
    public EstablishmentService(ModelMapper modelMapper, EstablishmentRepository establishmentRepository,
            EmployeeRepository employeeRepository, ServiceRepository serviceRepository,
            EstablishmentServiceRepository establishmentServiceRepository, UserService userService) {
        this.modelMapper = modelMapper;
        this.establishmentRepository = establishmentRepository;
        this.establishmentServiceRepository = establishmentServiceRepository;
        this.employeeRepository = employeeRepository;
        this.serviceRepository = serviceRepository;
        this.userService = userService;
    }

    public EstablishmentDTO getEstablishment(Long id) throws NotFoundException {
        return establishmentRepository.findById(id).map((element) -> element.convertToDto())
                .orElseThrow(NotFoundException::new);
    }

    public List<EstablishmentDTO> listEstablishmentStaff(Long id, Pageable pageable) {
        return employeeRepository.findOwnedEstablishmentsById(id, pageable).stream()
                .map((element) -> modelMapper.map(element, EstablishmentDTO.class)).toList();
    }

    @Transactional
    public void create(BaseEstablishmentDTO establishmentDTO, Principal principal) {
        create(establishmentDTO, userService.getEmployee(principal));
    }

    @Transactional
    public void create(BaseEstablishmentDTO establishmentDTO, Employee owner) {
        Establishment establishment = modelMapper.map(establishmentDTO, Establishment.class);
        if (establishment != null) {
            if (establishmentRepository.existsByName(establishment.getName()))
                throw new AlreadyExistsException("Establishment name already exists");
            establishment = establishmentRepository.save(establishment);
            establishmentDTO.setId(establishment.getId());
            EstablishmentStaff establishmentOwned = new EstablishmentStaff(true, true, false, owner, establishment);
            if (owner.getEstablishments() == null)
                owner.setEstablishments(new HashSet<>());
            owner.getEstablishments().add(establishmentOwned);
            employeeRepository.save(owner);
        } else
            throw new IllegalArgumentException("Establishment cannot be null");
    }

    public List<BaseEstablishmentDTO> findAllBase(Pageable pageable) {
        return establishmentRepository.findAllBase(pageable).getContent();
    }

    @Transactional
    public void addEmployee(Long establishmentId, Long userId, Principal principal)
            throws NotFoundException, UnsupportedOperationException, UserAlreadyExistsException {
        addEmployee(establishmentId, userId, userService.getEmployee(principal));
    }

    @Transactional
    public void addEmployee(Long establishmentId, Long employeeId, Employee invitor)
            throws NotFoundException, UnsupportedOperationException, UserAlreadyExistsException {
        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(NotFoundException::new);
        if (employeeRepository.existsById(employeeId)) {
            if (establishment.getStaff() == null)
                establishment.setStaff(new HashSet<>());
            if (establishment.getStaff().stream().anyMatch((staff) -> staff.getEmployee().getId().equals(employeeId)))
                throw new UserAlreadyExistsException("User is already an employee");
            establishment.getStaff()
                    .add(new EstablishmentStaff(false, true, false,
                            employeeRepository.findById(employeeId).orElseThrow(UserNotFoundException::new),
                            establishment));
            establishmentRepository.save(establishment);
            // TODO: note that the we might want to start an employee approval process here,
            // so we might want to set approved to false
        }
    }

    public Page<EstablishmentDTO> findByLocation(double latitude, double longitude, Pageable pageable)
            throws ParseException {
        return establishmentRepository.findClosestEstablishments(GeometryUtils.parseLocation(latitude, longitude),
                pageable);
    }

    public Page<ServiceDTO> listServices(Long id, Pageable pageable) {
        return establishmentRepository.findServicesByEstablishmentId(id, pageable);
    }

    @Transactional
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
    public void addService(Long id, Long serviceId, double price) throws NotFoundException, AlreadyExistsException {
        if (serviceId != null) {
            Establishment establishment = establishmentRepository.findById(id).orElseThrow(NotFoundException::new);
            com.teamsantos.easybarber.entities.Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(NotFoundException::new);
            if (establishment.getStaff().stream()
                    .noneMatch((staff) -> Objects.equals(staff.getEmployee().getId(), service.getEmployee().getId())))
                throw new UnsupportedOperationException("User is not an employee of this establishment");
            if (establishmentServiceRepository.existsByServiceIdAndEstablishmentId(serviceId, id))
                throw new AlreadyExistsException("Service already registered in establishment");
            com.teamsantos.easybarber.entities.EstablishmentService serviceEntity = modelMapper.map(service,
                    com.teamsantos.easybarber.entities.EstablishmentService.class);
            serviceEntity.setPrice(price);
            serviceEntity.setEstablishment(establishment);
            serviceEntity.setService(service);
            establishmentServiceRepository.save(serviceEntity);
            establishment.getServices()
                    .add(serviceEntity);
        }
    }

    @Transactional
    public void updateService(Long establishmentId, CreateEstablishmentServiceDTO serviceDTO) throws NotFoundException {
        if (serviceDTO != null) {
            Establishment establishment = establishmentRepository.findById(establishmentId).orElseThrow();
            com.teamsantos.easybarber.entities.Service service = serviceRepository.findById(serviceDTO.getId())
                    .orElseThrow(NotFoundException::new);
            if (establishment.getStaff().stream().noneMatch((staff) -> staff.getEmployee().getUser()
                    .getMobileInformation().equals(service.getEmployee().getUser().getMobileInformation())))
                throw new UnsupportedOperationException("User is not an employee");
            establishment.getServices().stream()
                    .filter((_service) -> _service.getId().equals(serviceDTO.getId()))
                    .findFirst().ifPresent((_service) -> modelMapper.map(serviceDTO, _service));
            establishmentRepository.save(establishment);
        }
    }

    @Transactional
    public void removeService(Long id, Long serviceId) {
        if (serviceId != null) {
            Establishment establishment = establishmentRepository.findById(id).orElseThrow();
            establishment.getServices().removeIf((service) -> service.getId().equals(serviceId));
            establishmentRepository.save(establishment);
        }
    }

    public List<EmployeeDTO> getEmployees(Long establishmentId, boolean onlyActive) throws NotFoundException {
        if (!establishmentRepository.existsById(establishmentId)) {
            throw new NotFoundException();
        }
        return employeeRepository.findEmployeesByEstablishmentId(establishmentId, onlyActive).stream()
                .map(EmployeeDTO::new).toList();
    }
}
