package com.teamsantos.easybarber.controllers;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.teamsantos.easybarber.DTO.BasePageDTO;
import com.teamsantos.easybarber.DTO.BaseResponseDTO;
import com.teamsantos.easybarber.DTO.ImageDTO;
import com.teamsantos.easybarber.entities.base.EntityWithImages;
import com.teamsantos.easybarber.entities.base.Image;
import com.teamsantos.easybarber.services.ServiceWithImages;

public abstract class ImageController<T extends EntityWithImages<T, E>, E extends Image<T, E>> {
    protected ServiceWithImages<T, E> service;

    public ImageController(ServiceWithImages<T, E> service) {
        this.service = service;
    }

    public abstract ResponseEntity<BaseResponseDTO> addImages(Long entityId, List<ImageDTO> images);

    public ResponseEntity<BaseResponseDTO> _addImages(Long entityId,
            List<ImageDTO> images) {
        BaseResponseDTO response = new BaseResponseDTO();
        try {
            if (images.isEmpty()) {
                response.setResponseMessage("Images list is empty");
                return ResponseEntity.badRequest().body(response);
            }
            service.saveImages(entityId, images);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (NotFoundException e) {
            System.err.println(e.getMessage());
            response.setResponseMessage("Establishment not found");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{entityId}/images")
    public ResponseEntity<BasePageDTO<ImageDTO>> getImages(@PathVariable("entityId") Long entityId, Pageable pageable) {
        BasePageDTO<ImageDTO> response = new BasePageDTO<ImageDTO>();
        try {
            response.setItems(service.getImages(entityId, pageable));
            return ResponseEntity.ok(response);
        } catch (NotFoundException e) {
            System.err.println(e.getMessage());
            response.setResponseMessage("Establishment not found");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            response.setResponseMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
