package com.teamsantos.easybarber.repositories.establishmentServices;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.NameIdImageDTO;
import com.teamsantos.easybarber.DTO.establishment.service.EstablishmentServiceDTO;
import com.teamsantos.easybarber.DTO.filters.EstablishmentServiceFilter;
import com.teamsantos.easybarber.DTO.service.ServiceDTO;
import com.teamsantos.easybarber.DTO.service.ServiceDynamicPriceDTO;
import com.teamsantos.easybarber.DTO.service.ServiceFullDTO;
import com.teamsantos.easybarber.DTO.service.ServiceListDTO;
import com.teamsantos.easybarber.entities.EstablishmentService;

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
            SELECT new com.teamsantos.easybarber.DTO.service.ServiceDynamicPriceDTO(
                ese.service.id,
                ese.service.service.duration,
                CASE
                    WHEN sdpe IS NOT NULL THEN sdpe.price
                    WHEN sdp IS NOT NULL THEN sdp.price
                    ELSE ese.service.price
                END,
                sdpe IS NOT NULL AND sdp IS NOT NULL
            )
            FROM EstablishmentServiceEmployee ese
            LEFT JOIN ese.dynamicPrices sdpe ON sdpe.validFrom <= :date AND (sdpe.validTo IS NULL OR sdpe.validTo >= :date)
            LEFT JOIN ese.service.dynamicPrices sdp ON sdp.validFrom <= :date AND (sdp.validTo IS NULL OR sdp.validTo >= :date) AND sdp.establishmentServiceEmployee IS NULL
            WHERE
                ese.establishment.id = :establishmentId
                AND ese.service.service.id = :serviceId
                AND ese.employee.id = :employeeId
            """)
    ServiceDynamicPriceDTO getIdAndDurationAndPrice(
            long establishmentId,
            long serviceId,
            long employeeId,
            LocalDateTime date);

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
                SELECT com.teamsantos.easybarber.DTO.service.ServiceFullDTO(
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
                SELECT new com.teamsantos.easybarber.DTO.establishment.service.EstablishmentServiceDTO(ess.service.id, ess.service.name, ess.service.description, ess.service.duration, si.data,
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
            SELECT new com.teamsantos.easybarber.DTO.service.ServiceDTO(ess.service.id, ess.service.employee.id, ess.service.serviceType.id, ess.service.name, ess.service.description,
            CASE
                WHEN dp IS NOT NULL THEN dp.price
                ELSE ess.price
            END , ess.service.duration)
            FROM EstablishmentService ess
            LEFT JOIN ess.dynamicPrices dp ON dp.validFrom <= :#{#filter.date} AND (dp.validTo IS NULL OR dp.validTo >= :#{filter.date}) AND dp.establishmentServiceEmployee IS NULL
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
            s.employee_id,
            u.name,
            e.description,
            CONCAT(u.country_mobile, u.mobile),
            CASE WHEN e.n_votes = 0 THEN 0 ELSE (e.sum_votes / e.n_votes) END,
            e.n_votes,
            GROUP_CONCAT(DISTINCT s.service_type_id) as availableServices,
            (
                SELECT GROUP_CONCAT(CONCAT(ei.id, ',', ei.is_main, ',', ei.data) ORDER BY ei.is_main DESC, ei.id DESC SEPARATOR ';')
                FROM (
                    SELECT id, data, is_main
                    FROM employee_image
                    WHERE entity_id = e.id
                    ORDER BY is_main DESC, id DESC
                    LIMIT 2
                ) ei
            ) AS images
            FROM establishment_service es
            JOIN service s ON s.id = es.service_id
            JOIN employee e ON e.id = s.employee_id
            JOIN user u ON u.id = e.user
            WHERE es.establishment_id = :establishmentId AND s.employee_id = :employeeId
            GROUP BY s.employee_id;
            """, nativeQuery = true)
    Optional<Tuple> findEmployeeInformation(long establishmentId, long employeeId);

    @Query(value = """
            SELECT
            es.establishment_id,
            e.name,
            e.description,
            e.address,
            e.location,
            (
                SELECT GROUP_CONCAT(CONCAT(ei.id, ',', ei.is_main, ',', ei.data) ORDER BY ei.is_main DESC, ei.id DESC SEPARATOR ';')
                FROM (
                    SELECT id, data, is_main
                    FROM establishment_image
                    WHERE entity_id = e.id
                    ORDER BY is_main DESC, id DESC
                    LIMIT 2
                ) ei
            ) AS images,
            e.n_votes,
            CASE WHEN e.n_votes = 0 THEN 0 ELSE (e.sum_votes / e.n_votes) END,
            GROUP_CONCAT(DISTINCT s.service_type_id) as availableServices
            FROM establishment_service es
            JOIN establishment e ON e.id = es.establishment_id
            JOIN service s ON s.id = es.service_id
            WHERE es.establishment_id = :establishmentId
            GROUP BY es.establishment_id;
            """, nativeQuery = true)
    Optional<Tuple> findEstablishmentInformation(long establishmentId);

    @Query("""
                SELECT DISTINCT es.service.serviceType.id
                FROM EstablishmentService es
                WHERE es.establishment.id = :establishmentId
            """)
    List<Long> findEstablishmentAvailableServiceTypes(long establishmentId);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.service.ServiceListDTO(
                se.service.id,
                se.service.serviceType.id,
                se.service.name,
                se.service.description,
                CASE
                    WHEN dpe IS NOT NULL THEN dpe.price
                    ELSE se.price
                END,
                img
            )
            FROM EstablishmentService se
            LEFT JOIN se.service.images img ON img.isMain = true
            LEFT JOIN se.dynamicPrices dpe ON dpe.validFrom <= :date AND (dpe.validTo IS NULL OR dpe.validTo >= :date) AND dpe.establishmentServiceEmployee IS NULL
            WHERE se.establishment.id = :establishmentId
            """)
    List<ServiceListDTO> listServices(long establishmentId, LocalDateTime date);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.NameIdImageDTO(
                    es.service.employee.id,
                    es.service.employee.user.name,
                    i.data
                )
                FROM EstablishmentService es
                LEFT JOIN es.service.employee.images i
                WHERE es.establishment.id = :establishmentId
                AND es.service.id = :serviceId
                AND i.isMain = true
            """)
    List<NameIdImageDTO> listEmployeesOfEstablishmentService(Long establishmentId, Long serviceId);

    @Query("""
                SELECT new com.teamsantos.easybarber.DTO.NameIdImageDTO(
                        es.service.serviceType.id,
                        es.service.serviceType.name,
                        es.service.serviceType.imageURL
                    )
                    FROM EstablishmentService es
                    WHERE es.establishment.id = :establishmentId
                    AND es.service.id = :serviceId
            """)
    Optional<EstablishmentService> findByEstablishmentAndService(Long establishmentId, Long serviceId);

    @Query("""
                SELECT es.id
                FROM EstablishmentService es
                WHERE
                    es.establishment.id = :establishmentId
                    AND es.service.id = :serviceId
            """)
    Optional<Long> findIdByEstablishmentAndService(long establishmentId, long serviceId);

    @Query("""
                SELECT es.service.id
                FROM EstablishmentService es
                WHERE
                    es.id = :establishmentServiceId
            """)
    Long getServiceId(Long establishmentServiceId);
}
