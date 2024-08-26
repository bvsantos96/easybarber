package com.teamsantos.easybarber.repositories;

import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

@Repository
public interface EmployeeScheduleRepository
        extends JpaRepository<EmployeeSchedule, Long>, JpaSpecificationExecutor<EmployeeSchedule> {
    @Override
    Optional<EmployeeSchedule> findById(Long id);

    Optional<EmployeeSchedule> findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndActive(
            Long id, DAY_OF_WEEK day, LocalTime startHour, LocalTime endHour, boolean active);

    @Query("""
            SELECT EXISTS(
                SELECT 1
                FROM EmployeeSchedule es
                WHERE es.employee.id = :id
                AND es.day = :day
                AND es.startHour <= :time
                AND es.endHour >= :endTime
                AND es.active = true
            LIMIT 1)
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
            LIMIT 1)
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
                LIMIT 1)
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
                LIMIT 1)
            """)
    boolean existsByEmployeeIdAndEstablishmentIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqual(
            Long employeeId, Long establishmentId, DAY_OF_WEEK dayOfWeek, LocalTime time, LocalTime endTime);
}
