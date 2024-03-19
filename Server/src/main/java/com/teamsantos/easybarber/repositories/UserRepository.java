package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.entities.Establishment;
import com.teamsantos.easybarber.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMobileInformation(String mobileInformation);

    @Query("SELECT u.id FROM User u WHERE u.mobileInformation = :name ORDER BY u.id LIMIT 1")
    Optional<Long> getIdByMobileInformation(String name);
    @Query("SELECT e FROM Establishment e JOIN e.owners u WHERE u.id = :userId")
    Set<Establishment> findOwnedEstablishmentsById(Long userId);
}
