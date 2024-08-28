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
            SELECT EXISTS(
                SELECT 1
                from Appointment s
                where (s.employee.id = :employeeId or :employeeId is null)
                and s.confirmed = true
                and (s.active = true)
                and (s.date = :date)
                and ((:time is null or s.time >= :time) and (:endTime is null or s.time <= :endTime))
                LIMIT 1)
            """)
    boolean intercepts(long employeeId, LocalDate date, LocalTime time, LocalTime endTime);

    @Query("""
            SELECT EXISTS(
                SELECT 1
                from Appointment s
                where s.employee.id = :employeeId
                and s.id != :appointmentId
                LIMIT 1)
            """)
    boolean existsByIdAndEmployeeId(long appointmentId, long employeeId);

    @Query("""
            SELECT EXISTS(
                SELECT 1
                from Appointment s
                where s.user.id = :userId
                and s.id != :appointmentId
                LIMIT 1)
            """)
    boolean existsByIdAndUserId(long appointmentId, long userId);
}
