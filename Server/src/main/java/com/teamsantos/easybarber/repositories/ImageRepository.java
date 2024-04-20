package com.teamsantos.easybarber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Image;
import com.teamsantos.easybarber.entities.base.EntityWithImages;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

	Long getIdByEntityAndData(EntityWithImages entity, String data);
}
