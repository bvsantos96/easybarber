package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.entities.EstablishmentStaff;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EstablishmentStaffRepository extends JpaRepository<EstablishmentStaff, Long> {
    @Cacheable("establishmentStaff")
    @Query("""
            SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END
            FROM EstablishmentStaff es
            WHERE
                es.employee.id = :employeeId
            AND es.establishment.id = :establishmentId
            AND es.approved = true
            AND es.deleted = false
            AND es.admin = true
            """)
    boolean isAdminOfEstablishment(Long employeeId, Long establishmentId);

    @Query("""
            SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END
            FROM EstablishmentStaff es
            WHERE
                es.employee.id = :employeeId
            AND es.establishment.id = :establishmentId
            AND es.approved = true
            AND es.deleted = false
            """)
    boolean isEmployeeOfEstablishment(Long employeeId, Long establishmentId);

    void deleteByEstablishmentId(Long id);

    void deleteByEmployeeId(Long id);
}
