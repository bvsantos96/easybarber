package com.teamsantos.easybarber.services;

import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.base.Image;
import com.teamsantos.easybarber.repositories.base.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ServiceWithImages<T extends EntityWithImages<T, E>, E extends Image<T, E>> {
    protected JpaRepository<T, Long> repository;
    protected ImageRepository<T, E> imageRepository;
    protected ModelMapper modelMapper;
    protected T entity;

    public ServiceWithImages(JpaRepository<T, Long> repository, ImageRepository<T, E> imageRepository,
            ModelMapper modelMapper) {
        this.repository = repository;
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
    }

    public void saveImages(Long entityId, List<ImageDTO> images) throws NotFoundException {
        entity = repository.findById(entityId).orElseThrow(NotFoundException::new);
        HashMap<Long, E> toBeDeleted = null;
        if (entity.getImages() == null) {
            entity
                    .setImages(images);
        } else {
            toBeDeleted = entity.getImages()
                    .stream()
                    .collect(Collectors.toMap(Image::getId, image -> image, (existing, replacement) -> replacement,
                            HashMap::new));
            for (ImageDTO image : images) {
                if ((image.getUrl() != null && !image.getUrl().isEmpty())) {
                    Long id = imageRepository.getIdByEntityAndData(entity, image.getUrl());
                    if (id == null || id == 0L) {
                        entity.addImage(image);
                    } else {
                        toBeDeleted.remove(id);
                    }
                }
            }
        }

        if (entity.getImages().isEmpty()) {
            repository.save(entity);
        }

        if (!toBeDeleted.isEmpty()) {
            imageRepository.deleteAllById(toBeDeleted.keySet());
        }
    }
}
