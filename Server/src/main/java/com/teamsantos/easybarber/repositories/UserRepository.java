package com.teamsantos.easybarber.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.UserSignInDTO;
import com.teamsantos.easybarber.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
                SELECT EXISTS (
                SELECT 1
                FROM User u
                WHERE u.mobileInformation = :mobileInforamtion
                )
            """)
    boolean existsByMobileInformation(String mobileInforamtion);

    Optional<UserSignInDTO> findUserSignInByMobileInformation(String mobileInformation);

    Page<User> findByUserTypeId(long userTypeId, Pageable pageable);
}
