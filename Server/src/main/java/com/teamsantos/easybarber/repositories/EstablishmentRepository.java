package com.teamsantos.easybarber.repositories;

import java.util.Optional;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.EstablishmentDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.Establishment;

@Repository
public interface EstablishmentRepository
        extends JpaRepository<Establishment, Long>, JpaSpecificationExecutor<EmployeeSchedule> {
    @NonNull
    Optional<Establishment> findById(@NonNull Long id);

    @Query("""
            SELECT EXISTS(
                SELECT 1
                from Establishment e
                where e.name = :name
                )
            """)
    boolean existsByName(String name);

    @Query("""
                SELECT e FROM Establishment e JOIN FETCH e.staff WHERE e.id = :establishmentId
            """)
    Optional<Establishment> findByIdWithStaff(long establishmentId);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.EstablishmentDTO(e.id,
            e.name, e.description, e.address, e.location, ST_Distance_Sphere(e.location,
            :location) AS distance, e.nVotes, e.sumVotes, i)
            FROM EstablishmentService es
            LEFT JOIN Establishment e ON e.id = es.establishment.id
            LEFT JOIN EstablishmentImage i ON i.isMain = true AND i.entity.id = e.id
            WHERE (:serviceType IS NULL OR es.service.id = :serviceType)
            AND (:partialName IS NULL OR lower(e.name) LIKE concat('%', lower(:partialName), '%'))
            AND (:rating IS NULL OR (e.nVotes > 0 AND e.sumVotes / e.nVotes >= :rating))
            ORDER BY ST_Distance_Sphere(e.location, :location) ASC
            """)
    Page<EstablishmentDTO> findClosestEstablishments(Point location, Long serviceType, String partialName,
            Double rating, Pageable pageable);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.EstablishmentDTO(e.id,
            e.name, e.description, e.address, e.location, ST_Distance_Sphere(e.location,
            :location) AS distance, e.nVotes, e.sumVotes, i)
            FROM Establishment e
            LEFT JOIN EstablishmentImage i ON i.isMain = true AND i.entity.id = e.id
            WHERE (:partialName IS NULL OR lower(e.name) LIKE concat('%', lower(:partialName), '%'))
            AND (:rating IS NULL OR (e.nVotes > 0 AND e.sumVotes / e.nVotes >= :rating))
            ORDER BY ST_Distance_Sphere(e.location, :location) ASC
            """)
    Page<EstablishmentDTO> findClosestEstablishments(Point location, String partialName,
            Double rating, Pageable pageable);

    @Query("""
            SELECT DISTINCT new com.teamsantos.easybarber.DTO.EstablishmentDTO(
                e.id, e.name, e.description, e.address, e.location, e.nVotes, e.sumVotes, i)
            FROM Establishment e
            LEFT JOIN e.images i
            INNER JOIN EstablishmentService es ON es.establishment.id = e.id
            WHERE (:serviceType IS NULL OR es.service.id = :serviceType)
            AND (:rating IS NULL OR (e.nVotes > 0 AND e.sumVotes / e.nVotes >= :rating))
            """)
    Page<EstablishmentDTO> list(@Param("serviceType") Long serviceType, @Param("rating") Double rating,
            Pageable pageable);

    @Query("SELECT es.establishment FROM EstablishmentStaff es WHERE es.employee.id = :employeeId AND (:admin = false OR es.admin = true)")
    Page<Establishment> findEstablishmentsByEmployeeId(Long employeeId, boolean admin, Pageable pageable);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.EstablishmentDTO(e.id, e.name, e.description, e.address, e.location, e.nVotes, e.sumVotes, e.images)
            FROM Establishment e
            WHERE e.id = :id
            """)
    Optional<EstablishmentDTO> findByIdDTO(long id);
}
