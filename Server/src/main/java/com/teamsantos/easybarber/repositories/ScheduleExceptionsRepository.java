package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.entities.ScheduleExceptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleExceptionsRepository extends JpaRepository<ScheduleExceptions, Long> {
}
