package com.teamsantos.easybarber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.EstablishmentService;

@Repository
public interface EstablishmentServiceRepository extends JpaRepository<EstablishmentService, Long> {
    boolean existsByServiceIdAndEstablishmentId(Long serviceId, Long id);
}
