package com.teamsantos.easybarber.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Establishment;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByUserId(Long userId);

    Optional<Employee> findByUserId(Long userId);

    @Query("SELECT sf.establishment FROM EstablishmentStaff sf WHERE sf.employee.id = :userId AND sf.approved = true AND sf.deleted = false")
    Page<Establishment> findOwnedEstablishmentsById(Long userId, Pageable pageable);
}
