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
    // @Query("""
    // SELECT new com.teamsantos.easybarber.entities.AuthUser(
    // u.id,
    // u.mobileInformation,
    // u.password,
    // CASE WHEN e.id IS NOT NULL THEN true ELSE false END
    // )
    // FROM User u
    // WHERE u.mobileInformation = :mobileInformation""")
    // Optional<AuthUser> findByMobileInformationAuth(@Param("mobileInformation")
    // String mobileInformation);

    @Query("""
                SELECT EXISTS (
                SELECT 1
                FROM User u
                WHERE u.mobileInformation = :mobileInforamtion
                LIMIT 1)
            """)
    boolean existsByMobileInformation(String mobileInforamtion);

    @Query("""
                SELECT EXISTS (
                    SELECT 1
                    FROM User u
                    WHERE
                        u.id = :userId
                    AND u.userTypeId = :userTypeId
                    LIMIT 1
                )
            """)
    boolean existsByIdAndUserTypeId(Long userId, Long userTypeId);

    Optional<UserSignInDTO> findUserSignInByMobileInformation(String mobileInformation);

    Page<User> findByUserTypeId(long userTypeId, Pageable pageable);
}
