package com.teamsantos.easybarber.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.teamsantos.easybarber.entities.EstablishmentStaff;

@Repository
public interface EstablishmentStaffRepository extends JpaRepository<EstablishmentStaff, Long> {
    Optional<EstablishmentStaff> findByUserIdAndEstablishmentId(Long userId, Long establishmentId);

    @Query("SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM EstablishmentStaff es WHERE es.user.id = :userId AND es.establishment.id = :establishmentId AND es.approved = true AND es.deleted = false AND es.admin")
    boolean isUserAdminOfEstablishment(Long userId, Long establishmentId);
}
