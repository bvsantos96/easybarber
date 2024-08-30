package com.teamsantos.easybarber.repositories.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.teamsantos.easybarber.DTO.ServiceBaseDTO;
import com.teamsantos.easybarber.DTO.ServiceWithImagesDTO;
import com.teamsantos.easybarber.DTO.filters.ServiceFilter;
import com.teamsantos.easybarber.DTO.filters.ServiceWithEmployeeFilter;

public interface CustomServiceRepository {
    Page<ServiceBaseDTO> findAllBase(ServiceFilter filter, Pageable pageable);

    Page<ServiceWithImagesDTO> findAllWEmployee(ServiceWithEmployeeFilter filter, Pageable pageable);
}
