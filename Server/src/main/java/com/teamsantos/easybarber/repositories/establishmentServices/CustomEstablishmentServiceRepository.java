package com.teamsantos.easybarber.repositories.establishmentServices;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.teamsantos.easybarber.DTO.EstablishmentServiceBaseDTO;
import com.teamsantos.easybarber.DTO.EstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.filters.EstablishmentServiceFilter;

public interface CustomEstablishmentServiceRepository {
    Page<EstablishmentServiceDTO> findAll(EstablishmentServiceFilter filter, Pageable pageable);

    Page<EstablishmentServiceBaseDTO> findAllBase(EstablishmentServiceFilter filter, Pageable pageable);

    Page<ServiceDTO> findAllServiceDTO(EstablishmentServiceFilter filter, Pageable pageable);
}
