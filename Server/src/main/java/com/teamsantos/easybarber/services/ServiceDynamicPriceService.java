package com.teamsantos.easybarber.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.filters.ServiceDynamicFilter;
import com.teamsantos.easybarber.DTO.service.ServiceDynamicPriceDTO;
import com.teamsantos.easybarber.repositories.establishmentServices.EstablishmentServiceEmployeeRepository;
import com.teamsantos.easybarber.repositories.services.ServiceDynamicPriceRepository;

import jakarta.persistence.EntityManager;

@Service
public class ServiceDynamicPriceService {
    private final ServiceDynamicPriceRepository serviceDynamicPriceRepository;
    private final EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository;
    private final EntityManager entityManager;

    @Autowired
    public ServiceDynamicPriceService(ServiceDynamicPriceRepository serviceDynamicPriceRepository,
            EstablishmentServiceEmployeeRepository establishmentServiceEmployeeRepository,
            EntityManager entityManager) {
        this.serviceDynamicPriceRepository = serviceDynamicPriceRepository;
        this.establishmentServiceEmployeeRepository = establishmentServiceEmployeeRepository;
        this.entityManager = entityManager;
    }

    public Double validate(long establishmentServiceId, Long establishmentStaffId,
            LocalDate date,
            LocalTime time) {
        Long establishmentServiceEmployeeId = establishmentStaffId == null ? null
                : establishmentServiceEmployeeRepository.getIdByEstablishmentServiceIdAndEstablishmentStaffId(
                        establishmentServiceId,
                        establishmentStaffId);
        return serviceDynamicPriceRepository.getDynamicPrice(establishmentServiceId,
                establishmentServiceEmployeeId,
                LocalDateTime.of(date, time));
    }

    @Transactional(readOnly = false)
    public Long create(ServiceDynamicPriceDTO serviceDynamicPriceDTO) {
        serviceDynamicPriceDTO.setId(null);
        return serviceDynamicPriceRepository.save(serviceDynamicPriceDTO.toEntity(entityManager)).getId();
    }

    @Transactional(readOnly = false)
    public Long update(ServiceDynamicPriceDTO serviceDynamicPriceDTO) {
        return serviceDynamicPriceRepository.save(serviceDynamicPriceDTO.toEntity(entityManager)).getId();
    }

    @Transactional(readOnly = false)
    public void delete(long id) {
        serviceDynamicPriceRepository.deleteById(id);
    }

    public BasePageDTO<ServiceDynamicPriceDTO> findAll(ServiceDynamicFilter filter, Pageable pageable) {
        return new BasePageDTO<ServiceDynamicPriceDTO>(serviceDynamicPriceRepository.findAllDTO(filter, pageable));
    }
}
