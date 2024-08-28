package com.teamsantos.easybarber.repositories.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.teamsantos.easybarber.DTO.ServiceWithEmployeeDTO;
import com.teamsantos.easybarber.DTO.filters.ServiceFilter;

public interface CustomServiceRepository {
    Page<ServiceWithEmployeeDTO> findAll(ServiceFilter filter, Pageable pageable);
}
