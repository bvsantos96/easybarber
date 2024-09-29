package com.teamsantos.easybarber.repositories.establishmentServices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
