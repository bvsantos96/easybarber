package com.teamsantos.easybarber.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMobileInformation(String mobileInformation);

    Optional<Long> getIdByMobileInformation(String name);

    Optional<Set<Establishment>> findOwnedEstablishmentsById(Long userId);
}
