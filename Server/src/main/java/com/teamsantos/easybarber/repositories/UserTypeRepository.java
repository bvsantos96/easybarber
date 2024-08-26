package com.teamsantos.easybarber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.UserType;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {
}
