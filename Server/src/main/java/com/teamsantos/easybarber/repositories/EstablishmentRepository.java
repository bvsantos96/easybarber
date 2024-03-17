package com.teamsantos.easybarber.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.entities.Establishment;

@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {
	Optional<Establishment> findByID(Long id);
	Optional<Establishment> findByName(String name);
	Optional<BaseEstablishmentDTO> findByIDNoOwner(Long id);
	List<BaseEstablishmentDTO> findAllNoOwner();
}
