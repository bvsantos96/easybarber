package com.teamsantos.easybarber.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.ServiceType;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    @Query("SELECT st FROM ServiceType st INNER JOIN st.service s")
    List<ServiceType> list();
}
