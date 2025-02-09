package com.teamsantos.easybarber.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.establishment.EstablishmentBaseDTO;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Establishment;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUserId(long userId);

    @Query("SELECT sf.establishment FROM EstablishmentStaff sf WHERE sf.employee.id = :userId AND sf.approved = true AND sf.deleted = false")
    Page<Establishment> findOwnedEstablishmentsById(long userId, Pageable pageable);

    void deleteByUserId(long id);

    @Modifying
    @Query("UPDATE Employee e SET e.enabled = false WHERE e.id = :id")
    void markAsDeleted(long id);

    @Query("""
                SELECT e.user.id FROM Employee e WHERE e.id = :employeeId
            """)
    Long findUserIdById(long employeeId);

    @Query("""
            SELECT EXISTS (
                SELECT 1 FROM Employee e WHERE e.user.id = :userId
            )
            """)
    boolean existsByUserId(long userId);

    @Query("""
                SELECT new com.teamsantos.easybarber.DTO.establishment.EstablishmentBaseDTO(es.establishment.id, es.establishment.name, esi.data)
                FROM EstablishmentStaff es
                JOIN EstablishmentImage esi ON esi.entity.id = es.establishment.id AND esi.isMain = true
                WHERE es.employee.id = :employeeId AND es.approved = true AND es.deleted = false
            """)
    List<EstablishmentBaseDTO> getEstablishments(long employeeId);
}
