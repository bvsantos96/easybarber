package com.teamsantos.easybarber.repositories;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

@Repository
public interface EmployeeScheduleRepository
        extends JpaRepository<EmployeeSchedule, Long>, JpaSpecificationExecutor<EmployeeSchedule> {
    @Override
    @Cacheable(value = "employeeSchedules", key = "#id")
    Optional<EmployeeSchedule> findById(Long id);

    @Cacheable(value = "employeeSchedules", key = "#id + '-' + #day + '-' + #startHour + '-' + #endHour + '-' + #active")
    Optional<EmployeeSchedule> findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndActive(
            Long id, DAY_OF_WEEK day, String startHour, String endHour, boolean active);

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
