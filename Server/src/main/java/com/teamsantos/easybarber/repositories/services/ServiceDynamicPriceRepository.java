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
            SELECT *
            FROM EstablishmentServiceEmployee ese
            LEFT JOIN ServiceDynamicPrice sdpe ON sdpe.establishmentServiceEmployee.id = ese.id AND sdpe.from <= :date AND (sdpe.to IS NULL OR sdpe.to >= :date)
            LEFT JOIN ServiceDynamicPrice sdp ON sdp.establishmentService.id = ese.service.id AND sdp.from <= :date AND (sdp.to IS NULL OR sdp.to >= :date) AND sdp.establishmentServiceEmployee IS NULL
            WHERE
                ese.establishment.id = :establishmentId
                AND ese.service.service.id = :serviceId
                AND ese.employee.id = :employeeId
            """)
    ServiceDynamicPrice findByEstablishmentIdAndServiceIdAndEmployeeIdAndDate(long establishmentId, long serviceId,
            long employeeId, LocalDateTime date);

    @Query("""
            SELECT dp.date
            FROM ServiceDynamicPrice dp
            WHERE
                (
                    (dp.establishmentService.id = :establishmentServiceId AND dp.establishmentServiceEmployee IS NULL)
                    OR
                    dp.establishmentServiceEmployee.id = :establishmentServiceEmployeeId
                )
                AND dp.from <= :from
                AND (dp.to IS NULL OR dp.to >= :to)
            """)
    List<LocalDateTime> list(long establishmentId, long establishmentServiceId, Long establishmentServiceEmployeeId,
            LocalDateTime from, LocalDateTime to);

    @Query("""
            SELECT new com.teamsantos.easybarber.utils.Triple(dp.from, dp.to ,dp.price)
            FROM ServiceDynamicPrice dp
            WHERE
                (
                    (dp.establishmentService.id = :establishmentServiceId AND dp.establishmentServiceEmployee IS NULL)
                    OR
                    dp.establishmentServiceEmployee.id = :establishmentServiceEmployeeId
                )
                AND (:to IS NULL AND dp.from == :date)
                AND (:to IS NOT NULL AND dp.from <= :date AND dp.to >= :to)
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
            LEFT JOIN e.dynamicPrices dpe ON dpe.from <= :of AND dpe.to >= :of
            LEFT JOIN e.service.dynamicPrices dp ON dp.from <= :of AND dp.to >= :of AND dp.establishmentServiceEmployee IS NULL
            WHERE e.service.id = :establishmentServiceId
                AND e.employee.id = :establishmentStaffId
            """)
    Pair<Double, Boolean> getPriceWithValidation(Long establishmentServiceId, Long establishmentStaffId,
            LocalDateTime of);
}
