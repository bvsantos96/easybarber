package com.teamsantos.easybarber.repositories;

import com.teamsantos.easybarber.entities.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
}
