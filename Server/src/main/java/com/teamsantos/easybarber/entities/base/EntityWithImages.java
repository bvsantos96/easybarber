package com.teamsantos.easybarber.entities.base;

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
public abstract class EntityWithImages<T> {
    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Image<T>> images;

    public Set<Image<T>> getImages() {
        setImages(images);
        return images;
    }

    public void addImage(ImageDTO image) {
        getImages().add(Utils.getModelMapper().map(image, new TypeToken<Image<T>>() {
        }.getType()));
    }

    @SuppressWarnings("rawtypes")
    public void addImage(Image image) {
        getImages().add(Utils.getModelMapper().map(image, new TypeToken<Image<T>>() {
        }.getType()));
    }

    public void removeImage(Image<T> image) {
        getImages().remove(image);
    }

    public void setImages(Set<Image<T>> images) {
        for (final Image<T> image : images)
            image.setEntity(getEntity());
        this.images = images;
    }

    public abstract T getEntity();
}
