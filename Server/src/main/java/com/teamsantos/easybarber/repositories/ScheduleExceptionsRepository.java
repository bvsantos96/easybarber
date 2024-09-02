package com.teamsantos.easybarber.repositories;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
