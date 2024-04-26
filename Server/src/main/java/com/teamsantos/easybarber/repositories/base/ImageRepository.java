package com.teamsantos.easybarber.repositories.base;

import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.base.Image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT new com.teamsantos.easybarber.DTO.ImageDTO(i.id, i.data) FROM #{#entityName} i WHERE i.entity.id = :entityId")
    Page<ImageDTO> findByEntityId(Long entityId, Pageable pageable);
}
