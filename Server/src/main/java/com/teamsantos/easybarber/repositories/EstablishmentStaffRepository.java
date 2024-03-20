package com.teamsantos.easybarber.repositories;

import org.springframework.stereotype.Repository;

@Repository
public interface EstablishmentStaffRepository extends JpaRepository<EstablishmentStaff, Long> {
	Optional<EstablishmentStaff> findByUserIdAndEstablishmentId(Long userId, Long establishmentId);
}
