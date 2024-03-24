package com.teamsantos.easybarber.services;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.EstablishmentStaff;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EstablishmentRepository;
import com.teamsantos.easybarber.repositories.EstablishmentStaffRepository;
import com.teamsantos.easybarber.repositories.UserRepository;
import com.teamsantos.easybarber.utils.GeometryUtils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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
    private final UserRepository userRepository;
    private final UserTypeService userTypeService;
    private final UserService userService;

    @Autowired
    public EstablishmentService(ModelMapper modelMapper, EstablishmentRepository establishmentRepository,
            UserRepository userRepository, UserTypeService userTypeService,
            EstablishmentStaffRepository establishmentStaffRepository, UserService userService) {
        this.modelMapper = modelMapper;
        this.establishmentRepository = establishmentRepository;
        this.establishmentStaffRepository = establishmentStaffRepository;
        this.userRepository = userRepository;
        this.userTypeService = userTypeService;
        this.userService = userService;
    }

    public EstablishmentDTO getEstablishment(Long id) throws NotFoundException {
        return establishmentRepository.findById(id).map((element) -> element.convertToDto())
                .orElseThrow(NotFoundException::new);
    }

    public List<EstablishmentDTO> listEstablishmentStaff(Long id) throws NotFoundException {
        return userRepository.findOwnedEstablishmentsById(id).stream()
                .map((element) -> modelMapper.map(element, EstablishmentDTO.class)).toList();
    }

    public void create(BaseEstablishmentDTO establishmentDTO, Principal principal) {
        create(establishmentDTO, userService.getUser(principal));
    }

    public void create(BaseEstablishmentDTO establishmentDTO, User owner) {
        Establishment establishment = modelMapper.map(establishmentDTO, Establishment.class);
        if (establishment != null) {
            establishment = establishmentRepository.save(establishment);
            establishmentDTO.setId(establishment.getId());
            EstablishmentStaff establishmentOwned = new EstablishmentStaff(owner, establishment, true, true, owner);
            if (owner.getEstablishments() == null)
                owner.setEstablishments(new HashSet<>());
            owner.getEstablishments().add(establishmentOwned);
            userRepository.save(owner);
            establishmentStaffRepository.save(establishmentOwned);
        } else
            throw new IllegalArgumentException("Establishment cannot be null");
    }

    public List<BaseEstablishmentDTO> findAllBase(Pageable pageable) {
        return establishmentRepository.findAllBase(pageable);
    }

    public void addEmployee(Long establishmentId, Long userId, Principal principal)
            throws NotFoundException, UnsupportedOperationException {
        addEmployee(establishmentId, userId, userService.getUser(principal));
    }

    public void addEmployee(Long establishmentId, Long userId, User invitor)
            throws NotFoundException, UnsupportedOperationException {
        Establishment establishment = establishmentRepository.findById(establishmentId)
                .orElseThrow(NotFoundException::new);
        if (userTypeService.isEmployee(userId)) {
            if (establishment.getStaff() == null)
                establishment.setStaff(new HashSet<>());
            establishment.getStaff()
                    .add(new EstablishmentStaff(userRepository.findById(userId).orElseThrow(UserNotFoundException::new),
                            establishment, false, true, invitor));
            // TODO: note that the we might want to start an employee approval process here,
            // so we might want to set approved to false
        }
    }

    public List<EstablishmentDTO> findByLocation(double latitude, double longitude, Pageable pageable) {
        return establishmentRepository.findClosestEstablishments(GeometryUtils.parseLocation(latitude, longitude),
                pageable);
    }
}
