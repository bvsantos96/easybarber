package com.teamsantos.easybarber.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.employee.EmployeeDTO;
import com.teamsantos.easybarber.DTO.employee.EmployeeListDTO;
import com.teamsantos.easybarber.DTO.filters.EmployeeFilter;
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
            SELECT new com.teamsantos.easybarber.DTO.employee.EmployeeListDTO(
                es.employee,
                ei.data,
                CASE WHEN ex IS NOT NULL THEN true ELSE false END,
                COALESCE(ex.message, '')
            )
            FROM EstablishmentStaff es
            LEFT JOIN EmployeeImage ei ON ei.isMain = true AND ei.entity.id = es.employee.id
            LEFT JOIN es.employee.exceptions ex ON ex.date = :date AND
                 (ex.establishment IS NULL OR ex.establishment.id = :establishmentId)
            WHERE es.establishment.id = :establishmentId
            AND (:#{#filter.hideDeleted} IS NULL OR es.deleted = CASE WHEN :#{#filter.hideDeleted} = true THEN false ELSE es.deleted END)
            AND (:#{#filter.hideNotApproved} IS NULL OR es.approved = :#{#filter.hideNotApproved})
            AND (:#{#filter.name} IS NULL OR es.employee.user.name LIKE :#{#filter.name})
            AND (:#{#filter.mobileInformation} IS NULL OR es.employee.user.mobileInformation LIKE :#{#filter.mobileInformation})
            AND (:#{#filter.serviceTypeIds} IS NULL OR EXISTS (
                SELECT 1
                FROM es.employee.services s
                WHERE s.serviceType.id IN :#{#filter.serviceTypeIds}
            ))
            AND (:#{#filter.greaterThanRating} IS NULL OR
                (CASE WHEN es.employee.nVotes > 0 THEN (es.employee.sumVotes / es.employee.nVotes) ELSE 0 END) >= :#{#filter.greaterThanRating})
            AND (:#{#filter.lessThanRating} IS NULL OR
                (CASE WHEN es.employee.nVotes > 0 THEN (es.employee.sumVotes / es.employee.nVotes) ELSE 0 END) <= :#{#filter.lessThanRating})
            """)
    List<EmployeeListDTO> findEmployeeListByEstablishmentIdAndActiveFilter(long establishmentId, EmployeeFilter filter,
            LocalDate date);

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

    @Query("""
                SELECT es.employee.id
                FROM EstablishmentStaff es
                WHERE es.id = :establishmentStaffId
            """)
    Long getEmployeeId(long establishmentStaffId);
}
