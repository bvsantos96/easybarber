package com.teamsantos.easybarber.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.teamsantos.easybarber.entities.EstablishmentStaff;

@Repository
public interface EstablishmentStaffRepository extends JpaRepository<EstablishmentStaff, Long> {
	Optional<EstablishmentStaff> findByUserIdAndEstablishmentId(Long userId, Long establishmentId);
	@Query("SELECT CASE WHEN COUNT(sf) > 0 THEN true ELSE false END FROM EstablishmentStaff sf WHERE sf.user.id = :userId AND sf.establishment.id = :establishmentId AND sf.approved = true AND sf.deleted = false AND sf.admin")
	boolean isUserAdminOfEstablishment(Long userId, Long establishmentId);
}
