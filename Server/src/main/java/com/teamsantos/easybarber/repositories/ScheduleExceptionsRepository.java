package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.ScheduleExceptions;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleExceptionsRepository extends JpaRepository<ScheduleExceptions, Long> {
    Optional<ScheduleExceptions> findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqual(Long id, DAY_OF_WEEK day, String startHour, String endHour);
}
