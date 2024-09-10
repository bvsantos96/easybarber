package com.teamsantos.easybarber.repositories.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.teamsantos.easybarber.DTO.ServiceBaseDTO;
import com.teamsantos.easybarber.DTO.ServiceWithImagesDTO;
import com.teamsantos.easybarber.DTO.filters.ServiceFilter;
import com.teamsantos.easybarber.DTO.filters.ServiceWithEmployeeFilter;

public interface CustomServiceRepository {
    List<ServiceBaseDTO> findAllBase(ServiceFilter filter, Pageable pageable);

    List<ServiceWithImagesDTO> findAllWEmployee(ServiceWithEmployeeFilter filter, Pageable pageable);

    long count(ServiceFilter filter);
}
