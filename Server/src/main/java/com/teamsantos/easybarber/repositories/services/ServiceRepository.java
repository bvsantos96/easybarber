package com.teamsantos.easybarber.repositories.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.ServiceBaseDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.filters.ServiceFilter;
import com.teamsantos.easybarber.entities.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long>, CustomServiceRepository {

    @Query("""
            SELECT EXISTS (
                SELECT 1
                FROM Service s
                JOIN s.employee e
                JOIN e.user u
                WHERE s.id = :serviceId
                AND u.mobileInformation = :mobileInformation
                )
            """)
    boolean checkIfEmployeeIsServiceOwner(Long serviceId, String mobileInformation);

    @Query("""
            SELECT EXISTS (
                SELECT 1
                FROM Service s
                WHERE s.employee.id = :employeeId
                AND s.serviceType.id = :serviceTypeId
                AND lower(s.name) = lower(:name)
                AND s.description = :description
                )
            """)
    boolean existsByEmployeeIdServiceTypeIdNameAndDescription(long employeeId, long serviceTypeId, String name,
            String description);

    void deleteByEmployeeId(Long id);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.ServiceDTO(s)
            FROM Service s
            WHERE s.employee.id = :employeeId
            """)
    Page<ServiceDTO> findAllByEmployeeId(Pageable pageable);

    Page<ServiceBaseDTO> findAllBase(@Param("filter") ServiceFilter filter, Pageable pageable);
}
