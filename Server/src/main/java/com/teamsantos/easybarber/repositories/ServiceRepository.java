package com.teamsantos.easybarber.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.entities.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
            FROM Service s
            JOIN s.employee e
            JOIN e.user u
            WHERE s.id = :serviceId
            AND u.mobileInformation = :mobileInformation""")
    boolean checkIfEmployeeIsServiceOwner(Long serviceId, String mobileInformation);

    List<Service> findByEmployeeId(Long id);
}
