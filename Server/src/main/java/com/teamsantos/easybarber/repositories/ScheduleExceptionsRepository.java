package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;
import com.teamsantos.easybarber.entities.ScheduleException;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleExceptionsRepository
        extends JpaRepository<ScheduleException, Long>, JpaSpecificationExecutor<ScheduleException> {
    Optional<ScheduleException> findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndDateAfter(
            Long id, DAY_OF_WEEK day, String startHour, String endHour, Date date);
}
