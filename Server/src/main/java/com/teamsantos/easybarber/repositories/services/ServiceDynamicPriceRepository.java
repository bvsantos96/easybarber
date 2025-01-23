package com.teamsantos.easybarber.repositories.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.filters.ServiceDynamicFilter;
import com.teamsantos.easybarber.DTO.service.ServiceDynamicPriceDTO;
import com.teamsantos.easybarber.entities.ServiceDynamicPrice;
import com.teamsantos.easybarber.utils.Pair;
import com.teamsantos.easybarber.utils.Triple;

@Repository
public interface ServiceDynamicPriceRepository extends JpaRepository<ServiceDynamicPrice, Long> {
    @Query("""
            SELECT CASE WHEN sdpe IS NOT NULL THEN sdpe ELSE sdp END
            FROM EstablishmentServiceEmployee ese
            LEFT JOIN ServiceDynamicPrice sdpe ON sdpe.establishmentServiceEmployee.id = ese.id AND sdpe.validFrom <= :date AND (sdpe.validTo IS NULL OR sdpe.validTo >= :date)
            LEFT JOIN ServiceDynamicPrice sdp ON sdp.establishmentService.id = ese.service.id AND sdp.validFrom <= :date AND (sdp.validTo IS NULL OR sdp.validTo >= :date) AND sdp.establishmentServiceEmployee IS NULL
            WHERE
                ese.establishment.id = :establishmentId
                AND ese.service.service.id = :serviceId
                AND ese.employee.id = :employeeId
            """)
    ServiceDynamicPrice findByEstablishmentIdAndServiceIdAndEmployeeIdAndDate(long establishmentId, long serviceId,
            long employeeId, LocalDateTime date);

    @Query("""
            SELECT new com.teamsantos.easybarber.utils.Pair(dp.validFrom, dp.validTo)
            FROM ServiceDynamicPrice dp
            WHERE
                (
                    (dp.establishmentService.id = :establishmentServiceId AND dp.establishmentServiceEmployee IS NULL)
                    OR
                    (:establishmentServiceEmployeeId IS NOT NULL AND dp.establishmentServiceEmployee.id = :establishmentServiceEmployeeId)
                )
                AND dp.validFrom BETWEEN :from AND :to OR dp.validTo BETWEEN :from AND :to
            """)
    List<Pair<LocalDateTime, LocalDateTime>> list(long establishmentServiceId, Long establishmentServiceEmployeeId,
            LocalDateTime from, LocalDateTime to);

    @Query("""
            SELECT new com.teamsantos.easybarber.utils.Triple(dp.validFrom, dp.validTo ,dp.price)
            FROM ServiceDynamicPrice dp
            WHERE
                (
                    (dp.establishmentService.id = :establishmentServiceId AND dp.establishmentServiceEmployee IS NULL)
                    OR
                    (:establishmentServiceEmployeeId IS NOT NULL AND dp.establishmentServiceEmployee.id = :establishmentServiceEmployeeId)
                )
                AND dp.validFrom BETWEEN :from AND :to OR dp.validTo BETWEEN :from AND :to
            """)
    Triple<LocalDateTime, LocalDateTime, Double> findPriceByEstablishmentServiceIdAndEstablishmentServiceEmployeeIdAndDates(
            long establishmentServiceId,
            Long establishmentServiceEmployeeId, LocalDateTime from, LocalDateTime to);

    @Query("""
            SELECT EXISTS (
                SELECT 1
                FROM ServiceDynamicPrice dp
                WHERE (
                        (dp.establishmentService.id = :establishmentServiceId AND dp.establishmentServiceEmployee IS NULL)
                        OR
                        (dp.establishmentServiceEmployee.id = :establishmentServiceEmployeeId)
                    )
                    AND :date BETWEEN dp.validFrom AND dp.validTo
                )
            """)
    boolean exists(long establishmentServiceId, Long establishmentServiceEmployeeId, LocalDateTime date);

    @Query("""
                SELECT dp.price
                FROM ServiceDynamicPrice dp
                WHERE (
                        (dp.establishmentService.id = :establishmentServiceId AND dp.establishmentServiceEmployee IS NULL)
                        OR
                        (dp.establishmentServiceEmployee.id = :establishmentServiceEmployeeId)
                    )
                    AND :date BETWEEN dp.validFrom AND dp.validTo
            """)
    Double getDynamicPrice(long establishmentServiceId,
            Long establishmentServiceEmployeeId, LocalDateTime date);

    @Query("""
                SELECT new com.teamsantos.easybarber.DTO.service.ServiceDynamicPriceDTO(
                    dp.id,
                    dp.price,
                    dp.validFrom,
                    dp.validTo,
                    dp.establishmentServiceEmployee.id,
                    dp.establishmentService.id
                )
                FROM ServiceDynamicPrice dp
                WHERE (
                    (:#{#filter.establishmentServiceId} IS NOT NULL AND dp.establishmentService.id = :#{#filter.establishmentServiceId} AND dp.establishmentServiceEmployee IS NULL)
                    OR
                    (:#{#filter.establishmentServiceEmployeeId} IS NOT NULL AND dp.establishmentServiceEmployee.id = :#{#filter.establishmentServiceEmployeeId})
                    OR
                    (:#{#filter.establishmentServiceId} IS NULL AND :#{#filter.establishmentServiceEmployeeId} IS NULL)
                )
                AND (
                    :#{#filter.establishmentEmployeeId} IS NULL
                    OR dp.establishmentServiceEmployee.employee.id = :#{#filter.establishmentEmployeeId}
                )
                AND (
                    :#{#filter.establishmentId} IS NULL
                    OR dp.establishmentService.establishment.id = :#{#filter.establishmentId}
                )
                AND (
                    (:#{#filter.from} IS NULL AND :#{#filter.to} IS NULL)
                    OR
                    (:#{#filter.from} IS NOT NULL AND :#{#filter.to} IS NOT NULL
                        AND (dp.validFrom BETWEEN :#{#filter.from} AND :#{#filter.to}
                             OR dp.validTo BETWEEN :#{#filter.from} AND :#{#filter.to}))
                    OR
                    (:#{#filter.from} IS NOT NULL AND :#{#filter.to} IS NULL
                        AND (dp.validFrom >= :#{#filter.from} OR dp.validTo >= :#{#filter.from}))
                    OR
                    (:#{#filter.to} IS NOT NULL
                        AND (dp.validTo <= :#{#filter.to} OR dp.validFrom <= :#{#filter.to}))
                )
            """)
    Page<ServiceDynamicPriceDTO> findAllDTO(ServiceDynamicFilter filter, Pageable pageable);
}
