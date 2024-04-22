package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.entities.AppointmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, Long> {
}
