package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.entities.Establishment;
import org.locationtech.jts.geom.Point;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Long>, JpaSpecificationExecutor<EmployeeSchedule> {
 {
    @NonNull
    @Cacheable("establishment")
    Optional<Establishment> findById(@NonNull Long id);

    @Query("SELECT new com.teamsantos.easybarber.DTO.BaseEstablishmentDTO(e.id, e.name, e.description) FROM Establishment e")
    Page<BaseEstablishmentDTO> findAllBase(Pageable pageable);

    @Query("""
                SELECT DISTINCT new com.teamsantos.easybarber.DTO.EstablishmentDTO(e.id, e.name, e.description, e.address, e.location, ST_Distance_Sphere(e.location, :location) AS distance, e.nVotes, e.sumVotes)
                FROM Establishment e
                INNER JOIN EstablishmentService es ON :serviceType IS NULL OR es.service.id = :serviceType
                WHERE es.establishment.id = e.id
                AND (:partialName IS NULL OR lower(e.name) LIKE concat('%', lower(:partialName), '%'))
                AND (:rating IS NULL OR (e.nVotes > 0 AND e.sumVotes / e.nVotes >= :rating))
                ORDER BY ST_Distance_Sphere(e.location, :location) ASC
            """)
    Page<EstablishmentDTO> findClosestEstablishments(Point location, Long serviceType, String partialName,
            Double rating, Pageable pageable);

    @Query("""
            SELECT DISTINCT new com.teamsantos.easybarber.DTO.EstablishmentDTO(e.id, e.name, e.description, e.address, e.location, e.nVotes, e.sumVotes)
            FROM Establishment e
            INNER JOIN EstablishmentService es ON :serviceType IS NULL OR es.service.id = :serviceType
            WHERE es.establishment.id = e.id AND :rating IS NULL OR (e.nVotes > 0 AND e.sumVotes / e.nVotes >= :rating)
            """)
    Page<EstablishmentDTO> list(Long serviceType, Double rating, Pageable pageable);

    @Query("SELECT es.establishment FROM EstablishmentStaff es WHERE es.employee.id = :employeeId AND (:admin = false OR es.admin = true)")
    Page<Establishment> findEstablishmentsByEmployeeId(Long employeeId, boolean admin, Pageable pageable);

    @Query("SELECT new com.teamsantos.easybarber.DTO.ServiceDTO(es.service.id, es.service.employee.id, es.service.serviceType.id, es.service.name, es.service.description, es.price) FROM EstablishmentService es WHERE es.establishment.id = :establishmentId AND es.active = true")
    Page<ServiceDTO> findServicesByEstablishmentId(Long establishmentId, Pageable pageable);

    boolean existsByName(String name);
}
