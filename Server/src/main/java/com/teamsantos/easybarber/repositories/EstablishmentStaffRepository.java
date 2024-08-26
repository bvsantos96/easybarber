package com.teamsantos.easybarber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.EstablishmentStaff;

@Repository
public interface EstablishmentStaffRepository extends JpaRepository<EstablishmentStaff, Long> {
    @Query("""
            SELECT EXISTS (
                SELECT 1
                FROM EstablishmentStaff es
                WHERE
                    es.employee_id = :employeeId
                AND es.establishment_id = :establishmentId
                AND es.approved = true
                AND es.deleted = false
                AND es.admin = true
                LIMIT 1
            )
            """)
    boolean isAdminOfEstablishment(Long employeeId, Long establishmentId);

    @Query("""
            SELECT EXISTS (
                SELECT 1
                FROM EstablishmentStaff es
                WHERE
                    es.employee_id = :employeeId
                AND es.establishment_id = :establishmentId
                AND es.approved = true
                AND es.deleted = false
                LIMIT 1
            )
            """)
    boolean isEmployeeOfEstablishment(Long employeeId, Long establishmentId);

    void deleteByEstablishmentId(Long id);

    void deleteByEmployeeId(Long id);
}
