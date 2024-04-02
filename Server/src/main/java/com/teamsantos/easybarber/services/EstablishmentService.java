package com.teamsantos.easybarber.services;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.EstablishmentServiceDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.EstablishmentStaff;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EmployeeRepository;
import com.teamsantos.easybarber.repositories.EstablishmentRepository;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.ServiceRepository;
import com.teamsantos.easybarber.utils.GeometryUtils;
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

@Service
public class EstablishmentService {
    private final ModelMapper modelMapper;
    private final EstablishmentRepository establishmentRepository;
    private final EstablishmentStaffRepository establishmentStaffRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;
    private final UserTypeService userTypeService;
    private final UserService userService;

    @Autowired
    public EstablishmentService(ModelMapper modelMapper, EstablishmentRepository establishmentRepository,
            EmployeeRepository employeeRepository, ServiceRepository serviceRepository, UserTypeService userTypeService,
            EstablishmentStaffRepository establishmentStaffRepository, UserService userService) {
        this.modelMapper = modelMapper;
        this.establishmentRepository = establishmentRepository;
        this.establishmentStaffRepository = establishmentStaffRepository;
        this.employeeRepository = employeeRepository;
        this.serviceRepository = serviceRepository;
        this.userTypeService = userTypeService;
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
            establishment = establishmentRepository.save(establishment);
            establishmentDTO.setId(establishment.getId());
            EstablishmentStaff establishmentOwned = new EstablishmentStaff(owner, establishment, true, true, owner);
            if (owner.getEstablishments() == null)
                owner.setEstablishments(new HashSet<>());
            owner.getEstablishments().add(establishmentOwned);
            employeeRepository.save(owner);
            establishmentStaffRepository.save(establishmentOwned);
        } else
            throw new IllegalArgumentException("Establishment cannot be null");
    }

    public List<BaseEstablishmentDTO> findAllBase(Pageable pageable) {
        return establishmentRepository.findAllBase(pageable).getContent();
    }

    @Transactional
    public void addEmployee(Long establishmentId, Long userId, Principal principal)
            throws NotFoundException, UnsupportedOperationException {
        addEmployee(establishmentId, userId, userService.getEmployee(principal));
    }

    @Transactional
    public void addEmployee(Long establishmentId, Long userId, Employee invitor)
            throws NotFoundException, UnsupportedOperationException {
        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(NotFoundException::new);
        if (userTypeService.isEmployee(userId)) {
            if (establishment.getStaff() == null)
                establishment.setStaff(new HashSet<>());
            establishment.getStaff()
                    .add(new EstablishmentStaff(
                            employeeRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new),
                            establishment, false, true, invitor));
            establishmentRepository.save(establishment);
            // TODO: note that the we might want to start an employee approval process here,
            // so we might want to set approved to false
        }
        throw new UnsupportedOperationException("User is not an employee");
    }

    public Page<EstablishmentDTO> findByLocation(double latitude, double longitude, Pageable pageable)
            throws ParseException {
        return establishmentRepository.findClosestEstablishments(GeometryUtils.parseLocation(latitude, longitude),
                pageable);
    }

    public Page<com.teamsantos.easybarber.entities.EstablishmentService> listServices(Long id, Pageable pageable) {
        return establishmentRepository.findServicesByEstablishmentId(id, pageable);
    }

    @Transactional
    public void addService(Long id, EstablishmentServiceDTO serviceDTO) {
        if (serviceDTO != null) {
            Establishment establishment = establishmentRepository.findById(id).orElseThrow();
            establishment.getStaff().stream()
                    .filter((staff) -> staff.getEmployee().getId().equals(serviceDTO.getEmployeeId()))
                    .findFirst().orElseThrow();
            establishment.getServices()
                    .add(modelMapper.map(serviceDTO, com.teamsantos.easybarber.entities.EstablishmentService.class));
            establishmentRepository.save(establishment);
        }
    }

    @Transactional
    public void addService(Long id, Long serviceId) throws NotFoundException {
        if (serviceId != null) {
            Establishment establishment = establishmentRepository.findById(id).orElseThrow(NotFoundException::new);
            com.teamsantos.easybarber.entities.Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(NotFoundException::new);
            if (establishment.getStaff().stream().noneMatch((staff) -> staff.getEmployee().getUser()
                    .getMobileInformation().equals(service.getEmployee().getUser().getMobileInformation())))
                throw new UnsupportedOperationException("User is not an employee");
            establishment.getServices()
                    .add(modelMapper.map(service, com.teamsantos.easybarber.entities.EstablishmentService.class));
            establishmentRepository.save(establishment);
        }
    }

    @Transactional
    public void updateService(Long establishmentId, EstablishmentServiceDTO serviceDTO) throws NotFoundException {
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

}
