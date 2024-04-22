package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppoinmentRepository extends JpaRepository<Appointment, Long> {
}
