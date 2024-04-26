package com.teamsantos.easybarber.entities.base;

import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.FetchType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@MappedSuperclass
public abstract class EntityWithImages<T extends EntityWithImages<T, E>, E extends Image<T, E>> {
    @OneToMany(mappedBy = "entity", fetch = FetchType.LAZY)
    private Set<E> images = new HashSet<>();

    @PrePersist
    public void prePersist() {
        if (getImages() == null)
            setImages(new HashSet<>());
        for (final E image : getImages())
            image.setEntity(getEntity());
    }

    public void addImage(E image) {
        if (image == null)
            images = new HashSet<>();
        images.add(image);
    }

    public void addImage(Object image) {
        if (image == null)
            images = new HashSet<>();
        images.add(getImage(image));
    }

    public E getImage(Object image) {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        @SuppressWarnings("unchecked")
        Class<E> imageClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
        E imageEntity = Utils.getModelMapper().map(image, imageClass);
        imageEntity.setEntity(getEntity());
        return imageEntity;
    }

    public void removeImage(E image) {
        getImages().remove(image);
    }

    public void setImages(Set<E> images) {
        for (final E image : images)
            image.setEntity(getEntity());
        this.images = images;
    }

    public void setImages(List<ImageDTO> images) {
        for (final ImageDTO image : images)
            addImage(image);
    }

    public abstract T getEntity();
}
