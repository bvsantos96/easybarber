package com.teamsantos.easybarber.repositories.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.ServiceDynamicPrice;
import com.teamsantos.easybarber.entities.ServiceDynamicPrice.ServiceDynamicPriceId;
import com.teamsantos.easybarber.utils.Pair;
import com.teamsantos.easybarber.utils.Triple;

@Repository
public interface ServiceDynamicPriceRepository extends JpaRepository<ServiceDynamicPrice, ServiceDynamicPriceId> {
    @Query("""
            SELECT CASE WHEN sdpe IS NOT NULL THEN sdpe ELSE sdp END
            FROM EstablishmentServiceEmployee ese
            LEFT JOIN ServiceDynamicPrice sdpe ON sdpe.establishmentServiceEmployee.id = ese.id AND sdpe.id.validFrom <= :date AND (sdpe.id.validTo IS NULL OR sdpe.id.validTo >= :date)
            LEFT JOIN ServiceDynamicPrice sdp ON sdp.establishmentService.id = ese.service.id AND sdp.id.validFrom <= :date AND (sdp.id.validTo IS NULL OR sdp.id.validTo >= :date) AND sdp.establishmentServiceEmployee IS NULL
            WHERE
                ese.establishment.id = :establishmentId
                AND ese.service.service.id = :serviceId
                AND ese.employee.id = :employeeId
            """)
    ServiceDynamicPrice findByEstablishmentIdAndServiceIdAndEmployeeIdAndDate(long establishmentId, long serviceId,
            long employeeId, LocalDateTime date);

    @Query("""
            SELECT new com.teamsantos.easybarber.utils.Pair(dp.id.validFrom, dp.id.validTo)
            FROM ServiceDynamicPrice dp
            WHERE
                (
                    (dp.establishmentService.id = :establishmentServiceId AND dp.establishmentServiceEmployee IS NULL)
                    OR
                    dp.establishmentServiceEmployee.id = :establishmentServiceEmployeeId
                )
                AND dp.id.validFrom <= :from
                AND (dp.id.validTo IS NULL OR dp.id.validTo >= :to)
            """)
    List<Pair<LocalDateTime, LocalDateTime>> list(long establishmentServiceId, Long establishmentServiceEmployeeId,
            LocalDateTime from, LocalDateTime to);

    @Query("""
            SELECT new com.teamsantos.easybarber.utils.Triple(dp.id.validFrom, dp.id.validTo ,dp.price)
            FROM ServiceDynamicPrice dp
            WHERE
                (
                    (dp.establishmentService.id = :establishmentServiceId AND dp.establishmentServiceEmployee IS NULL)
                    OR
                    dp.establishmentServiceEmployee.id = :establishmentServiceEmployeeId
                )
                AND (:to IS NULL AND dp.id.validFrom = :from)
                AND (:to IS NOT NULL AND dp.id.validFrom <= :from AND dp.id.validTo >= :to)
            """)
    Triple<LocalDateTime, LocalDateTime, Double> findPriceByEstablishmentServiceIdAndEstablishmentServiceEmployeeIdAndDates(
            long establishmentServiceId,
            Long establishmentServiceEmployeeId, LocalDateTime from, LocalDateTime to);

    @Query("""
            SELECT CASE
                WHEN dpe IS NOT NULL THEN dpe.price
                WHEN dp IS NOT NULL THEN dp.price
                ELSE e.service.price
            END, dpe IS NOT NULL AND dp IS NOT NULL
            FROM EstablishmentServiceEmployee e
            LEFT JOIN e.dynamicPrices dpe ON dpe.id.validFrom <= :of AND dpe.id.validTo >= :of
            LEFT JOIN e.service.dynamicPrices dp ON dp.id.validFrom <= :of AND dp.id.validTo >= :of AND dp.establishmentServiceEmployee IS NULL
            WHERE e.service.id = :establishmentServiceId
                AND e.employee.id = :establishmentStaffId
            """)
    Pair<Double, Boolean> getPriceWithValidation(Long establishmentServiceId, Long establishmentStaffId,
            LocalDateTime of);
}
