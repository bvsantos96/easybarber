package com.teamsantos.easybarber.services;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.User;
import com.teamsantos.easybarber.exceptions.UserNotFoundException;
import com.teamsantos.easybarber.repositories.EstablishmentRepository;
import com.teamsantos.easybarber.repositories.UserRepository;

@Service
public class EstablishmentService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EstablishmentRepository establishmentRepository;
    @Autowired
    private UserRepository userRepository;

    public BaseEstablishmentDTO getEstablishment(Long id) throws NotFoundException {
        return userRepository.findOwnedEstablishmentsById(id).map(establishment -> modelMapper.map(establishment, BaseEstablishmentDTO.class))
                .orElseThrow(NotFoundException::new);
    }

    public boolean create(BaseEstablishmentDTO establishmentDTO, Principal principal) {
        User owner = userRepository.findByMobileInformation(principal.getName()).orElseThrow(UserNotFoundException::new);
        return create(establishmentDTO, owner);
    }

    public boolean create(BaseEstablishmentDTO establishmentDTO, Long userId) {
        User owner = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return create(establishmentDTO, owner);
    }

    public boolean create(BaseEstablishmentDTO establishmentDTO, User owner) {
        Establishment establishment = modelMapper.map(establishmentDTO, Establishment.class);
        if (establishment != null) {
            establishmentRepository.save(establishment);
            Set<Establishment> establishments = owner.getOwned_establishments();
            if (establishments == null) 
                establishments = new HashSet<>();
            establishments.add(establishment);
            userRepository.save(owner);
            return true; 
        } else
            throw new IllegalArgumentException("Establishment cannot be null");
    }

    public List<BaseEstablishmentDTO> findAllBase(Pageable pageable) {
        return establishmentRepository.findAllBase(pageable);
    }
}
