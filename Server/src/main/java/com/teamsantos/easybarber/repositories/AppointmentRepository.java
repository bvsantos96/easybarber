package com.teamsantos.easybarber.repositories;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Appointment;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    @Query("""
                    select count(s) > 0
                    from Appointment s
                    where (s.employee.id = :employeeId or :employeeId is null)
                    and (s.date = :date)
                    and ((s.startHour <= :time and s.endHour > :time)
                        or (s.startHour < :endTime and s.endHour >= :endTime)
                        or (s.startHour <= :time and s.endHour <= :endTime))
            """)
    boolean intercepts(Long employeeId, LocalDate date, LocalTime time, LocalTime endTime);
}
