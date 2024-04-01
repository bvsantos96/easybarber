package com.teamsantos.easybarber.repositories;

import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.EstablishmentService;

@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {
    @NonNull
    Optional<Establishment> findById(@NonNull Long id);

    @Query("SELECT new com.teamsantos.easybarber.DTO.BaseEstablishmentDTO(e.id, e.name, e.description) FROM Establishment e")
    Page<BaseEstablishmentDTO> findAllBase(Pageable pageable);

    @Query("SELECT new com.teamsantos.easybarber.DTO.EstablishmentDTO(e.id, e.name, e.description, e.address, e.location, ST_Distance_Sphere(e.location, :location)) "
            +
            "FROM Establishment e " +
            "ORDER BY ST_Distance_Sphere(e.location, :location) ASC ")
    Page<EstablishmentDTO> findClosestEstablishments(Point location, Pageable pageable);

    @Query("""
            SELECT e FROM Establishment e
            JOIN e.staff es
            WHERE es.employee.id = :employeeId
            AND es.admin = :admin""")
    Page<Establishment> findEstablishmentsByEmployeeId(Long employeeId, boolean admin, Pageable pageable);

    @Query("SELECT e.services FROM Establishment e WHERE e.id = :establishmentId")
    Page<EstablishmentService> findServicesByEstablishmentId(Long establishmentId, Pageable pageable);
}
