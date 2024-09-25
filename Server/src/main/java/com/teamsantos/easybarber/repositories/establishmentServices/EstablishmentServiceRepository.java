package com.teamsantos.easybarber.repositories.establishmentServices;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.EstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceFullDTO;
import com.teamsantos.easybarber.DTO.filters.EstablishmentServiceFilter;
import com.teamsantos.easybarber.entities.EstablishmentService;
import com.teamsantos.easybarber.utils.Pair;

import jakarta.persistence.Tuple;

@Repository
public interface EstablishmentServiceRepository
        extends JpaRepository<EstablishmentService, Long> {

    void deleteByEstablishmentId(long id);

    void deleteByServiceId(long id);

    @Query("""
            SELECT es.service.duration
            FROM EstablishmentService es
            WHERE
                es.establishment.id = :establishmentId
            AND es.service.id = :serviceId
            """)
    Integer getDurationOfService(long establishmentId, long serviceId);

    @Query("""
            SELECT new com.teamsantos.easybarber.utils.Pair(es.id, es.service.duration)
            FROM EstablishmentService es
            WHERE
                es.establishment.id = :establishmentId
                AND es.service.id = :serviceId
            """)
    Pair<Long, Integer> getIdAndDuration(long establishmentId, long serviceId);

    @Query("""
            SELECT EXISTS (
            SELECT 1
                FROM EstablishmentService es
                WHERE
                    es.establishment.id = :establishmentId
                AND es.service.id = :serviceId
            )
            """)
    boolean existsByServiceIdAndEstablishmentId(long serviceId, long establishmentId);

    @Query("""
                SELECT com.teamsantos.easybarber.DTO.ServiceFullDTO(
                    es.id, es.service.name, es.service.description, es.service.duration, es.price, i,
                    es.service.serviceType.id, es.service.serviceType.name, es.service.serviceType, es.service.serviceType.imageURL,
                    es.service.employee.id, es.service.employee.user.name
                ) FROM EstablishmentService es
                LEFT JOIN es.service.images i
                WHERE
                    es.establishment.id = :establishmentId
                AND es.service.id = :serviceId
            """)
    ServiceFullDTO findByEstablishmentIdAndServiceId(long establishmentId, long serviceId);

    @Query("""
                SELECT new com.teamsantos.easybarber.DTO.EstablishmentServiceDTO(ess.service.id, ess.service.name, ess.service.description, ess.service.duration, si.data,
                ess.service.serviceType.id, ess.service.serviceType.name, ess.service.serviceType.description, ess.service.serviceType.imageURL,
                ess.service.employee.id, ess.service.employee.user.name, ei.data,
                ess.establishment.id, ess.establishment.name, esi.data,
                ess.price, ess.active)
                FROM EstablishmentService ess
                LEFT JOIN ServiceImage si ON :#{#filter.includeServiceImage} = true AND si.isMain = true and si.entity.id = ess.service.id
                LEFT JOIN EmployeeImage ei ON :#{#filter.includeEmployeeImage} = true AND ei.isMain = true and ei.entity.id = ess.service.employee.id
                LEFT JOIN EstablishmentImage esi ON :#{#filter.includeEstablishmentImage} = true AND esi.isMain = true and esi.entity.id = ess.establishment.id
                WHERE (:#{#filter.establishmentId} is null or ess.establishment.id = :#{#filter.establishmentId})
                AND (:#{#filter.employeeId} is null or ess.service.employee.id = :#{#filter.employeeId})
                AND (:#{#filter.serviceTypeId} is null or ess.service.serviceType.id = :#{#filter.serviceTypeId})
                AND (:#{#filter.establishmentId} is null or ess.establishment.id = :#{#filter.establishmentId})
                AND (:#{#filter.name} is null or lower(ess.service.name) like lower(concat('%', :#{#filter.name}, '%')))
                AND (:#{#filter.description} is null or lower(ess.service.description) like lower(concat('%', :#{#filter.description}, '%')))
            """)
    Page<EstablishmentServiceDTO> findAll(@Param("filter") EstablishmentServiceFilter filter, Pageable pageable);

    @Query("""
                SELECT new com.teamsantos.easybarber.DTO.ServiceDTO(ess.service.id, ess.service.employee.id, ess.service.serviceType.id, ess.service.name, ess.service.description, ess.price, ess.service.duration)
                FROM EstablishmentService ess
                WHERE (:#{#filter.establishmentId} is null or ess.establishment.id = :#{#filter.establishmentId})
                AND (:#{#filter.employeeId} is null or ess.service.employee.id = :#{#filter.employeeId})
                AND (:#{#filter.serviceTypeId} is null or ess.service.serviceType.id = :#{#filter.serviceTypeId})
                AND (:#{#filter.establishmentId} is null or ess.establishment.id = :#{#filter.establishmentId})
                AND (:#{#filter.name} is null or lower(ess.service.name) like lower(concat('%', :#{#filter.name}, '%')))
                AND (:#{#filter.description} is null or lower(ess.service.description) like lower(concat('%', :#{#filter.description}, '%')))
            """)
    Page<ServiceDTO> findAllServiceDTO(EstablishmentServiceFilter filter, Pageable pageable);

    @Query(value = """
            SELECT
            s1_0.employee_id,
            u1_0.name,
            e1_0.description,
            CONCAT(u1_0.country_mobile, u1_0.mobile),
            CASE WHEN e1_0.n_votes = 0 THEN 0 ELSE (e1_0.sum_votes / e1_0.n_votes) END,
            e1_0.n_votes,
            GROUP_CONCAT(DISTINCT s1_0.service_type_id) as availableServices,
            (
                SELECT GROUP_CONCAT(ei_sub.data ORDER BY ei_sub.id ASC)
                FROM (
                    SELECT data, id
                    FROM employee_image
                    WHERE entity_id = e1_0.id
                    ORDER BY id ASC
                    LIMIT 2
                ) ei_sub
            ) AS images
            FROM establishment_service es1_0
            JOIN service s1_0 ON s1_0.id = es1_0.service_id
            JOIN employee e1_0 ON e1_0.id = s1_0.employee_id
            JOIN user u1_0 ON u1_0.id = e1_0.user
            WHERE es1_0.establishment_id = 1 AND s1_0.employee_id = 2
            GROUP BY s1_0.employee_id;
            """, nativeQuery = true)
    Optional<Tuple> findEmployeeInformation(long establishmentId, long employeeId);
}
