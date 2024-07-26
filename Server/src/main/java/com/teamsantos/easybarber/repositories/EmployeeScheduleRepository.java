package com.teamsantos.easybarber.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.EmployeeSchedule;
import com.teamsantos.easybarber.entities.EmployeeSchedule.DAY_OF_WEEK;

@Repository
public interface EmployeeScheduleRepository
        extends JpaRepository<EmployeeSchedule, Long>, JpaSpecificationExecutor<EmployeeSchedule> {
    Optional<EmployeeSchedule> findByEmployeeIdAndDayAndStartHourLessThanEqualAndEndHourGreaterThanEqualAndActive(
            Long id,
            DAY_OF_WEEK day,
            String startHour, String endHour, boolean active);
}
