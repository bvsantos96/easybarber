package com.teamsantos.easybarber.repositories.establishmentServices;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teamsantos.easybarber.DTO.ServiceFullDTO;
import com.teamsantos.easybarber.entities.EstablishmentService;
import com.teamsantos.easybarber.utils.Pair;

@Repository
public interface EstablishmentServiceRepository
        extends JpaRepository<EstablishmentService, Long>, CustomEstablishmentServiceRepository {

    void deleteByEstablishmentId(long id);

    void deleteByServiceId(long id);

    @Query("""
            SELECT es.service.duration
            FROM EstablishmentService es
            WHERE
                es.establishment.id = :establishmentId
            AND es.service.id = :serviceId
            """)
    Integer getDurationOfService(long establishmentId, long serviceId);

    @Query("""
            SELECT new com.teamsantos.easybarber.utils.Pair(es.id, es.service.duration)
            FROM EstablishmentService es
            WHERE
                es.establishment.id = :establishmentId
                AND es.service.id = :serviceId
            """)
    Pair<Long, Integer> getIdAndDuration(long establishmentId, long serviceId);

    @Query("""
            SELECT EXISTS (
            SELECT 1
                FROM EstablishmentService es
                WHERE
                    es.establishment.id = :establishmentId
                AND es.service.id = :serviceId
            )
            """)
    boolean existsByServiceIdAndEstablishmentId(long serviceId, long establishmentId);

    @Query("""
                SELECT com.teamsantos.easybarber.DTO.ServiceFullDTO(
                    es.id, es.service.name, es.service.description, es.service.duration, es.price, i,
                    es.service.serviceType.id, es.service.serviceType.name, es.service.serviceType, es.service.serviceType.imageURL,
                    es.service.employee.id, es.service.employee.user.name
                ) FROM EstablishmentService es
                LEFT JOIN es.service.images i
                WHERE
                    es.establishment.id = :establishmentId
                AND es.service.id = :serviceId
            """)
    ServiceFullDTO findByEstablishmentIdAndServiceId(long establishmentId, long serviceId);
}
