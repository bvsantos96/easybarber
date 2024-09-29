package com.teamsantos.easybarber.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.employee.EmployeeDTO;
import com.teamsantos.easybarber.entities.EstablishmentStaff;

@Repository
public interface EstablishmentStaffRepository extends JpaRepository<EstablishmentStaff, Long> {
    @Query("""
            SELECT EXISTS (
                SELECT 1
                FROM EstablishmentStaff es
                WHERE
                    es.employee.id = :employeeId
                AND es.establishment.id = :establishmentId
                AND es.approved = true
                AND es.deleted = false
                AND es.admin = true
            )
            """)
    boolean isAdminOfEstablishment(long employeeId, long establishmentId);

    @Query("""
            SELECT EXISTS (
                SELECT 1
                FROM EstablishmentStaff es
                WHERE
                    es.employee.id = :employeeId
                AND es.establishment.id = :establishmentId
                AND es.approved = true
                AND es.deleted = false

            )
            """)
    boolean isEmployeeOfEstablishment(long employeeId, long establishmentId);

    @Modifying
    @Query("""
                DELETE FROM EstablishmentStaff es
                WHERE es.establishment.id = :id
            """)
    void deleteByEstablishmentId(long id);

    void deleteByEmployeeId(long id);

    @Modifying
    @Query("""
                DELETE FROM EstablishmentStaff es
                WHERE es.establishment.id = :establishmentId
                    AND es.employee.id = :employeeId
            """)
    void deletebyEstablishmentIdAndEmployeeId(long establishmentId, long employeeId);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.employee.EmployeeDTO(
                es.employee
            ) FROM EstablishmentStaff es
            WHERE
                es.establishment.id = :establishmentId
            AND (:onlyActive = false OR es.deleted = false AND es.approved = true)
            """)
    List<EmployeeDTO> findEmployeeByEstablishmentIdAndActiveFilter(long establishmentId, boolean onlyActive);

    @Query("""
                SELECT es.id
                FROM EstablishmentStaff es
                WHERE es.employee.id = :employeeId
                    AND es.establishment.id = :establishmentId
            """)
    Optional<Long> findIdByEstablishmentAndEmployee(long establishmentId, long employeeId);
}
