package com.teamsantos.easybarber.entities.base;

import java.util.HashSet;
import java.util.Set;

import com.teamsantos.easybarber.entities.Image;

import jakarta.persistence.CascadeType;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;

@MappedSuperclass
public abstract class EntityWithImages {
    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Image> images = new HashSet<>();

    public Set<Image> getImages() {
        setImages(images);
        return images;
    }

    public void setImages(Set<Image> images) {
        for(final Image image : images)
            image.setEntity(this);
        this.images = images;
    }

    public abstract String getEntityType();
}
