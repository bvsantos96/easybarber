package com.teamsantos.easybarber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    @Query("""
            SELECT EXISTS (
                SELECT 1
                FROM Service s
                JOIN s.employee e
                JOIN e.user u
                WHERE s.id = :serviceId
                AND u.mobileInformation = :mobileInformation
                LIMIT 1)
            """)
    boolean checkIfEmployeeIsServiceOwner(Long serviceId, String mobileInformation);

    @Query("""
            SELECT EXISTS (
                SELECT 1
                Service s
                WHERE s.employee.id = :employeeId
                AND s.serviceType.id = :serviceTypeId
                AND s.name = :name
                AND s.description = :description
                LIMIT 1)
            """)
    boolean existsByEmployeeIdServiceTypeIdNameAndDescription(Long employeeId, Long serviceTypeId, String name,
            String description);

    void deleteByEmployeeId(Long id);
}
