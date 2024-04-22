package com.teamsantos.easybarber.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
            FROM Service s
            JOIN s.employee e
            JOIN e.user u
            WHERE s.id = :serviceId
            AND u.mobileInformation = :mobileInformation""")
    boolean checkIfEmployeeIsServiceOwner(Long serviceId, String mobileInformation);

    Page<Service> findByEmployeeId(Long id, Pageable pageable);

    @Query("""
            SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Service s
            WHERE s.employee.id = :employeeId
            AND s.serviceType.id = :serviceTypeId
            AND s.name = :name
            AND s.description = :description""")
    boolean existsByEmployeeIdServiceTypeIdNameAndDescription(Long employeeId, Long serviceTypeId, String name,
            String description);

    Page<Service> findByServiceTypeId(Long serviceTypeId, Pageable pageable);

    @Query("""
             SELECT s
             FROM Service s
             WHERE :serviceTypeId IS NULL OR s.serviceType.id = :serviceTypeId
            """)
    Page<Service> listByServiceTypeId(Long serviceTypeId, Pageable pageable);
}
