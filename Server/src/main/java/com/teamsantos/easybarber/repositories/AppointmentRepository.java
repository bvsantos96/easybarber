package com.teamsantos.easybarber.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.appointment.AppointmentListDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentReminderDTO;
import com.teamsantos.easybarber.DTO.appointment.AppointmentsHashDTO;
import com.teamsantos.easybarber.DTO.filters.AppointmentFilter;
import com.teamsantos.easybarber.DTO.schedule.ScheduleExceptionDTO;
import com.teamsantos.easybarber.entities.Appointment;
import com.teamsantos.easybarber.entities.ScheduleException;

import jakarta.persistence.Tuple;

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
                SELECT new com.teamsantos.easybarber.DTO.appointment.AppointmentListDTO(
                    s.id,
                    s.service.service.name,
                    s.employee.id,
                    u.name,
                    s.establishment.id,
                    s.establishment.name,
                    s.establishment.location,
                    s.establishment.address,
                    COALESCE(ei.data, esi.data, s.service.service.serviceType.imageURL),
                    s.date,
                    s.time,
                    s.confirmed,
                    s.active,
                    s.feedback
                )
                FROM Appointment s
                LEFT JOIN User u on s.user.id = s.employee.user.id
                LEFT JOIN EmployeeImage ei on s.employee.id = ei.entity.id and ei.isMain = true
                LEFT JOIN EstablishmentImage esi on s.establishment.id = esi.entity.id and esi.isMain = true
                WHERE (:#{#filter.employeeId} is null or s.employee.id = :#{#filter.employeeId})
                and (:#{#filter.establishmentId} is null or s.establishment.id = :#{#filter.establishmentId})
                and (:#{#filter.clientId} is null or s.user.id = :#{#filter.clientId})
                and (:#{#filter.serviceId} is null or s.service.id = :#{#filter.serviceId})
                and (:#{#filter.date} is null or s.date = :#{#filter.date})
                and (:#{#filter.time} is null or s.time >= :#{#filter.time})
                and (:#{#filter.endTime} is null or s.time <= :#{#filter.endTime})
                and (:#{#filter.future} is null OR (
                        (:#{#filter.future} = true AND (s.date > CURRENT_DATE or (s.date = current_date and s.time >= current_time)))
                    OR
                        (:#{#filter.future} = false AND ((s.date < CURRENT_DATE or (s.date = current_date and s.time < current_time)) OR (s.active = false)))
                    )
                )
                and (:#{#filter.activeOnly} is null or s.active = :#{#filter.activeOnly})
                ORDER BY s.date ASC, s.time ASC
            """)
    Page<AppointmentListDTO> findAllToUser(AppointmentFilter filter, Pageable pageable);

    @Query("""
                SELECT new com.teamsantos.easybarber.DTO.appointment.AppointmentListDTO(
                    s.id,
                    s.service.service.name,
                    COALESCE(s.user.id),
                    COALESCE(s.nonRegisteredUser, s.user.name),
                    s.establishment.id,
                    s.establishment.name,
                    s.establishment.location,
                    s.establishment.address,
                    COALESCE(esi.data, s.service.service.serviceType.imageURL),
                    s.date,
                    s.time,
                    s.confirmed,
                    s.active,
                    s.feedback
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
                and (:#{#filter.future} is null OR (
                        (:#{#filter.future} = true AND (s.date > CURRENT_DATE or (s.date = current_date and s.time >= current_time)))
                    OR
                        (:#{#filter.future} = false AND ((s.date < CURRENT_DATE or (s.date = current_date and s.time < current_time)) OR (s.active = false)))
                    )
                )
                and (:#{#filter.activeOnly} is null or s.active = :#{#filter.activeOnly})
                ORDER BY s.date ASC, s.time ASC
            """)
    Page<AppointmentListDTO> findAllToEmployee(AppointmentFilter filter, Pageable pageable);

    @Query("""
                    SELECT new com.teamsantos.easybarber.DTO.appointment.AppointmentReminderDTO(
                        s.id,
                        s.user.name,
                        s.user.mobileInformation,
                        s.date,
                        s.time,
                        s.establishment.name,
                        s.employee.user.name
                    )
                    FROM Appointment s
                    WHERE s.reminded = false
                    AND s.date = :date
                    AND s.active = true
            """)
    List<AppointmentReminderDTO> findNextDayAppointmentsNotReminded(@Param("date") LocalDate date);

    @Query("""
            SELECT new com.teamsantos.easybarber.entities.ScheduleException(
                s.employee.id,
                s.establishment.id,
                s.date,
                s.time,
                s.service.service.duration
            )
            FROM Appointment s
            WHERE s.date = :from
                AND (:employeeId = null OR s.employee.id = :employeeId)
                AND s.establishment.id = :establishmentId
                AND s.active = true
            """)
    List<ScheduleException> findAppointmentsByDateEmployeeEstablishment(LocalDate from, Long employeeId,
            long establishmentId);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.schedule.ScheduleExceptionDTO(
                s.id,
                s.employee.id,
                s.establishment.id,
                s.date,
                s.time,
                s.service.service.duration
            )
            FROM Appointment s
            WHERE s.date = :from
                AND (:employeeId = null OR s.employee.id = :employeeId)
                AND s.establishment.id = :establishmentId
                AND s.active = true
            """)
    List<ScheduleExceptionDTO> findAppointmentsByDateEmployeeEstablishmentDTO(LocalDate from,
            Long employeeId, Long establishmentId);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.schedule.ScheduleExceptionDTO(
                s.id,
                s.employee.id,
                s.establishment.id,
                s.date,
                s.time,
                s.service.service.duration
            )
            FROM Appointment s
            WHERE s.date = :from
                AND (:employeeIds IS NULL OR s.employee.id IN :employeeIds)
                AND s.establishment.id = :establishmentId
                AND s.active = true
            """)
    List<ScheduleExceptionDTO> findAppointmentsByDateEmployeesEstablishmentDTO(
            @Param("from") LocalDate from,
            @Param("employeeIds") Set<Long> employeeIds,
            @Param("establishmentId") Long establishmentId);

    @Query(value = """
                SELECT
                    SUM(CASE WHEN (a.active = TRUE AND a.date > CURRENT_DATE OR (a.date = CURRENT_DATE AND a.time >= CURRENT_TIME)) THEN 1 ELSE 0 END) AS future,
                    SUM(CASE WHEN (a.active = FALSE OR a.date < CURRENT_DATE OR (a.date = CURRENT_DATE AND a.time < CURRENT_TIME)) THEN 1 ELSE 0 END) AS past
                FROM appointment a
                WHERE (:userView = true AND :userId = a.user_id OR :userView = false AND :userId = a.employee_id)
                    AND a.confirmed = true
            """, nativeQuery = true)
    Optional<Tuple> countAppointments(long userId, boolean userView);

    @Query("""
                SELECT a
                FROM Appointment a
                WHERE a.user.id = :userId
                AND a.feedbackAsked IS NULL OR a.feedbackAsked = false
                AND a.active = true
                AND a.confirmed = true
                AND a.date < CURRENT_DATE
                ORDER BY a.date DESC, a.time DESC
            """)
    List<Appointment> findTopByUserIdAndFeedbackAskedFalseOrderByDateDescTimeDesc(@Param("userId") long userId,
            Pageable pageable);

    @Query("""
                SELECT new com.teamsantos.easybarber.DTO.appointment.AppointmentsHashDTO(
                    a.id,
                    CASE
                        WHEN (a.active = TRUE AND (a.date > CURRENT_DATE OR (a.date = CURRENT_DATE AND a.time >= CURRENT_TIME)))
                        THEN true
                        ELSE false
                    END
                )
                FROM Appointment a
                WHERE
                    ((:userView = true AND :userId = a.user.id) OR (:userView = false AND :userId = a.employee.id))
                    AND a.confirmed = true
            """)
    List<AppointmentsHashDTO> findAllAppointmentsHash(long userId, boolean userView);
}
