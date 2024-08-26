package com.teamsantos.easybarber.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Establishment;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByUserId(Long userId);

    @Query("SELECT sf.establishment FROM EstablishmentStaff sf WHERE sf.employee.id = :userId AND sf.approved = true AND sf.deleted = false")
    Page<Establishment> findOwnedEstablishmentsById(Long userId, Pageable pageable);

    void deleteByUserId(Long id);
}
