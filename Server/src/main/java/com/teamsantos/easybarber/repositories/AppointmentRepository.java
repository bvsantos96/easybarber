package com.teamsantos.easybarber.repositories;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.AppointmentListDTO;
import com.teamsantos.easybarber.DTO.filters.AppointmentFilter;
import com.teamsantos.easybarber.entities.Appointment;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    @Query("""
            SELECT EXISTS(
                SELECT 1
                from Appointment s
                where (s.employee.id = :employeeId or :employeeId is null)
                and s.confirmed = true
                and (s.active = true)
                and (s.date = :date)
                and ((:time is null or s.time >= :time) and (:endTime is null or s.time <= :endTime))
                )
            """)
    boolean intercepts(long employeeId, LocalDate date, LocalTime time, LocalTime endTime);

    @Query("""
            SELECT EXISTS(
                SELECT 1
                from Appointment s
                where s.employee.id = :employeeId
                and s.id = :appointmentId
                )
            """)
    boolean existsByIdAndEmployeeId(long appointmentId, long employeeId);

    @Query("""
            SELECT EXISTS(
                SELECT 1
                from Appointment s
                where s.user.id = :userId
                and s.id = :appointmentId
                )
            """)
    boolean existsByIdAndUserId(long appointmentId, long userId);

    @Query("""
                SELECT new com.teamsantos.easybarber.DTO.AppointmentListDTO(
                    s.id,
                    s.service.service.name,
                    s.employee.user.name,
                    s.establishment.name,
                    s.establishment.location,
                    COALESCE(ei.data, esi.data, s.service.service.serviceType.imageURL),
                    s.date,
                    s.time,
                    s.confirmed
                )
                FROM Appointment s
                LEFT JOIN EmployeeImage ei on s.employee.id = ei.entity.id and ei.isMain = true
                LEFT JOIN EstablishmentImage esi on s.establishment.id = esi.entity.id and esi.isMain = true
                WHERE (:#{#filter.employeeId} is null or s.employee.id = :#{#filter.employeeId})
                and (:#{#filter.establishmentId} is null or s.establishment.id = :#{#filter.establishmentId})
                and (:#{#filter.clientId} is null or s.user.id = :#{#filter.clientId})
                and (:#{#filter.serviceId} is null or s.service.id = :#{#filter.serviceId})
                and (:#{#filter.date} is null or s.date = :#{#filter.date})
                and (:#{#filter.time} is null or s.time >= :#{#filter.time})
                and (:#{#filter.endTime} is null or s.time <= :#{#filter.endTime})
                and (:#{#filter.future} is null or (s.date > CURRENT_DATE or (s.date = current_date and s.time >= current_time)))
                and (:#{#filter.activeOnly} is null or s.active = :#{#filter.activeOnly})
            """)
    Page<AppointmentListDTO> findAllToUser(AppointmentFilter filter, Pageable pageable);

    @Query("""
                SELECT new com.teamsantos.easybarber.DTO.AppointmentListDTO(
                    s.id,
                    s.service.service.name,
                    COALESCE(s.nonRegisteredUser, s.user.name),
                    s.establishment.name,
                    s.establishment.location,
                    COALESCE(esi.data, s.service.service.serviceType.imageURL),
                    s.date,
                    s.time,
                    s.confirmed
                )
                FROM Appointment s
                LEFT JOIN EstablishmentImage esi on s.establishment.id = esi.entity.id and esi.isMain = true
                WHERE (:#{#filter.employeeId} is null or s.employee.id = :#{#filter.employeeId})
                and (:#{#filter.establishmentId} is null or s.establishment.id = :#{#filter.establishmentId})
                and (:#{#filter.clientId} is null or s.user.id = :#{#filter.clientId})
                and (:#{#filter.serviceId} is null or s.service.id = :#{#filter.serviceId})
                and (:#{#filter.date} is null or s.date = :#{#filter.date})
                and (:#{#filter.time} is null or s.time >= :#{#filter.time})
                and (:#{#filter.endTime} is null or s.time <= :#{#filter.endTime})
                and (:#{#filter.future} is null or (s.date > CURRENT_DATE or (s.date = current_date and s.time >= current_time)))
                and (:#{#filter.activeOnly} is null or s.active = :#{#filter.activeOnly})
            """)
    Page<AppointmentListDTO> findAllToEmployee(AppointmentFilter filter, Pageable pageable);

}
