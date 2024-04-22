package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.entities.EstablishmentService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstablishmentServiceRepository extends JpaRepository<EstablishmentService, Long> {
    boolean existsByServiceIdAndEstablishmentId(Long serviceId, Long id);

    void deleteByEstablishmentId(Long id);

    void deleteByServiceId(Long id);
}
