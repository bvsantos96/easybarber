package com.teamsantos.easybarber.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Location;
import com.teamsantos.easybarber.entities.User;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findLocationByUser(User user);
}
