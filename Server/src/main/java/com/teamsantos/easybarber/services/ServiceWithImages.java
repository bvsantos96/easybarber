package com.teamsantos.easybarber.services;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.entities.Image;
import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.repositories.ImageRepository;

public abstract class ServiceWithImages<T extends EntityWithImages> {
    public abstract JpaRepository<T, Long> getRepository();

    public abstract ImageRepository getImageRepository();

    public abstract ModelMapper getModelMapper();

    public abstract T getEntity();

    public abstract void setEntity(T entity);

    public void saveImages(Long entityId, List<ImageDTO> images) throws NotFoundException {
        setEntity(getRepository().findById(entityId).orElseThrow(NotFoundException::new));
        HashMap<Long, Image> toBeDeleted = null;
        if (getEntity().getImages() == null) {
            getEntity()
                    .setImages(
                            images.stream().map(i -> getModelMapper().map(i, Image.class)).collect(Collectors.toSet()));
        } else {
            toBeDeleted = getEntity().getImages()
                    .stream()
                    .collect(Collectors.toMap(Image::getId, image -> image, (existing, replacement) -> replacement,
                            HashMap::new));
            for (ImageDTO image : images) {
                if ((image.getData() != null && !image.getData().equals(""))) {
                    Long id = getImageRepository().getIdByEntityAndData(getEntity(), image.getData());
                    if (id == null || id == 0L) {
                        getEntity().getImages().add(getModelMapper().map(image, Image.class));
                    } else {
                        toBeDeleted.remove(id);
                    }
                }
            }
        }

        if (getEntity().getImages().isEmpty()) {
            getRepository().save(getEntity());
        }

        if (!toBeDeleted.isEmpty()) {
            getImageRepository().deleteAllById(toBeDeleted.keySet());
        }
    }
}
