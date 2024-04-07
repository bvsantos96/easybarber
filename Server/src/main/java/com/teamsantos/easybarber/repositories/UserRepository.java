package com.teamsantos.easybarber.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMobileInformation(String mobileInformation);

    @Query("SELECT u.id FROM User u WHERE u.mobileInformation = :name ORDER BY u.id LIMIT 1")
    Optional<Long> getIdByMobileInformation(String name);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.id = :userId AND u.userTypeId = :userTypeId")
    boolean existsByIdAndUserTypeId(Long userId, Long userTypeId);

    Page<User> findByUserTypeId(Long userTypeId, Pageable pageable);

}
