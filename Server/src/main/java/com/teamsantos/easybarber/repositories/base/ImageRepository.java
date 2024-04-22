package com.teamsantos.easybarber.repositories.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.base.Image;

@NoRepositoryBean
public interface ImageRepository<T extends EntityWithImages<T, E>, E extends Image<T, E>>
        extends JpaRepository<E, Long> {
    Long getIdByEntityAndData(T entity, String data);
}
