package com.teamsantos.easybarber.services;

import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.base.Image;
import com.teamsantos.easybarber.repositories.base.ImageRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
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

    private void setEntityById(Long entityId) throws NotFoundException {
        entity = repository.findById(entityId).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void saveImages(Long entityId, List<ImageDTO> images) throws NotFoundException {
        setEntityById(entityId);
        HashMap<Long, E> toBeDeleted = null;
        List<E> imagesToAdd = new ArrayList<>();
        if (entity.getImages() == null || entity.getImages().isEmpty()) {
            for (ImageDTO image : images) {
                if ((image.getData() != null && !image.getData().isEmpty())) {
                    imagesToAdd.add(entity.getImage(image));
                }
            }
        } else {
            toBeDeleted = entity.getImages()
                    .stream()
                    .collect(Collectors.toMap(Image::getId, image -> image, (existing, replacement) -> replacement,
                            HashMap::new));
            for (ImageDTO image : images) {
                if ((image.getData() != null && !image.getData().isEmpty())) {
                    Long id = imageRepository.getIdByEntityIdAndData(entityId, image.getData());
                    if (id == null || id == 0L) {
                        imagesToAdd.add(entity.getImage(image));
                    } else {
                        toBeDeleted.remove(id);
                    }
                }
            }
        }

        if (toBeDeleted != null && !toBeDeleted.isEmpty()) {
            imageRepository.deleteAllById(toBeDeleted.keySet());
        }

        if (!imagesToAdd.isEmpty()) {
            imageRepository.saveAll(imagesToAdd);
        }
    }

    public Page<ImageDTO> getImages(Long entityId, Pageable pageable) throws NotFoundException {
        return imageRepository.findByEntityId(entityId, pageable);
    }
}
