package com.teamsantos.easybarber.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.EmployeeDTO;
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
    boolean isAdminOfEstablishment(long employeeId, long establishmentId);

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
    boolean isEmployeeOfEstablishment(long employeeId, long establishmentId);

    void deleteByEstablishmentId(long id);

    void deleteByEmployeeId(long id);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.EmployeeDTO(
                es.employee
            ) FROM EstablishmentStaff es
            WHERE
                es.establishment.id = :establishmentId
            AND (NOT :onlyActive OR es.active = :onlyActive)
            """)
    List<EmployeeDTO> findEmployeeByEstablishmentIdAndActiveFilter(long establishmentId, boolean onlyActive);
}
