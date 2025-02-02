package com.teamsantos.easybarber.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.DTO.schedule.ScheduleExceptionDTO;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.ScheduleException;

@Repository
public interface ScheduleExceptionsRepository
        extends JpaRepository<ScheduleException, Long>, JpaSpecificationExecutor<ScheduleException> {
    Optional<ScheduleException> findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndDateAfter(
            Long id, DAY_OF_WEEK day, LocalTime startHour, LocalTime endHour, LocalDate date);

    // <= not needed in the (s.startHour <= :time and s.endHour <= :endTime)) just
    // in there because its not wrong as well
    @Query("""
            SELECT EXISTS(
                SELECT 1
                from ScheduleException s
                where (s.employee.id = :employeeId or :employeeId is null)
                and s.active = true
                and (s.establishment.id = :establishmentId or :establishmentId is null)
                and (s.date = :date)
                and ((s.startHour <= :time and s.endHour > :time)
                    or (s.startHour < :endTime and s.endHour >= :endTime)
                    or (s.startHour <= :time and s.endHour <= :endTime))
            )
            """)
    boolean intercepts(Long employeeId, Long establishmentId, LocalDate date, LocalTime time, LocalTime endTime);

    // Long id, Long employeeId, Long establishmentId, LocalDate date,
    // LocalTime startHour, LocalTime endHour, Boolean active, DAY_OF_WEEK day
    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.schedule.ScheduleExceptionDTO(
                s.id,
                s.employee.id,
                s.establishment.id,
                s.date,
                s.startHour,
                s.endHour,
                s.active,
                s.day
            )
            FROM ScheduleException s
            WHERE
                (
                    (:#{#filter.employeeId} IS NULL OR
                    (
                        (s.employee.id = :#{#filter.employeeId} AND
                        (:#{#filter.establishmentId} = s.establishment.id OR s.establishment.id IS NULL))
                        OR
                        (s.employee.id IS NULL AND s.establishment.id = :#{#filter.establishmentId})
                    ))
                )
                AND (:#{#filter.employeeId} IS NOT NULL AND s.establishment.id = :#{#filter.establishmentId})
                AND (:#{#filter.from} IS NULL OR s.date >= :#{#filter.from})
                AND (:#{#filter.to} IS NULL OR s.date <= :#{#filter.to})
                AND s.active = true
            """)
    List<ScheduleExceptionDTO> findAllDTO(ScheduleFilter filter);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.schedule.ScheduleExceptionDTO(
                s.id,
                s.employee.id,
                s.establishment.id,
                s.date,
                s.startHour,
                s.endHour,
                s.active,
                s.day
            )
            FROM ScheduleException s
            WHERE
                (
                    (:employeeIds IS NULL OR
                    (
                        (s.employee.id IN :employeeIds AND
                        (:establishmentId = s.establishment.id OR s.establishment.id IS NULL))
                        OR
                        (s.employee.id IS NULL AND s.establishment.id = :establishmentId)
                    ))
                )
                AND (:employeeIds IS NOT NULL AND s.establishment.id = :establishmentId)
                AND (:from IS NULL OR s.date >= :from)
                AND (:to IS NULL OR s.date <= :to)
                AND s.active = true
            """)
    List<ScheduleExceptionDTO> findAllByEstablishmentIdEmployeeSetFromAndToDTO(Long establishmentId,
            Set<Long> employeeIds, LocalDate from, LocalDate to);

    @Query("""
            SELECT e.date
            FROM ScheduleException e
            WHERE date >= :from AND date <= :to
                AND (
                (:employeeId IS NULL OR e.employee.id = :employeeId)
                OR
                (:employeeId IS NULL AND ( :establishmentId IS NULL OR e.establishment.id = :establishmentId))
            )
            GROUP BY e.date ORDER BY e.date
            """)
    List<LocalDate> getExceptionsCalendar(LocalDate from, LocalDate to, Long employeeId, Long establishmentId);
}
