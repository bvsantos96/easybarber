package com.teamsantos.easybarber.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.repositories.EstablishmentRepository;

@Service
public class EstablishmentService {
    @Autowired
    private EstablishmentRepository establishmentRepository;

    public BaseEstablishmentDTO getEstablishment(Long id) throws NotFoundException {
        return establishmentRepository.findByIDNoOwner(id).orElseThrow(NotFoundException::new);
    }
}
