package com.teamsantos.easybarber.repositories.establishmentServices;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.NameIdImagePriceDTO;
import com.teamsantos.easybarber.entities.EstablishmentServiceEmployee;

@Repository
public interface EstablishmentServiceEmployeeRepository extends JpaRepository<EstablishmentServiceEmployee, Long> {
    @Modifying
    @Query("""
            DELETE
            FROM EstablishmentServiceEmployee e
            WHERE e.establishment.id = :establishmentId
                AND e.service.service.id = :serviceId
                AND e.employee.id = :employeeId
            """)
    void deleteByEstablishmentIdAndServiceIdAndEmployeeId(long establishmentId, long serviceId,
            long employeeId);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.NameIdImagePriceDTO(
                e.employee.id,
                e.employee.employee.user.name,
                img.data,
                CASE
                    WHEN dpe IS NOT NULL THEN dpe.price
                    WHEN dp IS NOT NULL THEN dp.price
                    ELSE e.service.price
                END AS price
            )
            FROM EstablishmentServiceEmployee e
            JOIN EmployeeSchedule es ON es.employee.id = e.employee.id AND es.establishment.id = e.establishment.id
            LEFT JOIN e.employee.employee.images img ON img.isMain = true
            LEFT JOIN e.dynamicPrices dpe ON dpe.id.validFrom <= :date AND dpe.id.validTo >= :date
            LEFT JOIN e.service.dynamicPrices dp ON dp.id.validFrom <= :date AND dp.id.validTo >= :date AND dp.establishmentServiceEmployee IS NULL
            WHERE e.service.id = :establishmentServiceId
            GROUP BY e.employee.id, e.employee.employee.user.name, img.data, price
            """)
    List<NameIdImagePriceDTO> listEmployeesOfEstablishmentService(long establishmentServiceId, LocalDateTime date);

    @Query("""
            SELECT ese.id
            FROM EstablishmentServiceEmployee ese
            WHERE
                ese.service.id = :establishmentServiceId
                AND ese.employee.id = :establishmentStaffId
            """)
    Long getIdByEstablishmentServiceIdAndEstablishmentStaffId(long establishmentServiceId, long establishmentStaffId);
}
