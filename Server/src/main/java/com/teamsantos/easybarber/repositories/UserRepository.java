package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMobileInformation(String mobileInformation);

    Optional<Long> getIdByMobileInformation(String name);

    Optional<Set<Establishment>> findOwnedEstablishmentsById(Long userId);
}
