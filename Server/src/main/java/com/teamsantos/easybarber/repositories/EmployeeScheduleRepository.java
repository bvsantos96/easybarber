package com.teamsantos.easybarber.repositories;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.DTO.schedule.EmployeeScheduleDTO;
import com.teamsantos.easybarber.DTO.schedule.MinutesInDay;
import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

@Repository
public interface EmployeeScheduleRepository
        extends JpaRepository<EmployeeSchedule, Long>, JpaSpecificationExecutor<EmployeeSchedule> {

    @Override
    @NonNull
    Optional<EmployeeSchedule> findById(@NonNull Long id);

    Optional<EmployeeSchedule> findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndActive(
            Long id, DAY_OF_WEEK day, LocalTime startHour, LocalTime endHour, boolean active);

    Optional<List<EmployeeSchedule>> findByEmployeeIdAndDayInAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndActive(
            Long employeeId, Set<DAY_OF_WEEK> days, LocalTime startHour, LocalTime endHour, boolean active);

    @Query("""
            SELECT EXISTS(
                SELECT 1
                FROM EmployeeSchedule es
                WHERE es.employee.id = :id
                AND es.day IN :days
                AND es.active = :active
                AND (es.startHour <= :endHour AND es.endHour >= :startHour)
            )
            """)
    boolean hasOverlappingSchedule(
            @Param("id") Long id,
            @Param("days") Set<DAY_OF_WEEK> days,
            @Param("startHour") LocalTime startHour,
            @Param("endHour") LocalTime endHour,
            @Param("active") boolean active);

    @Query("""
            SELECT EXISTS(
            SELECT 1
                    FROM EmployeeSchedule s
                    JOIN s.employee e
                    WHERE s.id = :scheduleId
                    AND e.user.mobileInformation = :mobileInformation
            )
                """)
    boolean checkIfEmployeeIsScheduleOwner(Long scheduleId, String mobileInformation);

    @Query("""
            SELECT EXISTS(
                SELECT 1
                FROM EmployeeSchedule s
                JOIN s.establishment e
                JOIN e.staff staff
                JOIN staff.employee emp
                WHERE s.id = :scheduleId
                AND emp.user.mobileInformation = :mobileInformation
                AND staff.admin = true
                )
            """)
    boolean checkIfEmployeeIsEstablishmentOwner(Long scheduleId, String mobileInformation);

    @Query("""
            SELECT EXISTS(
                SELECT 1
                FROM EmployeeSchedule s
                WHERE s.employee.id = :employeeId
                AND s.active = true
                AND s.establishment.id = :establishmentId
                AND s.day = :dayOfWeek
                AND s.startHour <= :time
                AND s.endHour >= :endTime
                )
            """)
    boolean existsByEmployeeIdAndEstablishmentIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
            Long employeeId, Long establishmentId, DAY_OF_WEEK dayOfWeek, LocalTime time, LocalTime endTime);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.schedule.EmployeeScheduleDTO(
                s.id,
                s.employee.id,
                s.establishment.id,
                s.day,
                s.startHour,
                s.endHour
            )
            FROM EmployeeSchedule s
            WHERE (:#{#filter.employeeId} IS NULL OR s.employee.id = :#{#filter.employeeId})
                AND (:#{#filter.establishmentId} IS NULL OR s.establishment.id = :#{#filter.establishmentId})
                AND (:#{#filter.dayOfWeek} IS NULL OR s.day IN :#{#filter.dayOfWeek})
                AND (s.active = :#{#filter.active} OR (s.active = true AND :#{#filter.active} IS NULL))
            ORDER BY s.startHour ASC
             """)
    List<EmployeeScheduleDTO> findAllDTO(ScheduleFilter filter);

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.schedule.MinutesInDay(
                s.day as dayOfWeek,
                SUM(TIMESTAMPDIFF(MINUTE, s.startHour, s.endHour)) as minutesInDay
            )
            FROM EmployeeSchedule s
            WHERE (:employeeId IS NULL OR s.employee.id = :employeeId)
              AND (:establishmentId IS NULL OR s.establishment.id = :establishmentId)
            GROUP BY s.day
            """)
    List<MinutesInDay> getDaysWithNoSchedule(Long employeeId, Long establishmentId);

    @Query("""
            SELECT EXISTS(
                SELECT 1
                FROM EmployeeSchedule s
                WHERE
                s.establishment.id = :establishmentId
                AND (:employeeId IS NULL OR s.employee.id = :employeeId)
                AND s.active = true
            )
            """)
    Boolean establishmentHasValidSchedule(long establishmentId, Long employeeId);
}
