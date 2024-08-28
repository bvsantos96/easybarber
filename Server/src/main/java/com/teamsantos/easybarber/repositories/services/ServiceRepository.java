package com.teamsantos.easybarber.repositories.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.ServiceBaseDTO;
import com.teamsantos.easybarber.DTO.ServiceDTO;
import com.teamsantos.easybarber.DTO.ServiceWithEmployeeDTO;
import com.teamsantos.easybarber.DTO.filters.ServiceFilter;
import com.teamsantos.easybarber.entities.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long>, ServiceRepositoryCustom {

    @Query("""
            SELECT EXISTS (
                SELECT 1
                FROM Service s
                JOIN s.employee e
                JOIN e.user u
                WHERE s.id = :serviceId
                AND u.mobileInformation = :mobileInformation
                LIMIT 1)
            """)
    boolean checkIfEmployeeIsServiceOwner(Long serviceId, String mobileInformation);

    @Query("""
            SELECT EXISTS (
                SELECT 1
                Service s
                WHERE s.employee.id = :employeeId
                AND s.serviceType.id = :serviceTypeId
                AND lower(s.name) = lowers(:name)
                AND s.description = :description
                LIMIT 1)
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

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.ServiceBaseDTO(s.id, s.name, s.description, s.duration, s.serviceType.id)
            FROM Service s
            WHERE (:filter.employeeId IS NULL OR s.employee.id = :filter.employeeId)
            AND (:filter.serviceTypeId IS NULL OR s.serviceType.id = :filter.serviceTypeId)
            AND (:filter.name IS NULL OR lower(s.name) LIKE :filter.name)
            AND (:filter.duration IS NULL OR s.duration = :filter.duration)
            """)
    Page<ServiceBaseDTO> findAllBase(@Param("filter") ServiceFilter filter, Pageable pageable);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.ServiceBaseDTO(s.id, s.name, s.description, s.duration, s.serviceType.id, i.data)
            FROM Service s
            JOIN (SELECT i.id, i.data, i.entity.id as serviceId
                FROM ServiceImage i
                WHERE i.isMain = true LIMIT 1) i
            WHERE (:filter.employeeId IS NULL OR s.employee.id = :filter.employeeId)
            AND (:filter.serviceTypeId IS NULL OR s.serviceType.id = :filter.serviceTypeId)
            AND (:filter.name IS NULL OR lower(s.name) LIKE :filter.name)
            """)
    Page<ServiceBaseDTO> findAllBaseWImage(ServiceFilter filter, Pageable pageable);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.ServiceWithEmployeeDTO(s.id, s.name, s.description, s.duration, s.serviceType.id, i.data, s.employee.id, s.employee.user.name, ei.data)
            FROM Service s
            JOIN (SELECT i.id, i.data, i.entity.id as serviceId
                FROM ServiceImage i
                WHERE i.isMain = true LIMIT 1) i
            JOIN (SELECT i.id, i.data, i.entity.id as serviceId
                FROM EmployeeImage i
                WHERE i.isMain = true LIMIT 1) i
            WHERE (:filter.employeeId IS NULL OR s.employee.id = :filter.employeeId)
            AND (:filter.serviceTypeId IS NULL OR s.serviceType.id = :filter.serviceTypeId)
            AND (:filter.name IS NULL OR lower(s.name) LIKE :filter.name)
            """)
    Page<ServiceWithEmployeeDTO> findAllWImagesAndEmployeeWImages(ServiceFilter filter, Pageable pageable);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.ServiceWithEmployeeDTO(s.id, s.name, s.description, s.duration, s.serviceType.id, i.data, s.employee.id, s.employee.user.name)
            FROM Service s
            JOIN (SELECT i.id, i.data, i.entity.id as serviceId
                FROM ServiceImage i
                WHERE i.isMain = true LIMIT 1) i
            WHERE (:filter.employeeId IS NULL OR s.employee.id = :filter.employeeId)
            AND (:filter.serviceTypeId IS NULL OR s.serviceType.id = :filter.serviceTypeId)
            AND (:filter.name IS NULL OR lower(s.name) LIKE :filter.name)
            """)
    Page<ServiceWithEmployeeDTO> findAllBaseWImageAndEmployee(ServiceFilter filter, Pageable pageable);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.ServiceWithEmployeeDTO(s.id, s.name, s.description, s.duration, s.serviceType.id, i.data, s.employee.id, s.employee.user.name)
            FROM Service s
            JOIN (SELECT i.id, i.data, i.entity.id as serviceId
                FROM EmployeeImage i
                WHERE i.isMain = true LIMIT 1) i
            WHERE (:filter.employeeId IS NULL OR s.employee.id = :filter.employeeId)
            AND (:filter.serviceTypeId IS NULL OR s.serviceType.id = :filter.serviceTypeId)
            AND (:filter.name IS NULL OR lower(s.name) LIKE :filter.name)
            """)
    Page<ServiceWithEmployeeDTO> findAllBaseAndEmployeeWImages(ServiceFilter filter, Pageable pageable);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.ServiceWithEmployeeDTO(s.id, s.name, s.description, s.duration, s.serviceType.id, i.data, s.employee.id, s.employee.user.name, ei.data)
            FROM Service s
            WHERE (:filter.employeeId IS NULL OR s.employee.id = :filter.employeeId)
            AND (:filter.serviceTypeId IS NULL OR s.serviceType.id = :filter.serviceTypeId)
            AND (:filter.name IS NULL OR lower(s.name) LIKE :filter.name)
            """)
    Page<ServiceWithEmployeeDTO> findAllBaseAndEmployee(ServiceFilter filter, Pageable pageable);
}
