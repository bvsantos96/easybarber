package com.teamsantos.easybarber.repositories.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.filters.ServiceFilter;
import com.teamsantos.easybarber.DTO.filters.ServiceWithEmployeeFilter;
import com.teamsantos.easybarber.DTO.service.ServiceBaseDTO;
import com.teamsantos.easybarber.DTO.service.ServiceDTO;
import com.teamsantos.easybarber.DTO.service.ServiceWithImagesDTO;
import com.teamsantos.easybarber.entities.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

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
                WHERE s.id = :serviceId
                AND s.employee.id = :employeeId
                )
            """)
    boolean checkIfEmployeeIsServiceOwner(long serviceId, long employeeId);

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
            SELECT new com.teamsantos.easybarber.DTO.service.ServiceDTO(s)
            FROM Service s
            WHERE s.employee.id = :employeeId
            """)
    Page<ServiceDTO> findAllByEmployeeId(Pageable pageable);

    @Query("""
                SELECT new com.teamsantos.easybarber.DTO.service.ServiceBaseDTO(s.id, s.name, s.description, s.duration, si.data,s.serviceType.id, s.serviceType.name,s.serviceType.description, s.serviceType.imageURL)
                FROM Service s
                LEFT JOIN ServiceImage si ON :#{#filter.includeServiceImage} = true AND si.isMain = true and si.entity.id = s.id
                WHERE (:#{#filter.employeeId} is null or s.employee.id = :#{#filter.employeeId})
                AND (:#{#filter.serviceTypeId} is null or s.serviceType.id = :#{#filter.serviceTypeId})
                AND (:#{#filter.name} is null or lower(s.name) like lower(concat('%', :#{#filter.name}, '%')))
                AND (:#{#filter.description} is null or lower(s.description) like lower(concat('%', :#{#filter.description}, '%')))
            """)
    Page<ServiceBaseDTO> findAllBase(@Param("filter") ServiceFilter filter, Pageable pageable);

    @Query("""
                SELECT new com.teamsantos.easybarber.DTO.service.ServiceWithImagesDTO(s.id, s.name, s.description, s.duration, si.data,s.serviceType.id, s.serviceType.name,s.serviceType.description, s.serviceType.imageURL,
                    s.employee.id, s.employee.user.name, ei.data)
                FROM Service s
                LEFT JOIN ServiceImage si ON :#{#filter.includeServiceImage} = true AND si.isMain = true and si.entity.id = s.id
                LEFT JOIN EmployeeImage ei ON :#{#filter.includeEmployeeImage} = true AND ei.isMain = true and ei.entity.id = s.employee.id
                WHERE (:#{#filter.employeeId} is null or s.employee.id = :#{#filter.employeeId})
                AND (:#{#filter.serviceTypeId} is null or s.serviceType.id = :#{#filter.serviceTypeId})
                AND (:#{#filter.name} is null or lower(s.name) like lower(concat('%', :#{#filter.name}, '%')))
                AND (:#{#filter.description} is null or lower(s.description) like lower(concat('%', :#{#filter.description}, '%')))
            """)
    Page<ServiceWithImagesDTO> findAllWEmployee(@Param("filter") ServiceWithEmployeeFilter filter, Pageable pageable);

    @Query("""
            SELECT s.duration
            FROM Service s
            WHERE s.id = :serviceId
            """)
    Integer getDuration(Long serviceId);
}
