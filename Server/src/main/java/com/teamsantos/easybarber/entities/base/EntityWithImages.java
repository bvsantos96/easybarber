package com.teamsantos.easybarber.entities.base;

import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.utils.Utils;
import jakarta.persistence.FetchType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import org.modelmapper.TypeToken;

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

    public void addImage(ImageDTO image) {
        if (image == null)
            images = new HashSet<>();
        images.add(Utils.getModelMapper().map(image, new TypeToken<E>() {
        }.getType()));
    }

    @SuppressWarnings("rawtypes")
    public void addImage(Image image) {
        if (image == null)
            images = new HashSet<>();
        images.add(Utils.getModelMapper().map(image, new TypeToken<E>() {
        }.getType()));
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
