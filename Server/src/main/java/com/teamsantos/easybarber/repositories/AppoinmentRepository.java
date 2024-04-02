package com.teamsantos.easybarber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Appointment;

@Repository
public interface AppoinmentRepository extends JpaRepository<Appointment, Long> {
}
