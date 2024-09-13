package com.teamsantos.easybarber.services;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.base.Image;
import com.teamsantos.easybarber.exceptions.GenericNotFoundException;
import com.teamsantos.easybarber.repositories.base.ImageRepository;
import com.teamsantos.easybarber.utils.Utils;

import jakarta.persistence.EntityManager;

public class ServiceWithImages<T extends EntityWithImages<T, E>, E extends Image<T, E>> {
    protected JpaRepository<T, Long> repository;
    protected ImageRepository<T, E> imageRepository;
    protected ModelMapper modelMapper;
    protected EntityManager entityManager;
    protected Class<T> entityClass;
    protected Class<E> imageClass;

    @SuppressWarnings("unchecked")
    public ServiceWithImages(JpaRepository<T, Long> repository, ImageRepository<T, E> imageRepository,
            ModelMapper modelMapper, EntityManager entityManager) {
        this.repository = repository;
        this.imageRepository = imageRepository;
        this.modelMapper = modelMapper;
        this.entityManager = entityManager;
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
        this.imageClass = (Class<E>) genericSuperclass.getActualTypeArguments()[1];
    }

    public E parseImage(EntityManager entityManager, Object image, Long entityId) {
        E imageEntity = Utils.getModelMapper().map(image, imageClass);
        imageEntity.setEntity(entityManager.getReference(entityClass, entityId));
        return imageEntity;
    }

    @Transactional
    public List<Long> saveImages(long entityId, List<ImageDTO> images) {
        List<E> imagesToAdd = new ArrayList<>();
        boolean newMain = false;

        for (ImageDTO image : images) {
            if ((image.getData() != null && !image.getData().isEmpty())) {
                if (image.getMain() != null && image.getMain()) {
                    if (newMain) {
                        image.setMain(false);
                    } else {
                        newMain = true;
                    }
                }
                E imageEntity = Utils.getModelMapper().map(image, imageClass);
                imageEntity.setEntity(entityManager.getReference(entityClass, entityId));
                imagesToAdd.add(imageEntity);
            }
        }
        if (newMain) {
            imageRepository.removeMainFlag(entityId);
        } else if (!imageRepository.existsMain(entityId)) {
            if (!imagesToAdd.isEmpty()) {
                imagesToAdd.get(0).setMain(true);
            }
        }
        if (!imagesToAdd.isEmpty()) {
            return imageRepository.saveAll(imagesToAdd).stream().map(Image::getId).toList();
        }

        return new ArrayList<>();
    }

    @Transactional
    public void deleteImages(long entityId, Set<Long> imageIds) {
        boolean mainDeleted = imageRepository.isAnyMainImage(entityId, imageIds);
        imageRepository.deleteImages(entityId, imageIds);
        if (mainDeleted) {
            Long id = imageRepository.findOldestImageId(entityId);
            if (id != null) {
                imageRepository.setNewMain(entityId, id);
            }
        }
    }

    @Transactional
    public void setMain(long entityId, long imageId) throws GenericNotFoundException {
        imageRepository.removeMainFlag(entityId);
        E i = imageRepository.findByIdAndEntityId(imageId, entityId)
                .orElseThrow(() -> new GenericNotFoundException("Image not found"));
        i.setMain(true);
        imageRepository.save(i);
    }

    @Transactional(readOnly = true)
    public Page<ImageDTO> getImages(Long entityId, Pageable pageable) throws NotFoundException {
        return imageRepository.findByEntityId(entityId, pageable);
    }

    @Transactional(readOnly = true)
    public ImageDTO getMainImage(Long entityId) throws GenericNotFoundException {
        return imageRepository.findMainImage(entityId)
                .orElseThrow(() -> new GenericNotFoundException("Image not found"));
    }
}
