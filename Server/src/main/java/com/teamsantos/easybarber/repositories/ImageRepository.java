package com.teamsantos.easybarber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.base.Image;

@Repository
public interface ImageRepository<T> extends JpaRepository<Image<T>, Long> {
    Long getIdByEntityAndData(T entity, String data);
}
