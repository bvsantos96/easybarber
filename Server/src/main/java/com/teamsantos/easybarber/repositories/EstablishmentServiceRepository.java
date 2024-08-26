package com.teamsantos.easybarber.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.EstablishmentService;

@Repository
public interface EstablishmentServiceRepository extends JpaRepository<EstablishmentService, Long> {
    boolean existsByServiceIdAndEstablishmentId(Long serviceId, Long id);

    void deleteByEstablishmentId(Long id);

    void deleteByServiceId(Long id);

    Optional<EstablishmentService> findByEstablishmentIdAndServiceId(long establishmentId, long serviceId);
}
