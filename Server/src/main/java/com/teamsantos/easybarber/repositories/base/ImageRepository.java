package com.teamsantos.easybarber.repositories.base;

import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.base.Image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface ImageRepository<T extends EntityWithImages<T, E>, E extends Image<T, E>>
        extends JpaRepository<E, Long> {
    @Query("SELECT i.id FROM #{#entityName} i WHERE i.entity.id = :entityId AND i.data = :data")
    Long getIdByEntityIdAndData(Long entityId, String data);

    @Modifying
    @Query("DELETE FROM #{#entityName} i WHERE i.entity.id = :id")
    void deleteByEntityId(@Param("id") Long id);
}
