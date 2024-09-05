package com.teamsantos.easybarber.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
            SELECT new com.teamsantos.easybarber.DTO.UserSignInDTO(
                u.id,
                u.password,
                u.mobileInformation,
                e.id,
                u.userTypes
            )
            FROM User u
            LEFT JOIN Employee e ON e.user.id = u.id
            WHERE u.mobileInformation = :mobileInformation
            """)
    Optional<UserSignInDTO> findUserSignInByMobileInformation(String mobileInformation);

    @Query("SELECT u FROM User u JOIN u.userTypes ut WHERE ut.id = :userTypeId")
    Page<User> findByUserTypeId(@Param("userTypeId") long userTypeId, Pageable pageable);
}
