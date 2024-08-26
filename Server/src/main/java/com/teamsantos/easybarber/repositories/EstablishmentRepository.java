package com.teamsantos.easybarber.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.Establishment;

@Repository
public interface EstablishmentRepository
        extends JpaRepository<Establishment, Long>, JpaSpecificationExecutor<EmployeeSchedule> {
    @NonNull
    Optional<Establishment> findById(@NonNull Long id);

    // @Query("""
    // SELECT DISTINCT new com.teamsantos.easybarber.DTO.EstablishmentDTO(e.id,
    // e.name, e.description, e.address, e.location, ST_Distance_Sphere(e.location,
    // :location) AS distance, e.nVotes, e.sumVotes)
    // FROM Establishment e
    // INNER JOIN EstablishmentService es ON :serviceType IS NULL OR es.service.id =
    // :serviceType
    // WHERE es.establishment.id = e.id
    // AND (:partialName IS NULL OR lower(e.name) LIKE concat('%',
    // lower(:partialName), '%'))
    // AND (:rating IS NULL OR (e.nVotes > 0 AND e.sumVotes / e.nVotes >= :rating))
    // ORDER BY ST_Distance_Sphere(e.location, :location) ASC
    // """)
    // Page<EstablishmentDTO> findClosestEstablishments(Point location, Long
    // serviceType, String partialName,
    // Double rating, Pageable pageable);

    // @Query("""
    // SELECT DISTINCT new com.teamsantos.easybarber.DTO.EstablishmentDTO(e.id,
    // e.name, e.description, e.address, e.location, e.nVotes, e.sumVotes)
    // FROM Establishment e
    // INNER JOIN EstablishmentService es ON :serviceType IS NULL OR es.service.id =
    // :serviceType
    // WHERE es.establishment.id = e.id AND :rating IS NULL OR (e.nVotes > 0 AND
    // e.sumVotes / e.nVotes >= :rating)
    // """)
    // Page<EstablishmentDTO> list(Long serviceType, Double rating, Pageable
    // pageable);
}
