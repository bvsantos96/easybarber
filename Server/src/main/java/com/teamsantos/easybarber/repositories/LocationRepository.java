package com.teamsantos.easybarber.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Location;
import com.teamsantos.easybarber.entities.User;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Page<Location> findLocationByUser(User user, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Location l WHERE l.latitude = :latitude AND l.longitude = :longitude AND l.user = :user")
    int deleteIfExist(@Param("latitude") double latitude, @Param("longitude") double longitude,
            @Param("user") User user);

    @Modifying
    @Query("UPDATE Location l SET l.selected = false WHERE l.user = :user AND l.selected = true")
    int unSelectAll(User user);
}
