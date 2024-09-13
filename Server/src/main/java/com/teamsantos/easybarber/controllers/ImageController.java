package com.teamsantos.easybarber.controllers;

import java.util.List;
import java.util.Set;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.base.Image;
import com.teamsantos.easybarber.exceptions.GenericNotFoundException;
import com.teamsantos.easybarber.services.ServiceWithImages;

public abstract class ImageController<T extends EntityWithImages<T, E>, E extends Image<T, E>> {
    protected ServiceWithImages<T, E> service;

    public ImageController(ServiceWithImages<T, E> service) {
        this.service = service;
    }

    public abstract boolean canEdit(long entityId);

    @Transactional
    @PostMapping("/{entityId}/images")
    public ResponseEntity<BaseResponseDTO> addImages(@PathVariable("entityId") long entityId,
            @RequestBody List<ImageDTO> images) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            if (!canEdit(entityId)) {
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
            if (images.isEmpty()) {
                response.setResponseMessage("Images list is empty");
                return ResponseEntity.badRequest().body(response);
            }
            response.setIds(service.saveImages(entityId, images));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Transactional
    @DeleteMapping("/{entityId}/images")
    public ResponseEntity<BaseResponseDTO> deleteImages(@PathVariable("entityId") long entityId,
            @RequestBody Set<Long> imageIds) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            if (!canEdit(entityId)) {
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
            service.deleteImages(entityId, imageIds);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Transactional
    @PutMapping("/{entityId}/images/main/{image}")
    public ResponseEntity<BaseResponseDTO> setMainImage(@PathVariable("entityId") long entityId,
            @PathVariable("image") long image) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            if (!canEdit(entityId)) {
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }
            service.setMain(entityId, image);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Transactional(readOnly = true)
    @GetMapping("/{entityId}/images")
    public ResponseEntity<BasePageDTO<ImageDTO>> getImages(@PathVariable("entityId") Long entityId, Pageable pageable) {
        BasePageDTO<ImageDTO> response = new BasePageDTO<ImageDTO>();
        try {
            response.setItems(service.getImages(entityId, pageable));
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            System.err.println(e.getMessage());
            response.setResponseMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Transactional(readOnly = true)
    @GetMapping("/{entityId}/images/main")
    public ResponseEntity<ImageDTO> getMainImage(@PathVariable("entityId") Long entityId) {
        try {
            return ResponseEntity.ok(service.getMainImage(entityId));
        } catch (GenericNotFoundException e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
