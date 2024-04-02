package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.entities.Employee;
import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByUser(User user);

    boolean existsByUserId(Long userId);

    Optional<Employee> findByUserId(Long userId);

    @Query("SELECT sf.establishment FROM EstablishmentStaff sf WHERE sf.employee.id = :userId AND sf.approved = true AND sf.deleted = false")
    Page<Establishment> findOwnedEstablishmentsById(Long userId, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.user.mobileInformation = :mobileInformation")
    Optional<Employee> findByMobileInformation(String mobileInformation);
}
