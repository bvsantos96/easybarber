package com.teamsantos.easybarber.entities.base;

import java.util.List;
import java.util.Set;

import org.modelmapper.TypeToken;

import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.CascadeType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class EntityWithImages<T extends EntityWithImages<T, E>, E extends Image<T, E>> {
    @OneToMany(mappedBy = "entity", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<E> images;

    public Set<E> getImages() {
        setImages(images);
        return images;
    }

    public void addImage(ImageDTO image) {
        getImages().add(Utils.getModelMapper().map(image, new TypeToken<E>() {
        }.getType()));
    }

    @SuppressWarnings("rawtypes")
    public void addImage(Image image) {
        getImages().add(Utils.getModelMapper().map(image, new TypeToken<E>() {
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
