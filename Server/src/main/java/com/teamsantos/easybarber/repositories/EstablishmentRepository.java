package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.DTO.BaseEstablishmentDTO;
import com.teamsantos.easybarber.entities.Establishment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {
    @NonNull
    Optional<Establishment> findById(@NonNull Long id);

    @Query("SELECT new com.teamsantos.easybarber.DTO.BaseEstablishmentDTO(e.id, e.name, e.description) FROM Establishment e")
    List<BaseEstablishmentDTO> findAllBase(Pageable pageable);

    @Query(value = "SELECT *, ST_Distance_Sphere(location, POINT(:longitude, :latitude)) AS distance " +
                   "FROM Establishment " +
                   "ORDER BY distance ASC " +
                   "LIMIT 10", nativeQuery = true)
    List<Establishment> findClosestEstablishments(double latitude, double longitude);

    @Query(value = "SELECT *, ST_Distance_Sphere(location, POINT(:longitude, :latitude)) AS distance " +
                   "FROM Establishment " +
                   "ORDER BY distance ASC " +
                   "LIMIT 1", nativeQuery = true)
    Establishment findClosestEstablishment(double latitude, double longitude);
}
