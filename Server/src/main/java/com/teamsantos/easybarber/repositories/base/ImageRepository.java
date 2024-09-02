package com.teamsantos.easybarber.repositories.base;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.base.Image;

@NoRepositoryBean
public interface ImageRepository<T extends EntityWithImages<T, E>, E extends Image<T, E>>
        extends JpaRepository<E, Long> {
    @Query("SELECT i.id FROM #{#entityName} i WHERE i.entity.id = :entityId AND i.data = :data")
    Long getIdByEntityIdAndData(long entityId, String data);

    @Modifying
    @Query("DELETE FROM #{#entityName} i WHERE i.entity.id = :id")
    void deleteByEntityId(@Param("id") long id);

    @Query("SELECT new com.teamsantos.easybarber.DTO.ImageDTO(i.id, i.data) FROM #{#entityName} i WHERE i.entity.id = :entityId")
    Page<ImageDTO> findByEntityId(long entityId, Pageable pageable);

    @Query("SELECT new com.teamsantos.easybarber.DTO.ImageDTO(i.id, i.data) FROM #{#entityName} i WHERE i.entity.id = :entityId")
    List<ImageDTO> findAllByEntityId(long entityId);

    @Query(value = """
            SELECT i.id
            FROM #{#entityName} i
            WHERE i.entity_id = :entityId
            AND i.is_main = :isMain
            LIMIT 1
            """, nativeQuery = true)
    Long getIdByEntityIdAndIsMain(long entityId, boolean isMain);

    @Modifying
    @Query("UPDATE #{#entityName} i SET i.isMain = false WHERE i.entity.id = :entityId AND i.isMain = true")
    void removeMainFlag(long entityId);

    @Query("""
                SELECT EXISTS (
                    SELECT 1
                    FROM #{#entityName} i
                    WHERE i.entity.id = :entityId
                    AND i.isMain = true
                )
            """)
    boolean existsMain(long entityId);
}
