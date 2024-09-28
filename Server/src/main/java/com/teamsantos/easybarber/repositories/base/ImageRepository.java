package com.teamsantos.easybarber.repositories.base;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import com.teamsantos.easybarber.DTO.image.ImageDTO;
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

    @Query("SELECT new com.teamsantos.easybarber.DTO.image.ImageDTO(i.id, i.data, i.isMain) FROM #{#entityName} i WHERE i.entity.id = :entityId")
    Page<ImageDTO> findByEntityId(long entityId, Pageable pageable);

    @Query("SELECT new com.teamsantos.easybarber.DTO.image.ImageDTO(i.id, i.data,i.isMain) FROM #{#entityName} i WHERE i.entity.id = :entityId")
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

    @Modifying
    @Query("DELETE FROM #{#entityName} i WHERE i.entity.id = :entityId AND i.id IN :imageIds")
    void deleteImages(long entityId, Set<Long> imageIds);

    @Query("""
                SELECT EXISTS (
                    SELECT 1
                FROM #{#entityName} i
                WHERE i.entity.id = :entityId
                AND i.id IN :imageIds
                AND i.isMain = true
                )
            """)
    boolean isAnyMainImage(long entityId, Set<Long> imageIds);

    @Query("SELECT i FROM #{#entityName} i WHERE i.id = :imageId AND i.entity.id = :entityId")
    Optional<E> findByIdAndEntityId(long imageId, long entityId);

    @Query("SELECT new com.teamsantos.easybarber.DTO.image.ImageDTO(i.id, i.data, i.isMain) FROM #{#entityName} i WHERE i.entity.id = :entityId AND i.isMain = true")
    Optional<ImageDTO> findMainImage(Long entityId);

    @Modifying
    @Query("""
             UPDATE #{#entityName} i
             SET i.isMain = true
             WHERE i.entity.id = :entityId AND i.id = :imageId
            """)
    void setNewMain(long entityId, long imageId);

    @Query("""
             SELECT MIN(i.id)
             FROM #{#entityName} i
             WHERE i.entity.id = :entityId
            """)
    Long findOldestImageId(long entityId);
}
