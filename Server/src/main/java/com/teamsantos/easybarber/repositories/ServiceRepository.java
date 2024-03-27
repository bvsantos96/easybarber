package com.teamsantos.easybarber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

}
