package com.teamsantos.easybarber.repositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "employeeSchedules", key = "#id")
    Optional<EmployeeSchedule> findById(Long id);

    Optional<EmployeeSchedule> findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndActive(
            Long id, DAY_OF_WEEK day, String startHour, String endHour, boolean active);

    Optional<List<EmployeeSchedule>> findByEmployeeIdAndDayInAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndActive(
            Long employeeId, Set<DAY_OF_WEEK> days, String startHour, String endHour, boolean active);

    @Query("SELECT COUNT(es) > 0 FROM EmployeeSchedule es " +
            "WHERE es.employee.id = :id " +
            "AND es.day IN :days " +
            "AND es.startHour <= :endHour " +
            "AND es.endHour >= :startHour " +
            "AND es.active = :active")
    boolean hasOverlappingSchedule(
            @Param("id") Long id,
            @Param("days") Set<DAY_OF_WEEK> days,
            @Param("startHour") String startHour,
            @Param("endHour") String endHour,
            @Param("active") boolean active);

    @Cacheable("employeeSchedules")
    @Query("""
                SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
                FROM EmployeeSchedule s
                JOIN s.employee e
                WHERE s.id = :scheduleId
                AND e.user.mobileInformation = :mobileInformation
            """)
    boolean checkIfEmployeeIsScheduleOwner(Long scheduleId, String mobileInformation);

    @Cacheable("employeeSchedulesEstablishmentOwner")
    @Query("""
                SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
                FROM EmployeeSchedule s
                JOIN s.establishment e
                JOIN e.staff staff
                JOIN staff.employee emp
                WHERE s.id = :scheduleId
                AND emp.user.mobileInformation = :mobileInformation
                AND staff.admin = true
            """)
    boolean checkIfEmployeeIsEstablishmentOwner(Long scheduleId, String mobileInformation);
}
