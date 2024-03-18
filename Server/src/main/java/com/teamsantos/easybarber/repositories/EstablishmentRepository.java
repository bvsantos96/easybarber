package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.entities.Establishment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {
	Optional<Establishment> findById(Long id);
	Optional<Establishment> findByName(String name);
	@Query("SELECT new com.teamsantos.easybarber.DTO.BaseEstablishmentDTO(e.id, e.name, e.description) FROM Establishment e")
	List<BaseEstablishmentDTO> findAllBase(Pageable pageable);
}
