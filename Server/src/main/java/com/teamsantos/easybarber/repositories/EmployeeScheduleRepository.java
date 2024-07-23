package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.DTO.filters.ScheduleFilter;
import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeScheduleRepository extends JpaRepository<EmployeeSchedule, Long> {
    Optional<EmployeeSchedule> findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndActive(Long id,
            DAY_OF_WEEK day,
            String startHour, String endHour, boolean active);

    @Query("""
            SELECT s
            FROM EmployeeSchedule s
            WHERE s.employee.id = :#{#filter.employeeId}
            AND s.establishment.id = :#{#filter.establishmentId}
            AND s.day IN :#{#filter.dayOfWeek}
            AND s.startHour >= :#{#filter.startHour}
            AND s.endHour <= :#{#filter.endHour}
            AND s.active = :#{#filter.active}
            """)
    Page<EmployeeSchedule> findAllByFilter(ScheduleFilter filter, Pageable pageable);
}
